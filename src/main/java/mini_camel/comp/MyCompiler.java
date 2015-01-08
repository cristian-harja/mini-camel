package mini_camel.comp;

import mini_camel.ast.*;

import java.io.*;

public class MyCompiler {
    public AstExp root;

    public MyCompiler(AstExp result){
        this.root = result;
    }


    //type inference to closure conversion
    public void preprocessCode(){

    }

    // virtual code generation, immediate optimisation and register allocation
    // build 3-adress code ??
    public void codeGeneration(){


    }

    //from 3-adress code to Assembly code
    public void generateAssembly(String output_file) throws IOException {
        PrintWriter file_out;
        file_out = new PrintWriter(new BufferedWriter(new FileWriter(output_file)));

        //headers for .data and .text sections
        StringBuilder data = new StringBuilder();
        data.append("\t.data\n");
        StringBuilder text = new StringBuilder();
        text.append("\n\t.text\n\t.global _start\n_start:\n");


        AssemblyVisitor v = new AssemblyVisitor(data, text);
        root.accept(v);


        System.out.println(data.toString() + "\n" + text.toString());


        //footer for .text section and printing everything in the output_file
        text.append("\tbl min_caml_exit\n\n");
        if(data.length() > 7) {
            file_out.print(data.toString());
        }
        file_out.print(text.toString());
        file_out.close();

    }
}