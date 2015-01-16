package mini_camel;

import mini_camel.comp.*;

import java.io.*;

public class TestAssemblyCode {
    static public void main(String argv[]) {

        Reader r;
        //r = new StringReader("let x = 1 in print_newline");
        r = new StringReader("let x = 5 in print_int(x)");

        MyCompiler c = new MyCompiler(r);

        c.parseCode();
        c.preProcessCode();

        c.codeGeneration();

        c.outputIR(System.out);


        c.outputAssembly(System.out);
    }
}

