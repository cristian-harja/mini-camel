package mini_camel;


import mini_camel.comp.*;

import java.io.*;

public class TestAssemblyCode {
    static public void main(String argv[]) {

        Reader r;
        //r = new StringReader("let x = 1 in print_newline");
        r = new StringReader("let x = 1 in 5");

        MyCompiler c = new MyCompiler(r);

        c.parseCode();
        c.preProcessCode();

        System.out.println("starting code gen\n");
        c.codeGeneration();

        System.out.println("starting assembly gen\n");

        PrintStream s = System.out;
        c.outputAssembly(s);
    }
}

