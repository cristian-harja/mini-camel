package mini_camel;

import mini_camel.comp.*;

import java.io.*;

public class TestAssemblyCode {
    static public void main(String argv[]) throws FileNotFoundException {

        Reader r = null;

        //Inlining test
        //r = new StringReader("let rec f x = f x in f");
        //r = new StringReader("let rec fact x = if (x <= 1.0) then 1.0 else (x *. (fact (x -. 1.0))) in fact 7.0");
        //r = new StringReader("let rec f x = x + 1 in let rec g z a = z + a in f(g 1 2)");
        //r = new StringReader("let rec f x = x + 1 in f 1");
        r = new StringReader("let rec f x y z t a = x + y - z + a - t in f 1 2 3 4 5");
        //r = new StringReader("let x = 1 in let rec f y = x in f 1");
        //r = new StringReader("let rec f x = let rec g z = g z in 3 in f 2");
        //Tester si printf function marche : ca marche ;) cf : inlining test

        //Elimination test
        //r = new StringReader("let x = 1 in let y = 2 in let z = 3 in let t = 2 in z + e");
        //r = new StringReader("let x = 1 in print_newline()");
        //r = new StringReader("let x = let y = 3 in let z = 4 in let t = z + 1 in 4 + t");
        //r = new StringReader("let x = let z = 1 in z + 2 in x + 1");
        //r = new StringReader("let rec f x y = x *. y in 5.");

        //AlphaConversion test
        //r = new StringReader("let x = 3 in let x = 2 in 3");

        //BetaReduction test

        //Constant folding
        // r = new StringReader("let x = 5 in print_int(x)");


        // r = new StringReader("let z = 3 in let x = z in x + z");
        //r = new StringReader("let rec f x = let y = 3 in x + y in f 2");

        //r = new StringReader("let rec incr x = x + 1 in print_int(incr(2))");
        //r = new StringReader("let a = 5 in let b = 12 in let x = if a <= b then 3 else 5 in print_int(x)");


        MyCompiler c = new MyCompiler(r);

        c.parseCode();
        c.preProcessCode();


        //c.codeGeneration();

        //c.outputIR(System.out);

        /*
        c.outputAssembly(System.out);*/
        //c.outputAssembly(new PrintStream("ARM/output.s"));
    }
}

