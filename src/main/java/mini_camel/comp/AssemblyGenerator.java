package mini_camel.comp;


import mini_camel.ir.*;

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
                varName = args.get(i).getName();
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
                varName = locals.get(i).getName();
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
            Instr.Type t = instr.getType();
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
                    text.append("\nDEFAULT GLAG\n");

                    /*throw new RuntimeException(
                            "Generating assembly for " + instr.getType() +
                                    " instructions not supported yet (" +
                                    instr.toString() + ")."
                    );*/
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

    private String locateVar(StringBuilder sb, String name) {
        Integer offset = varOffsets.get(name);
        if (offset == null) {
            text.append("\nGLAG\n");
            /*throw new RuntimeException(
                    "Sorry, dynamic links & closures not supported yet."
            );*/
        }
        sb.append("[").append(FP_REG).append(", #");
        text.append((int) offset).append("]");
        return sb.toString();
    }

    private void emitAssign(Var v, Op op) {
        emitAssign("r0", op);
        emitAssign(v, "r0");
    }

    private void emitAssign(Var v, String register) {
        if (v.getName().equals(RET_VAR.getName())) {
            text.append("\n\tLDR r0, =ret");
            text.append("\n\tSTR ").append(register).append(", [r0]");
            return;
        }
        text.append("\n\tSTR ").append(register).append(", ");
        locateVar(text, v.getName());
    }

    private void emitAssign(String register, Op op) {
        if (op instanceof Const) {
            if (!((Const) op).getConstType().equals("int")) {
                throw new RuntimeException(
                        "floating point constants not supported yet."
                );
            }

            text.append("\n\tMOV ").append(register);
            text.append(", #").append(((Const) op).getValueI());
            return;
        }
        if (op instanceof Var) {
            String varName = ((Var) op).getName();
            if (varName.equals(RET_VAR.getName())) {
                // fixme
                text.append("\n\tLDR r0, =ret");
                text.append("\n\tLDR ").append(register).append(", [r0]");
                return;
            }
            text.append("\n\tLDR ").append(register).append(", ");
            locateVar(text, varName);
        }
    }

    private void genBinaryOp(String mnemonic, Var var, Op op1, Op op2) {
        emitAssign("r1", op1); // r1 <- op1
        emitAssign("r2", op2); // r2 <- op2

        // r3 <- op1 (?) op2
        text.append("\n\t").append(mnemonic).append(" r3, r1, r2");

        emitAssign(var, "r3"); // var <- r3
    }

    private void genLabel(@Nonnull Label i) {
        text.append("\n").append(i.getName() + ":");
    }

    private void genCall(Call instr) {
        String name = instr.getName();
        String newName = IMPORTS.get(name);
        if (newName != null) name = newName;

        // EXCEPTION: print_int
        if (name.equals("min_caml_print_int")) {
            emitAssign("r0", instr.getArgs().get(0)); // r0 <- only argument
            text.append("\n\tBL ").append(name); // just call

            return;
        }

        List<Op> args = instr.getArgs();
        for (int i = args.size() - 1; i >= 0; i--) {
            text.append("\n\t@ push argument ").append(i);
            text.append("\n\tSUB sp, #4");
            emitAssign("r0", args.get(i));
            text.append("\n\tSTR r0, [sp]");

        }
        text.append("\n\t@ call, then free stack");
        text.append("\n\tBL ").append(name);

        int popSize = 4 * instr.getArgs().size();
        text.append("\n\tADD sp, #").append(Integer.toString(popSize));
        text.append("\n\t@ end call");
    }

    private void genAssign(Assign i) {
        emitAssign(i.getVar(), i.getOp()); // r0 <- op
    }

    private void genAddI(AddI i) {
        genBinaryOp("ADD", i.getVar(), i.getOp1(), i.getOp2());
    }

    private void genSubI(SubI i) {
        genBinaryOp("SUB", i.getVar(), i.getOp1(), i.getOp2());
    }

    private void genCmp(Compare i) {
        emitAssign("r1", i.getOp1()); // r1 <- op1
        emitAssign("r2", i.getOp2()); // r2 <- op2

        text.append("\n\tCMP r1, r2");
    }


    private void genBle(BranchLe i) {
        text.append("\n\tBLE ").append(i.getLabel().getName());
    }


    private void genBeq(BranchEq i) {
        text.append("\n\tBEQ ").append(i.getLabel().getName());
    }


    private void genJump(Jump i) {
        text.append("\n\tBAL ").append(i.getLabel().getName());
    }

    private void genReturn(Ret i) {
        emitAssign("r11", i.getOperand());
        emitAssign(RET_VAR, "r11"); // fixme
    }

    public void writeAssembly(PrintStream out) {
        //footer for .text section and printing everything in the output_file
        if (data.length() > 7) {
            out.println(data.toString());
        }
        out.println(text.toString());
    }

}
