package mini_camel.comp;


import mini_camel.ir.*;

import java.io.PrintStream;
import java.util.List;

public class AssemblyGenerator{
    StringBuilder data;
    StringBuilder text;
    //StringBuilder fun;

    PrintStream file_out;
    boolean[] registers;


    public AssemblyGenerator(PrintStream out){
        //headers for .data and .text sections
        data = new StringBuilder();
        data.append("\t.data");
        text = new StringBuilder();
        text.append("\n\t.text\n\t.global _start\n_start:");

        // StringBuilder section containing the declared functions
        //fun = new StringBuilder();

        registers = new boolean[13];
        file_out = out;

    }


    /*private int getReg(){
        for(int i = 0; i<13; i++){
            if(!registers[i]){
                return i;
            }
        }


    }*/


    public void generateAssembly(List<Instr> list){
        String varl, varr, var1, var2;
        Op op, op1, op2;
        int valuei1, valuei2;
        //float valuef1, valuef2;


        for(Instr i : list){
            switch(i.getType()){
                case ASSIGN :
                    varl = ((Assign)i).getVar().getName();
                    data.append("\n");
                    data.append(varl);
                    data.append(" : .word ");

                    op = ((Assign)i).getOp();
                    switch(op.getType()){
                        case VAR :
                            varr = ((Var)op).getName();

                            text.append("\n\tMOV r1, =");
                            text.append(varr);
                            text.append("\n\tLDR r0, [r1]");
                            text.append("\n\tMOV r1, =");
                            text.append(varl);
                            text.append("\n\tSTR r0, [r1]");

                            break;
                        case CONST:
                            if(((Const)op).getConstType().equals("int")){
                                valuei1 = ((Const)op).getValueI();
                                data.append(valuei1);
                            }
                            /*else{
                                valuef1 = ((Const)op).getValueF();
                                data.append(valuef1);
                            }*/
                    }
                break;




                case CALL :
                    if(((Call)i).getName().equals("print_newline")){
                        text.append("\n\tbl min_caml_print_newline");
                    }
                    else if(((Call)i).getName().equals("print_int")){
                        // NEED TO HAVE ARGS PUSHED BEFORE
                        text.append("\n\tbl min_caml_print_int");
                    }
                break;




                case ADD_I :
                    varl = ((Add)i).getVar().getName();
                    op1 = ((Add)i).getOp1();
                    op2 = ((Add)i).getOp2();

                    data.append("\n");
                    data.append(varl);
                    data.append(" : .word ");

                    switch(op1.getType()){
                        case VAR :
                            var1 = ((Var)op1).getName();
                            text.append("\n\tMOV r1, =");
                            text.append(var1);
                            text.append("\n\tLDR r1, [r1]");
                            break;
                        case CONST:
                            valuei1 = ((Const)op1).getValueI();
                            text.append("\n\tMOV r1, #");
                            text.append(valuei1);
                    }

                    switch(op2.getType()){
                        case VAR :
                            var2 = ((Var)op2).getName();
                            text.append("\n\tMOV r2, =");
                            text.append(var2);
                            text.append("\n\tLDR r2, [r2]");
                            break;
                        case CONST:
                            valuei2 = ((Const)op2).getValueI();
                            text.append("\n\tMOV r2, #");
                            text.append(valuei2);
                    }

                    text.append("\n\tADD r3, r1, r2");
                    text.append("\n\tMOV r0, =");
                    text.append(varl);
                    text.append("\n\tSTR r3, [r0]");
                break;

                case SUB_I :
                    varl = ((Add)i).getVar().getName();
                    op1 = ((Add)i).getOp1();
                    op2 = ((Add)i).getOp2();

                    data.append("\n");
                    data.append(varl);
                    data.append(" : .word ");

                    switch(op1.getType()){
                        case VAR :
                            var1 = ((Var)op1).getName();
                            text.append("\n\tMOV r1, =");
                            text.append(var1);
                            text.append("\n\tLDR r1, [r1]");
                            break;
                        case CONST:
                            valuei1 = ((Const)op1).getValueI();
                            text.append("\n\tMOV r1, #");
                            text.append(valuei1);
                    }

                    switch(op2.getType()){
                        case VAR :
                            var2 = ((Var)op2).getName();
                            text.append("\n\tMOV r2, =");
                            text.append(var2);
                            text.append("\n\tLDR r2, [r2]");
                            break;
                        case CONST:
                            valuei2 = ((Const)op2).getValueI();
                            text.append("\n\tMOV r2, #");
                            text.append(valuei2);
                    }

                    text.append("\n\tSUB r3, r1, r2");
                    text.append("\n\tMOV r0, =");
                    text.append(varl);
                    text.append("\n\tSTR r3, [r0]");
                break;





                default : break;
            }



        }

    }

    public void writeAssembly(){
        //footer for .text section and printing everything in the output_file
        text.append("\n\tbl min_caml_exit\n\n");
        if (data.length() > 7) {
            file_out.print(data.toString());
        }
        file_out.print(text.toString());
        //file_out.print(fun.toString());
        file_out.close();
    }

}
