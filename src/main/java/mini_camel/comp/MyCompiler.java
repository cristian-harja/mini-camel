package mini_camel.comp;

import mini_camel.ErrMsg;
import mini_camel.Pair;
import mini_camel.PrintVisitor;
import mini_camel.ast.AstExp;
import mini_camel.gen.Lexer;
import mini_camel.gen.Parser;
import mini_camel.transform.AlphaConv;
import mini_camel.transform.BetaReduc;
import mini_camel.transform.ConstantFold;
import mini_camel.type.Checker;
import mini_camel.type.Type;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MyCompiler {
    private Reader inputReader;
    private AstExp parsedAst;
    private AstExp transformedAst;

    private List<ErrMsg> messageLog = new ArrayList<>(100);

    private boolean parseBegun, parseSuccessful;
    private boolean typingBegun, typingSuccessful;

    public MyCompiler(@Nonnull Reader input) {
        inputReader = input;
    }

    public boolean parseCode() {
        Parser p;

        if (parseBegun) return parseSuccessful;
        parseBegun = true;

        try {
            p = new Parser(new Lexer(inputReader));
            parsedAst = (AstExp) p.parse().value;
        } catch (Exception e) {
            ErrMsg msg = new ErrMsg();
            msg.type = ErrMsg.Type.ERROR;
            msg.message = "An exception has occurred.";
            msg.ex = e;
            messageLog.add(msg);
            return false;
        }

        if (parsedAst == null && messageLog.size() != 0) {
            ErrMsg msg = new ErrMsg();
            msg.type = ErrMsg.Type.ERROR;
            msg.message = "Parser returned `null` instead of an AST.";
            return false;
        }

        transformedAst = parsedAst;
        return parseSuccessful = true;
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

    public boolean preProcessCode() {
        transformAlphaConversion();
        transformBetaReduction();
        transformConstantFolding();
        return true;
    }

    // virtual code generation, immediate optimisation and register allocation
    // build 3-adress code ??
    public boolean codeGeneration() {

        return true;
    }

    //from 3-adress code to Assembly code
    public void outputAssembly(PrintStream file_out) {

        //headers for .data and .text sections
        StringBuilder data = new StringBuilder();
        data.append("\t.data\n");
        StringBuilder text = new StringBuilder();
        text.append("\n\t.text\n\t.global _start\n_start:\n");

        AssemblyVisitor v = new AssemblyVisitor(data, text);
        transformedAst.accept(v);

        //footer for .text section and printing everything in the output_file
        text.append("\tbl min_caml_exit\n\n");
        if (data.length() > 7) {
            file_out.print(data.toString());
        }
        file_out.print(text.toString());
        file_out.close();

    }

    public void outputIR(PrintStream out) {
        throw new RuntimeException("Not implemented");
    }

    public void printErrors(PrintStream err) {
        StringBuilder sb = new StringBuilder(200);
        for (ErrMsg msg : messageLog) {
            sb.setLength(0);
            sb.append('[');
            sb.append(msg.type);
            sb.append("]: ");
            sb.append(msg.message);
            err.println(sb.toString());

            if (msg.ex == null) {
                continue;
            }
            msg.ex.printStackTrace(err);
        }
    }
}