package mini_camel;

import java.io.*;
import mini_camel.comp.MyCompiler;


public class TestTransform {
    static public void main(String argv[]) {
        try {
            Reader r;
            //r = new StringReader("let x = y in x + y");
            r = new StringReader("let rec comp x y = if x = y then 1 else 0 in let a = comp true false in print_int(a)");
            //r = new StringReader("let x = 4 in let y = 12 in let z = x - y in z - 3 + 12");
            //r = new StringReader("let rec f x = let x = 1 in 4 in f(x)");
            //r = new StringReader("let t = 123 in let x = 12 in let a = true in let b = true in let s = 1 + if(b) then x else if not(a) = b then t else x + t in print_int(s)");



            MyCompiler c = new MyCompiler(r);

            if(!c.parseCode()){
                c.printErrors(System.err);
                return;
            }

            if(!c.freeCheck()){
                c.printErrors(System.err);
                return;
            }

            if(!c.typeCheck()){
                c.printErrors(System.err);
                return;
            }

            if(!c.preProcessCode()){
                c.printErrors(System.err);
                return;
            }

            c.outputTransformedAst(System.out);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

