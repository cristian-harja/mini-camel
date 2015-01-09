package mini_camel;

import mini_camel.ast.AstExp;
import mini_camel.comp.MyCompiler;

import java.io.*;

public class TestAssemblyCode {
    static public void main(String argv[]) {
        try {
            Reader r;
            r = new StringReader("print_newline");


            Parser p = new Parser(new Lexer(r));
            AstExp result = (AstExp) p.parse().value;
            if (result == null) {
                return;
            }

            MyCompiler c = new MyCompiler(result);
            c.preprocessCode();
            c.codeGeneration();
            c.generateAssembly("ARM/output.s");


            System.out.println("Printing the visitor : ");
            result.accept(new PrintVisitor(System.out));
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

