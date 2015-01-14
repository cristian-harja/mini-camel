package mini_camel;

import java.io.*;
import mini_camel.ast.*;
import mini_camel.gen.Lexer;
import mini_camel.gen.Parser;
import mini_camel.transform.*;


/**
 * Created by mommess on 11/01/15.
 */
public class TestTransform {
    static public void main(String argv[]) {
        try {
            Reader r;
            //r = new StringReader("let x = y in x + y");
            //r = new StringReader("let y = 4 in let x = y in let z = print_int(x) in z");
            r = new StringReader("let x = 4 in let y = 12 in let z = x - y in z - 3 + 12");

            //r = new StringReader("let x = 1.5 in let y = 2.74 in x +. y");

            Parser p = new Parser(new Lexer(r));
            AstExp result = (AstExp) p.parse().value;

            if (result == null) {
                return;
            }


            AlphaConv ac = new AlphaConv();
            AstExp res2 = ac.applyTransform(result);


            BetaReduc br = new BetaReduc();
            AstExp res3 = br.applyTransform(res2);

            ConstantFold cf = new ConstantFold();
            AstExp res4 = cf.applyTransform(res3);





            System.out.println("Printing the visitor : ");
            res4.accept(new PrintVisitor(System.out));
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

