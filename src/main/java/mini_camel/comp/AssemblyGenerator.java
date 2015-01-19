package mini_camel.comp;


import mini_camel.ir.*;
import mini_camel.ir.ConstInt;
import mini_camel.ir.Operand;
import mini_camel.ir.Var;

import javax.annotation.Nonnull;
import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AssemblyGenerator {
    StringBuilder data;
    StringBuilder text;

    boolean[] registers;

    private static final Map<String, String> IMPORTS = new LinkedHashMap<>();

    private Map<String, Integer> varOffsets;

    private static final String FP_REG = "r12";
    private static final Var RET_VAR = new Var("ret");

    static {
        IMPORTS.put("print_newline", "min_caml_print_newline");
        IMPORTS.put("print_int", "min_caml_print_int");

    }

    public AssemblyGenerator() {
        //headers for .data and .text sections
        data = new StringBuilder("\t.data");
        text = new StringBuilder();

        // TODO: use register for return value
        data.append("\nret\t: .word @ ugly");

        text.append("\n\t.text");
        text.append("\n\t.global _start");
        text.append("\n_start:");
        text.append("\n\tBL _main");
        text.append("\n\tBL min_caml_exit\n");

        registers = new boolean[13];
    }

    public void generateAssembly(List<FunDef> functions) {
        for (FunDef fd : functions) {
            generateAssembly(fd);
        }
    }

    private void generateAssembly(FunDef funDef) {
        int i, n;
        List<Var> args = funDef.args;
        List<Var> locals = funDef.locals;
        String varName;
        int varOffset;

        genLabel(funDef.name);

        varOffsets = new LinkedHashMap<>();

        // Function arguments
        n = args.size();
        if (n > 0) {
            text.append("\n\t@ arguments");
            for (i = 0; i < n; i++) {
                varName = args.get(i).name;
                varOffset = 4 * i + 8;
                text.append("\n\t@   ").append(varName);
                text.append(" -> ").append(FP_REG);
                text.append("+").append(varOffset);

                varOffsets.put(varName, varOffset);
            }
            text.append('\n');
        }

        // Local variables
        n = locals.size();
        if (n > 0) {
            text.append("\n\t@ local variables");
            for (i = 0, n = locals.size(); i < n; i++) {
                varName = locals.get(i).name;
                varOffset = 4 * i + 4; // negative offset
                text.append("\n\t@   ").append(varName);
                text.append(" -> ").append(FP_REG);
                text.append("-").append(varOffset);

                varOffsets.put(varName, -varOffset);
            }
            text.append('\n');
        }

        // Prologue
        text.append("\n\t@ prologue");
        text.append("\n\tSUB sp, #4");
        text.append("\n\tSTR lr, [sp]");

        // Push frame pointer
        text.append("\n\tSUB sp, #4");
        text.append("\n\tSTR ").append(FP_REG).append(", [sp]");
        text.append("\n\tMOV ").append(FP_REG).append(", sp");

        // Allocate local vars
        text.append("\n\tSUB sp, sp, #").append(4 * locals.size());

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
                case CMP:
                    genCmp((Compare) instr);
                    break;
                case BEQ:
                    genBeq((BranchEq) instr);
                    break;
                case BLE:
                    genBle((BranchLe) instr);
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
        text.append("\n\tLDR lr, [sp]");
        text.append("\n\tADD sp, #4");
        text.append("\n\tMOV pc, lr");
        text.append('\n');
        text.append('\n');
    }

    private String locateVar(
            @Nonnull StringBuilder out_sb,
            @Nonnull String name
    ) {
        Integer offset = varOffsets.get(name);
        if (offset == null) {
            throw new RuntimeException(
                    "Sorry, dynamic links & closures not supported yet."
            );
        }
        out_sb.append("[").append(FP_REG).append(", #");
        text.append(offset).append("]");
        return out_sb.toString();
    }

    private void emitAssign(@Nonnull Var v, @Nonnull Operand op) {
        emitAssign("r0", op);
        emitAssign(v, "r0");
    }

    private void emitAssign(@Nonnull Var v, @Nonnull String register) {
        if (v.name.equals(RET_VAR.name)) {
            text.append("\n\tLDR r0, =ret");
            text.append("\n\tSTR ").append(register).append(", [r0]");
            return;
        }
        text.append("\n\tSTR ").append(register).append(", ");
        locateVar(text, v.name);
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
                String varName = ((Var) op).name;
                if (varName.equals(RET_VAR.name)) {
                    // fixme
                    text.append("\n\tLDR r0, =ret");
                    text.append("\n\tLDR ").append(register).append(", [r0]");
                    return;
                }
                text.append("\n\tLDR ").append(register).append(", ");
                locateVar(text, varName);
        }
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
        text.append("\n\t").append(mnemonic).append(" r3, r1, r2");

        emitAssign(var, "r3"); // var <- r3
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

            return;
        }

        List<Operand> args = instr.args;
        for (int i = args.size() - 1; i >= 0; i--) {
            text.append("\n\t@ push argument ").append(i);
            text.append("\n\tSUB sp, #4");
            emitAssign("r0", args.get(i));
            text.append("\n\tSTR r0, [sp]");

        }
        text.append("\n\t@ call, then free stack");
        text.append("\n\tBL ").append(name);

        int popSize = 4 * instr.args.size();
        text.append("\n\tADD sp, #").append(Integer.toString(popSize));
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

    private void genCmp(@Nonnull Compare i) {
        emitAssign("r1", i.op1); // r1 <- op1
        emitAssign("r2", i.op2); // r2 <- op2

        text.append("\n\tCMP r1, r2");
    }


    private void genBle(@Nonnull BranchLe i) {
        text.append("\n\tBLE ").append(i.label.name);
    }


    private void genBeq(@Nonnull BranchEq i) {
        text.append("\n\tBEQ ").append(i.label.name);
    }


    private void genJump(@Nonnull Jump i) {
        text.append("\n\tBAL ").append(i.label.name);
    }

    private void genReturn(@Nonnull Ret i) {
        if (i.op == null) return;
        emitAssign("r11", i.op);
        emitAssign(RET_VAR, "r11"); // fixme
    }

    public void writeAssembly(@Nonnull PrintStream out) {
        //footer for .text section and printing everything in the output_file
        if (data.length() > 7) {
            out.println(data.toString());
        }
        out.println(text.toString());
    }

}
