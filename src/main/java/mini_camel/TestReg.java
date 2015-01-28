package mini_camel;

import mini_camel.comp.*;

import java.io.*;

public class TestReg {
    static public void main(String argv[]) throws FileNotFoundException {

        Reader r;
        //r = new StringReader("let x = 1 in let rec incr x = x + 1 in let z = incr 3 in print_int(z)");
        r = new StringReader("let rec incr x = x + 1 in let y = 3 in let z = 23 in let x = incr 1 in let rec add x y  = x + y in let a = incr x in let b = add a z in print_int(b);print_int(145 + x)");

        MyCompiler c = new MyCompiler(r);

        c.parseCode();
        c.preProcessCode();

        //c.outputTransformedAst(System.out);

        c.codeGeneration();


        c.outputIR(System.out);


        c.outputAssembly(new PrintStream("ARM/reg.s"));

        c.printErrors(System.err);
    }
}

