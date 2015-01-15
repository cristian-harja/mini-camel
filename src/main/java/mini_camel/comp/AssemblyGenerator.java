package mini_camel.comp;


import java.io.PrintStream;

public class AssemblyGenerator{
    StringBuilder data;
    StringBuilder text;
    //StringBuilder fun;

    PrintStream file_out;
    boolean[] registers;


    public AssemblyGenerator(PrintStream out){
        //headers for .data and .text sections
        StringBuilder data = new StringBuilder();
        data.append("\t.data\n");
        StringBuilder text = new StringBuilder();
        text.append("\n\t.text\n\t.global _start\n_start:\n");

        // StringBuilder section containing the declared functions
        //StringBuilder fun = new StringBuilder();

        registers = new boolean[13];
        file_out = out;

    }



    public void generateAssembly(){










    }

    public void writeAssembly(){
        //footer for .text section and printing everything in the output_file
        text.append("\tbl min_caml_exit\n\n\n");
        if (data.length() > 7) {
            file_out.print(data.toString());
        }
        file_out.print(text.toString());
        //file_out.print(fun.toString());
        file_out.close();
    }

}
