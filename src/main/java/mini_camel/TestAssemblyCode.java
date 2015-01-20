package mini_camel;

import mini_camel.comp.*;

import java.io.*;

public class TestAssemblyCode {
    static public void main(String argv[]) {

        Reader r;
        //r = new StringReader("let x = 1 in print_newline");
        //r = new StringReader("let x = let y = 3 in let z = 4 in let t = z + 1 in 4 + t");
        r = new StringReader("let z = 3 in let x = z in 3 + z");

        MyCompiler c = new MyCompiler(r);

        c.parseCode();
        c.preProcessCode();

        /*
        c.codeGeneration();

        c.outputIR(System.out);


        c.outputAssembly(System.out);*/
    }
}

