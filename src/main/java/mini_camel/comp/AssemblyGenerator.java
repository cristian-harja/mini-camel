package mini_camel.comp;


import mini_camel.ir.Function;
import mini_camel.ir.instr.*;
import mini_camel.ir.op.*;

import javax.annotation.Nonnull;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AssemblyGenerator {
    StringBuilder data;
    StringBuilder text;


    private static final Map<String, String> IMPORTS = new LinkedHashMap<>();

    private Map<String, Integer> argOffsets;
    private String[] registers;
    private ArrayList<String> memory;
    private int cursor;

    private String RET_KEYWORD = "ret"; // return value in r11
    private int heap_size = 2048; // To have 2048 cells of 4 bytes

    static {
        IMPORTS.put("print_newline", "min_caml_print_newline");
        IMPORTS.put("print_int", "min_caml_print_int");

    }


    public AssemblyGenerator() {
        //headers for .data and .text sections
        data = new StringBuilder("\t.data\n\t.balign 4");
        data.append("\nlimit : .word 0");    // to store the limit of the heap (head + heap_size*4)
        data.append("\nhead : .word 0");     // to have the head/top of the heap
        data.append("\nheap : .skip ").append(heap_size * 4); // to have heap_size cells of 4 bytes
        data.append("\nerror_message : .asciz\t\"Not enough space in the heap\\n\"");

        text = new StringBuilder("\n\t.text");
        text.append("\n\t.global _start");
        text.append("\n_start:");
        text.append("\n\tBL _main");
        //text.append("\n\tBL min_caml_print_newline"); // for more clarity we print a new line before terminating correctly
        text.append("\n\tBL min_caml_exit\n");



    }

    public void generateAssembly(List<Function> functions) {
        for (Function fd : functions) {
            generateAssembly(fd);
        }
    }

    private void generateAssembly(Function funDef) {
        int i, n;
        List<Var> args = funDef.args;
        String varName;
        int varOffset;

        genLabel(funDef.name);

        argOffsets = new LinkedHashMap<>();
        // initialization for the register allocator
        registers = new String[11];
        memory = new ArrayList<>();
        cursor = 3;


        // initialization of the heap management
        if(funDef.name.name.equals("_main")) {
            //push lr in stack
            text.append("\n\t@ prologue");
            text.append("\n\tSUB sp, #4");
            text.append("\n\tSTR lr, [sp]");

            //initialize the head of the heap in memory
            text.append("\n\tLDR r0, =heap");
            text.append("\n\tLDR r1, =head");
            text.append("\n\tSTR r0, [r1]");

            //initialize the limit of the heap in memory
            text.append("\n\tLDR r2, =limit");
            text.append("\n\tLDR r3, =").append(heap_size * 4);
            text.append("\n\tADD r3, r0, r3");
            text.append("\n\tSTR r3, [r2]");
        }
        else {
            // Function arguments
            n = args.size();
            if (n > 0) {
                text.append("\n\t@ arguments");
                for (i = 0; i < n; i++) {
                    varName = args.get(i).name;
                    varOffset = 4 * i + 40;
                    text.append("\n\t@   ").append(varName);
                    text.append(" -> r12");
                    text.append("+").append(varOffset);

                    argOffsets.put(varName, varOffset);
                }
                text.append('\n');
            }

            // Prologue
            text.append("\n\t@ prologue");
            text.append("\n\tSUB sp, #4");
            text.append("\n\tSTR lr, [sp]");
            text.append("\n\tstmfd	sp!, {r3 - r10}");


            // Push frame pointer
            text.append("\n\tSUB sp, #4");
            text.append("\n\tSTR r12, [sp]");
            text.append("\n\tMOV r12, sp");
        }



        // Body of the function
        for (Instr instr : funDef.body) {
            Instr.Type t = instr.getInstrType();
            if (t != Instr.Type.LABEL) {
                text.append("\n\n\t@ ").append(instr.toString());
            }


            switch (t) {
                case LABEL:
                    genLabel((Label) instr);
                    break;
                case CALL:
                    genCall((DirApply) instr);
                    break;
                case CLS_MAKE:
                    genMake((ClsMake)instr);
                    break;
                case CLS_APPLY:
                    genApply((ClsApply)instr);
                    break;
                case ASSIGN:
                    genAssign((Assign) instr);
                    break;
                case ADD_I:
                    genAddI((AddI) instr);
                    break;
                case SUB_I:
                    genSubI((SubI) instr);
                    break;
                case BRANCH:
                    genBranch((Branch) instr);
                    break;
                case JUMP:
                    genJump((Jump) instr);
                    break;
                case RETURN:
                    genReturn((Ret) instr);
                    break;

                default:
                    throw new RuntimeException(
                            "Generating assembly for " +
                                    instr.getInstrType() +
                                    " instructions not supported yet (" +
                                    instr.toString() + ")."
                    );
            }

        }

        // Epilogue
        text.append("\n\t@epilogue");
        if(!funDef.name.name.equals("_main")){
            text.append("\n\tMOV sp, r12");
            text.append("\n\tLDR r12, [sp]");
            text.append("\n\tADD sp, #4");
            text.append("\n\tldmfd	sp!, {r3 - r10}");
        }
        text.append("\n\tLDR lr, [sp]");
        text.append("\n\tADD sp, #4");
        text.append("\n\tMOV pc, lr\n\n");
    }



    private Integer locateVar(
            @Nonnull String name
    ) {
        Integer offset = argOffsets.get(name);
        /*if (offset == null) {
            throw new RuntimeException(
                    "Sorry, dynamic links & closures not supported yet."
            );
        }*/
        return offset;
    }


    private int getnewReg(){

        for(int i = 3; i<=10; i++){
            if(registers[i] == null){
                return i;
            }
        }

        String tmp = registers[cursor];
        data.append("\n").append(tmp).append(" : .word ");
        text.append("\n\tLDR r0, =").append(tmp);
        text.append("\n\tSTR r").append(cursor).append(", [r0]");
        memory.add(tmp);

        int ret = cursor;
        cursor = cursor > 9 ? 3 : ++cursor;

        return ret;
    }

    private int getReg(String var){
        int i;

        if(var.equals(RET_KEYWORD)){
            return 11;
        }

        for (i = 3; i<11; i++){
            if(registers[i] != null && registers[i].equals(var)){
                return i;
            }
        }

        for(i = 0; i<memory.size(); i++){
            if(memory.get(i).equals(var)){
                memory.remove(i);
                int r = getnewReg();
                text.append("\n\tLDR r0, =").append(var);
                text.append("\n\tLDR r").append(r).append(", [r0]");

                return r;
            }
        }

        return -1;
    }





    private void emitAssign(@Nonnull Var v, @Nonnull Operand op) {
        String var = v.name;
        int rd;
        switch (op.getOperandType()) {
            case CONST_INT:
                rd = getnewReg();
                int val = ((ConstInt)op).value;
                registers[rd] = var;
                text.append("\n\tLDR r").append(rd).append(", =").append(val);
                return;


            case VAR :
                if((rd = getReg(var)) != -1){
                    emitAssign("r"+rd, op);
                }

                Integer offset = locateVar(var);
                if(offset != null) {
                    emitAssign("r0", op);
                    text.append("\n\tSTR r0, [r12, #").append(offset).append("]");
                    return;
                }

                // here the var is not encountered yet
                rd = getnewReg();
                registers[rd] = var;
                emitAssign("r"+rd,op);
                return;


        }

    }

    private void emitAssign(@Nonnull Var v, @Nonnull String register) {
        int rd;
        String var = v.name;

        if((rd = getReg(var)) != -1){
            text.append("\n\tMOV r").append(rd).append(", ").append(register);
            return;
        }

        Integer offset = locateVar(var);
        if(offset != null) {
            text.append("\n\tSTR ").append(register).append(", [r12, #").append(offset).append("]");
            return;
        }

        // here the var is not encountered yet
        rd = getnewReg();
        registers[rd] = var;
        text.append("\n\tMOV r").append(rd).append(", ").append(register);
        return;

    }

    private void emitAssign(@Nonnull String register, @Nonnull Operand op) {
        switch (op.getOperandType()) {
            case CONST_INT:
                text.append("\n\tLDR ").append(register);
                text.append(", =").append(((ConstInt) op).value);
                return;

            case CONST_FLOAT:
                // TODO: floats
                throw new RuntimeException(
                        "floating point constants not supported yet."
                );

            case LABEL:
                text.append("\n\tLDR ").append(register);
                text.append(", =").append(((Label)op).name);
                return;

            case VAR:
                int rd;
                String var = ((Var)op).name;

                if((rd = getReg(var)) != -1){
                    text.append("\n\tMOV ").append(register).append(", r").append(rd);
                    return;
                }

                int offset = locateVar(var);
                text.append("\n\tLDR ").append(register).append(", [r12, #").append(offset).append("]");

        }
    }

    private void emitAssign(String destRegister, String srcRegister) {
        text.append("\n\tMOV ").append(destRegister).append(", ").append(srcRegister);
    }

    private void genBinaryOp(
            @Nonnull String mnemonic,
            @Nonnull Var var,
            @Nonnull Operand op1,
            @Nonnull Operand op2
    ) {
        emitAssign("r1", op1); // r1 <- op1
        emitAssign("r2", op2); // r2 <- op2

        // r3 <- op1 (?) op2
        text.append("\n\t").append(mnemonic).append(" r0, r1, r2");

        emitAssign(var, "r0"); // var <- r0
    }

    private void genLabel(@Nonnull Label i) {
        text.append("\n").append(i.name).append(":");
    }

    private void genCall(@Nonnull DirApply instr) {
        String name = instr.name;
        String newName = IMPORTS.get(name);
        if (newName != null) name = newName;


        // EXCEPTION: print_int
        if (name.equals("min_caml_print_int")) {
            emitAssign("r0", instr.args.get(0)); // r0 <- only argument
            text.append("\n\tBL ").append(name); // just call

            if(instr.ret != null){
                emitAssign(instr.ret, "r11");
            }

            return;
        }

        if (name.equals("min_caml_print_newline")) {
            text.append("\n\tBL min_caml_print_newline");

            if(instr.ret != null){
                emitAssign(instr.ret, "r11");
            }
            return;
        }


        List<Operand> args = instr.args;
        for (int i = args.size() - 1; i >= 0; i--) {
            text.append("\n\t@ push argument ").append(i);
            text.append("\n\tSUB sp, #4");
            emitAssign("r0", args.get(i));
            text.append("\n\tSTR r0, [sp]");
        }

        text.append("\n\t@ call, free stack and set the return value");
        text.append("\n\tBL ").append(name);

        int popSize = 4 * instr.args.size();
        text.append("\n\tADD sp, #").append(Integer.toString(popSize));

        if(instr.ret != null){
            emitAssign(instr.ret, "r11");
        }

        text.append("\n\t@ end call");
    }

    private void genMake(@Nonnull ClsMake instr) {
        List<Operand> args = instr.free_args;
        int var = getnewReg();
        String rd = "r" + var;
        registers[var] = instr.v.name;

        text.append("\n@starting make cls");
        text.append("\n\tLDR r0, =").append(args.size() + 2);
        text.append("\n\tBL malloc");
        text.append("\n\tMOV ").append(rd).append(", r0");

        text.append("\n\tLDR r2, =").append(Integer.toString(args.size()));
        text.append("\n\tSTR r2, [").append(rd).append("]");

        text.append("\n\tLDR r2, =").append(instr.fun.name);
        text.append("\n\tSTR r2, [").append(rd).append(", #4]");


        for(int i = 0; i<args.size(); i++){
            emitAssign("r2", args.get(i));
            text.append("\n\tSTR r2, [").append(rd).append(", #").append(Integer.toString(i*4 + 8)).append("]");
        }

        text.append("\n@ending make cls");
    }

    private void genApply(@Nonnull ClsApply instr) {
        int cls = getReg(instr.cls.name);
        String rc = "r" + cls;
        int popSize = 0;



        text.append("\n\tLDR r2, [").append(rc).append("]");
        text.append("\n\tSUB r2, r2, #1");  // r2 <- size - 1
        text.append("\n\tADD r1, ").append(rc).append(", #8]");
        text.append("\n\tADD r1, r1, r2, LSL #2"); // r1 <- address of the last arg (rc + r2*4 + 8)
        text.append("\nfor : ");
        text.append("\n\tCMP r2, #0");
        text.append("\n\tBLT end_for");
        text.append("\n\tLDR r0, [r1]");
        text.append("\n\tSUB sp, sp, #4");
        text.append("\n\tSTR r0, [sp]");
        text.append("\n\tSUB r2, r2, #1");
        text.append("\n\tBAL for");

        text.append("\nend_for : \n");



        List<Operand> args = instr.args;
        for (int i = args.size() - 1; i >= 0; i--) {
            text.append("\n\t@ push argument ").append(i);
            text.append("\n\tSUB sp, #4");
            emitAssign("r0", args.get(i));
            text.append("\n\tSTR r0, [sp]");
            popSize += 4;  // TODO ADD size of free_args
        }

        text.append("\n\t@ call, free stack and set the return value");
        text.append("\n\tBX [").append(rc).append(", #4"); //TODO TEST BX [rc, #4]

        text.append("\n\tADD sp, #").append(Integer.toString(popSize));

        if(instr.ret != null){
            emitAssign(instr.ret, "r11");
        }
        text.append("\n\t@ end call");

    }



    private void genAssign(@Nonnull Assign i) {
        emitAssign(i.var, i.op); // r0 <- op
    }

    private void genAddI(@Nonnull AddI i) {
        genBinaryOp("ADD", i.var, i.op1, i.op2);
    }

    private void genSubI(@Nonnull SubI i) {
        genBinaryOp("SUB", i.var, i.op1, i.op2);
    }

    private void genBranch(@Nonnull Branch i) {
        emitAssign("r1", i.op1); // r1 <- op1
        emitAssign("r2", i.op2); // r2 <- op2

        String mnemonic = i.lessOrEqual ? "\n\tBLE " : "\n\tBEQ ";

        text.append("\n\tCMP r1, r2");
        text.append(mnemonic).append(i.ifTrue.name);
        text.append("\n\tBAL ").append(i.ifFalse.name);
    }


    private void genJump(@Nonnull Jump i) {
        text.append("\n\tBAL ").append(i.label.name);
    }

    private void genReturn(@Nonnull Ret i) {
        if (i.op == null) return;
        if (i.op instanceof Var) {
            if (((Var) i.op).name.equals(RET_KEYWORD)) {
                return;
            }
        }
        emitAssign("r11", i.op);
    }

    public void writeAssembly(@Nonnull PrintStream out) {
        // function check_heap :
        text.append("\ncheck_heap :");
        text.append("\n\tLDR r0, =limit");
        text.append("\n\tLDR r0, [r0]");
        text.append("\n\tCMP r1, r0");
        text.append("\n\tBGT error_in_heap");
        text.append("\n\tMOV PC, LR");
        text.append("\nerror_in_heap :");
        text.append("\n\tLDR r0, =error_message");
        text.append("\n\tBL min_caml_print_string");
        text.append("\n\tBL min_caml_exit\n");

        // function malloc (r0) :
        text.append("\nmalloc :");
        text.append("\n\tLDR r1, =head");
        text.append("\n\tLDR r2, [r1]");
        text.append("\n\tADD r1, r2, r0, LSL #2");
        text.append("\n\tBL check_heap");
        text.append("\n\tLDR r0, =head");
        text.append("\n\tSTR r1, [r0]");
        text.append("\n\tMOV r0, r1\n");

        //prints everything contained in data and text in the output_file
        out.println(data.toString());
        out.println(text.toString());
    }

}
