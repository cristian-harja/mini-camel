package mini_camel.comp;

import ldf.java_cup.runtime.*;
import mini_camel.ir.CodeGenerator;
import mini_camel.ir.CodeGenerator2;
import mini_camel.ir.instr.Label;
import mini_camel.knorm.KNode;
import mini_camel.knorm.Program;
import mini_camel.type.*;
import mini_camel.util.SymDef;
import mini_camel.util.SymRef;
import mini_camel.util.Pair;
import mini_camel.visit.*;
import mini_camel.ast.AstExp;
import mini_camel.gen.Lexer;
import mini_camel.gen.Parser;
import mini_camel.ir.Function;
import mini_camel.ir.instr.Instr;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.util.*;

public class MyCompiler {
    private Reader inputReader;
    private AstExp parsedAst;
    private AstExp transformedAst;
    private KNode kNormalized;
    private Program closureConv;
    private List<Function> funDefs;

    private final Set<ErrMsg> messageLog = new TreeSet<>();

    private boolean parseBegun, parseSuccessful, parseErrors;
    private boolean freeVarsBegun, freeVarsSuccessful;
    private boolean typingBegun, typingSuccessful;


    public static final Map<String, Type> PREDEFS;
    static {
        Map<String, Type> predefs  = new LinkedHashMap<>();

        Type UNIT = TUnit.INSTANCE;
        Type INT = TInt.INSTANCE;
        Type FLOAT = TFloat.INSTANCE;

        predefs.put("print_newline", new TFun(UNIT, UNIT));
        predefs.put("print_int", new TFun(INT, UNIT));

        Type floatFun = new TFun(FLOAT, FLOAT);

        predefs.put("abs_float", floatFun);
        predefs.put("sqrt", floatFun);
        predefs.put("sin", floatFun);
        predefs.put("cos", floatFun);

        predefs.put("float_of_int", new TFun(INT, FLOAT));
        predefs.put("int_of_float", new TFun(FLOAT, INT));
        predefs.put("truncate", new TFun(FLOAT, INT));

        PREDEFS = Collections.unmodifiableMap(predefs);
    }

    private Checker typeChecker;

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

    public void warn(@Nonnull String msg) {
        ErrMsg m = new ErrMsg();
        m.type = ErrMsg.Type.WARN;
        m.message = msg;
        messageLog.add(m);
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

        FreeVars fvv = FreeVars.compute(parsedAst, PREDEFS.keySet());

        Set<SymRef> freeVars = fvv.getFreeVariables();

        for (SymRef i : freeVars) {
            error(i.getSymbol(), "Unknown symbol: " + i.id + ".");
        }

        return freeVarsSuccessful = freeVars.isEmpty();
    }

    public boolean typeCheck() {
        if (typingBegun) return typingSuccessful;
        typingBegun = true;

        typeChecker = new Checker(parsedAst, PREDEFS);
        typingSuccessful = typeChecker.wellTyped();

        if (!typingSuccessful) {
            for (Pair<Type, Type> e : typeChecker.getErrors()) {
                ErrMsg msg = new ErrMsg();
                msg.type = ErrMsg.Type.ERROR;
                msg.message = "Type mismatch: " + e.left + " = " + e.right;
                messageLog.add(msg);
            }
        }

        return typingSuccessful;
    }

    public void outputAST(PrintStream out) {
        parsedAst.accept(new PrettyPrinter(out));
        out.print("\n");
    }

    public void outputTransformedAST(PrintStream out) {
        transformedAst.accept(new PrettyPrinter(out));
        out.print("\n");
    }

    private void reportUnusedVars() {
        Set<String> unusedVars = UnusedVar.compute(parsedAst);
        for (String unusedVar : unusedVars) {
            warn("Unused variable: " + unusedVar);
        }
    }

    private boolean checkDuplicateDecl() {
        boolean passed = true;
        List<Collection<SymDef>> dup = DuplicateDecl.compute(parsedAst);
        for (Collection<SymDef> incident : dup) {
            for (SymDef sym : incident) {
                error(sym.getSymbol(),
                        "Duplicate symbol declaration: " + sym.id
                );
                passed = false;
            }
        }
        return passed;
    }

    private void transformAlphaConversion() {
        transformedAst = AlphaConv.compute(transformedAst);
    }

    private void transformBetaReduction() {
        transformedAst = BetaReduction.compute(transformedAst);
    }

    private void transformConstantFolding() {
        transformedAst = ConstantFold.compute(transformedAst);
    }
    private void transformInlining() {
        transformedAst = Inlining.compute(transformedAst);
    }

    private void transformElimination() {
        transformedAst = UnusedElim.compute(transformedAst);
    }

    public boolean preProcessCode() {

        reportUnusedVars();

        if (!checkDuplicateDecl()) return false;

        transformAlphaConversion();
        transformBetaReduction();
        int i = 0;
        while(i < 4){
            i++;
            transformInlining();
            transformConstantFolding();
            transformElimination();
        }
        return true;
    }

    @Deprecated
    @SuppressWarnings("unused")
    public boolean codeGeneration_old() {
        try {
            funDefs = CodeGenerator.generateIR(transformedAst, "_main");
        } catch (RuntimeException e) {
            error("An exception has occurred while generating the IR.", e);
            return false;
        }
        return true;
    }

    public boolean performKNormalization() {
        try {
            kNormalized = KNormalize.compute(transformedAst);
        } catch (RuntimeException e) {
            error("An exception has occurred while K-normalizing.", e);
            return false;
        }
        return true;
    }

    public boolean performClosureConversion() {
        try {
            closureConv = ClosureConv.compute(kNormalized, PREDEFS.keySet());
        } catch (RuntimeException e) {
            error("An exception has occurred during closure-conversion.", e);
            return false;
        }
        return true;
    }

    public boolean generateIR() {
        try {
            funDefs = CodeGenerator2.compile(
                    PREDEFS, typeChecker, closureConv, "_main"
            );
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
            out.println("# Closure: " + fd.free);
            out.println("# Arguments: " + fd.args);
            out.println("# Locals: " + fd.locals);
            out.println(fd.name.toString());
            for (Instr i : fd.body) {
                if (!(i instanceof Label)) out.print('\t');
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