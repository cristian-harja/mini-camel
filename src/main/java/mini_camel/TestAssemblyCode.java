package mini_camel;

import mini_camel.comp.MyCompiler;
import mini_camel.ir.CodeGenerator;
import mini_camel.ir.FunDef;

import java.io.*;
import java.util.Queue;

public class TestAssemblyCode {
    static public void main(String argv[]) {
        try {
            MyCompiler c = new MyCompiler(new StringReader("print_int (1+2)"));
            c.parseCode();
            c.codeGeneration();

            Queue<FunDef> funDefs = CodeGenerator.generateIR(c.getParsedAst(), "_start");
            funDefs.clear();

            //c.outputAssembly(new PrintStream("ARM/output.s"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

