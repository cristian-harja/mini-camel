package mini_camel.comp;


import mini_camel.ir.Function;
import mini_camel.ir.instr.Label;
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

    private static final String FP_REG = "r12";
    private String RET_KEYWORD = "ret"; // return value in r11

    static {
        IMPORTS.put("print_newline", "min_caml_print_newline");
        IMPORTS.put("print_int", "min_caml_print_int");

    }



    public AssemblyGenerator() {
        //headers for .data and .text sections
        data = new StringBuilder("\t.data");
        text = new StringBuilder("\n\t.text");

        text.append("\n\t.global _start");
        text.append("\n_start:");
        text.append("\n\tBL _main");
        text.append("\n\tBL min_caml_print_newline");
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
        List<Var> locals = funDef.locals;
        String varName;
        int varOffset;

        genLabel(funDef.name);

        argOffsets = new LinkedHashMap<>();
        registers = new String[11];
        memory = new ArrayList<String>();
        cursor = 3;

        // Function arguments
        n = args.size();
        if (n > 0) {
            text.append("\n\t@ arguments");
            for (i = 0; i < n; i++) {
                varName = args.get(i).name;
                varOffset = 4 * i + 40;
                text.append("\n\t@   ").append(varName);
                text.append(" -> ").append(FP_REG);
                text.append("+").append(varOffset);

                argOffsets.put(varName, varOffset);
            }
            text.append('\n');
        }

        // Local variables
        /*n = locals.size();
        if (n > 0) {
            text.append("\n\t@ local variables");
            for (i = 0, n = locals.size(); i < n; i++) {
                varName = locals.get(i).name;
                varOffset = 4 * i + 4; // negative offset
                text.append("\n\t@   ").append(varName);
                text.append(" -> ").append(FP_REG);
                text.append("-").append(varOffset);

                argOffsets.put(varName, -varOffset);
            }
            text.append('\n');
        }*/

        // Prologue
        text.append("\n\t@ prologue");
        text.append("\n\tSUB sp, #4");
        text.append("\n\tSTR lr, [sp]");
        text.append("\n\tstmfd	sp!, {r3 - r10}");


        // Push frame pointer
        text.append("\n\tSUB sp, #4");
        text.append("\n\tSTR ").append(FP_REG).append(", [sp]");
        text.append("\n\tMOV ").append(FP_REG).append(", sp");

        // Allocate local vars
        //text.append("\n\tSUB sp, sp, #").append(4 * locals.size());

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
                    genCall((Call) instr);
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
        text.append('\n');
        text.append("\n\t@epilogue");
        text.append("\n\tMOV sp, ").append(FP_REG);
        text.append("\n\tLDR ").append(FP_REG).append(", [sp]");
        text.append("\n\tADD sp, #4");
        text.append("\n\tldmfd	sp!, {r3 - r10}");
        text.append("\n\tLDR lr, [sp]");
        text.append("\n\tADD sp, #4");
        text.append("\n\tMOV pc, lr");
        text.append('\n');
        text.append('\n');
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
                    text.append("\n\tSTR r0, [").append(FP_REG).append(", #").append(offset).append("]");
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
            text.append("\n\tSTR ").append(register).append(", [").append(FP_REG).append(", #").append(offset).append("]");
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
                text.append("\n\tLDR ").append(register).append(", [").append(FP_REG).append(", #").append(offset).append("]");

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

    private void genCall(@Nonnull Call instr) {
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
        //footer for .text section and printing everything in the output_file
        if (data.length() > 7) {
            out.println(data.toString());
        }
        out.println(text.toString());
    }

}
