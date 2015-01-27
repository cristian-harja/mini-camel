package mini_camel;

import java.io.*;
import mini_camel.comp.MyCompiler;


public class TestTransform {
    static public void main(String argv[]) {
        try {
            Reader r;
            //r = new StringReader("let x = y in x + y");
            //r = new StringReader("let y = 4 in let x = y in let z = print_int(x) in z");
            r = new StringReader("let x = 4 in let y = 12 in let z = x - y in z - 3 + 12");



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

