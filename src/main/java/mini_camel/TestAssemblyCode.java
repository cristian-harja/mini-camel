package mini_camel;

import mini_camel.comp.MyCompiler;

import java.io.*;

public class TestAssemblyCode {
    static public void main(String argv[]) {
        try {
            MyCompiler c = new MyCompiler(new StringReader("print_newline"));
            c.codeGeneration();
            c.outputAssembly(new PrintStream("ARM/output.s"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

