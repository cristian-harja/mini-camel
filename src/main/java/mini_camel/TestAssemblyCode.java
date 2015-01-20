package mini_camel;

import mini_camel.comp.*;

import java.io.*;

public class TestAssemblyCode {
    static public void main(String argv[]) throws FileNotFoundException {

        Reader r;
        //r = new StringReader("let x = 1 in print_newline");
        //r = new StringReader("let x = let y = 3 in let z = 4 in let t = z + 1 in 4 + t");
        r = new StringReader("let z = 3 in let x = z in 3 + z");
        //r = new StringReader("let x = 5 in print_int(x)");
        //r = new StringReader("let rec incr x y  = x + y + 1234 in let z = incr 5 (incr 4 5) in print_int z");
        //r = new StringReader("let rec incr x = x + 1 in print_int(incr(2))");
        //r = new StringReader("let a = 5 in let b = 12 in let x = if a <= b then 3 else 5 in print_int(x)");


        MyCompiler c = new MyCompiler(r);

        c.parseCode();
        c.preProcessCode();

        /*
        c.codeGeneration();

        c.outputIR(System.out);


        c.outputAssembly(System.out);*/
        //c.outputAssembly(new PrintStream("ARM/output.s"));
    }
}

