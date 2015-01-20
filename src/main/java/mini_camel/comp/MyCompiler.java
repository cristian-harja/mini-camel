package mini_camel.comp;

import ldf.java_cup.runtime.*;
import mini_camel.ErrMsg;
import mini_camel.Pair;
import mini_camel.PrintVisitor;
import mini_camel.ast.AstExp;
import mini_camel.ast.Id;
import mini_camel.gen.Lexer;
import mini_camel.gen.Parser;
import mini_camel.transform.*;
import mini_camel.ir.Function;
import mini_camel.ir.instr.Instr;
import mini_camel.transform.AlphaConv;
import mini_camel.transform.BetaReduc;
import mini_camel.transform.ConstantFold;
import mini_camel.type.Checker;
import mini_camel.type.Type;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.util.*;

import static mini_camel.ir.CodeGenerator.*;

public class MyCompiler {
    private Reader inputReader;
    private AstExp parsedAst;
    private AstExp transformedAst;
    private List<Function> funDefs;

    private final Set<ErrMsg> messageLog = new TreeSet<>();

    private boolean parseBegun, parseSuccessful, parseErrors;
    private boolean freeVarsBegun, freeVarsSuccessful;
    private boolean typingBegun, typingSuccessful;

    private static Set<String> PREDEFS = new LinkedHashSet<>();
    static {
        Collections.addAll(PREDEFS,
                "print_newline", "print_int", "abs_float", "sqrt", "sin",
                "cos", "float_of_int", "int_of_float", "truncate"
        );
    }

    public MyCompiler(@Nonnull Reader input) {
        inputReader = input;
    }

    public void error(
            @Nullable LocationAwareEntity loc,
            @Nonnull String msg,
            @Nullable Exception e
    ) {
        ErrMsg m = new ErrMsg();
        m.type = ErrMsg.Type.ERROR;
        m.loc = loc;
        m.message = msg;
        m.ex = e;
        messageLog.add(m);
    }

    public void error(LocationAwareEntity loc, @Nonnull String msg) {
        error(loc, msg, null);
    }

    private void error(String s, Exception e) {
        error(null, s, e);
    }

    public void error(@Nonnull String s) {
        error(null, s, null);
    }

    public void parseError(Symbol s) {
        parseErrors = true;

        error(s, "Syntax error. Unexpected " + s.getSymbolName() + ".");
    }

    public boolean parseCode() {
        Parser p;

        if (parseBegun) return parseSuccessful;
        parseBegun = true;

        try {
            TokenFactory tf = new MyTokenFactory();
            p = new Parser(this, tf, new Lexer(inputReader, tf));
            parsedAst = (AstExp) p.parse().value;
        } catch (Exception e) {
            error("An exception has occurred while parsing.", e);
            return false;
        }

        if (parsedAst == null && messageLog.size() != 0) {
            error("Parser returned `null` instead of an AST.");
            return false;
        }

        transformedAst = parsedAst;
        return parseSuccessful = !parseErrors;
    }

    public AstExp getParseTree() {
        return parseCode() ? parsedAst : null;
    }

    public boolean freeCheck() {
        if (freeVarsBegun) return freeVarsSuccessful;
        freeVarsBegun = true;

        FreeVarVisitor fvv = new FreeVarVisitor(PREDEFS);
        parsedAst.accept(fvv);

        Set<Id> freeVars = fvv.getFreeVariables();

        for (Id i : freeVars) {
            error(i.getSymbol(), "Unknown symbol: " + i.id + ".");
        }

        return freeVarsSuccessful = freeVars.isEmpty();
    }

    public boolean typeCheck() {
        if (typingBegun) return typingSuccessful;
        typingBegun = true;

        Checker c = new Checker(parsedAst);
        typingSuccessful = c.wellTyped();

        if (!typingSuccessful) {
            for (Pair<Type, Type> e : c.getErrors()) {
                ErrMsg msg = new ErrMsg();
                msg.type = ErrMsg.Type.ERROR;
                msg.message = "Type mismatch: " + e.left + " = " + e.right;
                messageLog.add(msg);
            }
        }

        return typingSuccessful;
    }

    public void outputAST(PrintStream out) {
        parsedAst.accept(new PrintVisitor(out));
    }

    private void transformAlphaConversion() {
        AlphaConv ac = new AlphaConv();
        transformedAst = ac.applyTransform(transformedAst);
    }

    private void transformBetaReduction() {
        BetaReduc br = new BetaReduc();
        transformedAst = br.applyTransform(transformedAst);
    }

    private void transformConstantFolding() {
        ConstantFold cf = new ConstantFold();
        transformedAst = cf.applyTransform(transformedAst);
    }

    private void transformElimination() {
        UnusedVar cff = new UnusedVar();
        Set<String> unused = cff.applyTransform(transformedAst);

        Elim cf = new Elim();
        cf.applyTransform(transformedAst, unused);
    }

    public boolean preProcessCode() {
        System.out.println("ETAPE 1 : "+transformedAst.toString());
        transformAlphaConversion();
        System.out.println("ETAPE 2 : "+transformedAst.toString());
        /*transformBetaReduction();
        transformConstantFolding();*/
        transformElimination();
        return true;
    }

    // virtual code generation, immediate optimisation and register allocation
    // build 3-adress code ??
    public boolean codeGeneration() {
        try {
            funDefs = generateIR(transformedAst, "_main");
        } catch (RuntimeException e) {
            error("An exception has occurred while generating the IR.", e);
            return false;
        }
        return true;
    }

    //from 3-address code to Assembly code
    public boolean outputAssembly(PrintStream out) {
        try {
            AssemblyGenerator ag = new AssemblyGenerator();
            ag.generateAssembly(funDefs);
            ag.writeAssembly(out);
        }catch (RuntimeException e) {
            error("An exception has occurred while generating assembly.", e);
            return false;
        }
        return true;
    }

    public void outputIR(PrintStream out) {
        for(Function fd : funDefs){
            out.println("# Function: " + fd.name.name);
            out.println("# Arguments: " + fd.args);
            out.println("# Locals: " + fd.locals);
            for (Instr i : fd.body) {
                out.print('\t');
                out.println(i.toString());
            }
            out.println();
        }
    }

    public void printErrors(PrintStream err) {
        StringBuilder sb = new StringBuilder(1000);
        for (ErrMsg msg : messageLog) {
            sb.setLength(0);
            sb.append('[').append(msg.type).append("]");

            LocationAwareEntity loc = msg.loc;
            if (loc != null) {
                sb.append(" (");
                sb.append(loc.getLineL());
                sb.append(':');
                sb.append(loc.getColumnL());
                sb.append(")-(");
                sb.append(loc.getLineR());
                sb.append(':');
                sb.append(loc.getColumnR());
                sb.append("):");
            }

            sb.append(' ');
            sb.append(msg.message);
            err.println(sb.toString());

            if (msg.ex != null) {
                msg.ex.printStackTrace(err);
            }
        }
    }

}