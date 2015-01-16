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

    static {
        IMPORTS.put("print_newline", "min_caml_print_newline");
        IMPORTS.put("print_int", "min_caml_print_int");

    }

    public AssemblyGenerator() {
        //headers for .data and .text sections
        data = new StringBuilder("\t.data");
        text = new StringBuilder();

        // TODO: use register for return value
        data.append("\nret\t: .word @ ugly\n");

        text.append("\n\t.text");
        text.append("\n\t.global _start");
        text.append("\n\n_start:");
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
        genLabel(funDef.name);

        // Prologue
        text.append("\n\t@ prologue");
        text.append("\n\tSUB sp, #4");
        text.append("\n\tSTR lr, [sp]");

        // Body of the function
        for (Instr i : funDef.body) {
            Instr.Type t = i.getType();
            if (t != Instr.Type.LABEL) {
                text.append("\n\n\t@ ").append(i.toString());
            }

            switch (t) {
                case LABEL:
                    genLabel((Label) i);
                    break;
                case CALL:
                    genCall((Call) i);
                    break;
                case ASSIGN:
                    genAssign((Assign) i);
                    break;
                case ADD_I:
                    genAddI((AddI) i);
                    break;
                case SUB_I:
                    genSubI((SubI) i);
                    break;
                case CMP:
                    genCmp((Compare) i);
                    break;
                case BEQ:
                    genBeq((BranchEq) i);
                    break;
                case BLE:
                    genBle((BranchLe) i);
                    break;
                case JUMP:
                    genJump((Jump) i);
                    break;
                case RETURN:
                    genReturn((Ret) i);
                    break;

                default:
                    throw new RuntimeException(
                            "Can't generate assembly for " + i.getType()
                    );
            }

        }

        // Epilogue
        text.append('\n');
        text.append("\n\t@epilogue");
        text.append("\n\tLDR lr, [sp]");
        text.append("\n\tADD sp, #4");
        text.append("\n\tMOV pc, lr");
        text.append('\n');
        text.append('\n');
    }

    private void loadOperand1(Op op1) {
        switch (op1.getType()) {
            case VAR:
                text.append("\n\tLDR r1, =").append(((Var) op1).getName());
                text.append("\n\tLDR r1, [r1]");
                break;
            case CONST:
                text.append("\n\tMOV r1, #").append(((Const) op1).getValueI());
        }
    }

    private void loadOperand2(Op op2) {
        switch (op2.getType()) {
            case VAR:
                text.append("\n\tLDR r2, =").append(((Var) op2).getName());
                text.append("\n\tLDR r2, [r2]");
                break;
            case CONST:
                text.append("\n\tMOV r2, #").append(((Const) op2).getValueI());
        }
    }

    private void genBinaryOp(String mnemonic, Var var, Op op1, Op op2) {
        String varName = var.getName();
        data.append("\n").append(varName).append("\t: .word ");

        loadOperand1(op1);
        loadOperand2(op2);

        text.append("\n\t").append(mnemonic).append(" r3, r1, r2");
        text.append("\n\tLDR r0, =").append(varName);
        text.append("\n\tSTR r3, [r0]");
    }

    private void genLabel(@Nonnull Label i) {
        text.append("\n").append(i.getName()).append(":");
    }

    private void genCall(Call i) {
        String name = i.getName();
        String newName = IMPORTS.get(name);
        if (newName != null) name = newName;

        // EXCEPTION: print_int
        if (name.equals("min_caml_print_int")) {
            Op arg = i.getArgs().get(0);
            if (arg instanceof Const) {
                text.append("\n\tMOV r0, #").append(((Const) arg).getValueI());
            } else if (arg instanceof Var) {
                text.append("\n\tLDR r0, =").append(((Var) arg).getName());
                text.append("\n\tLDR r0, [r0]");
            }
            text.append("\n\tBL ").append(name);

            return;
        }

        for (Op arg : i.getArgs()) {
            text.append("\n\tSUB sp, #4");
            if (arg instanceof Const) {
                text.append("\n\rMOV r0, #").append(((Const) arg).getValueI());
            } else if (arg instanceof Var) {
                text.append("\n\tLDR r0, =").append(((Var) arg).getName());
                text.append("\n\tLDR r0, [r0]");
            }
            text.append("\n\tSTR r0, [sp]");

        }
        text.append("\n\tBL ").append(name);

        int popSize = 4*i.getArgs().size();
        text.append("\n\tADD sp, #").append(Integer.toString(popSize));
    }

    private void genAssign(Assign i) {
        String varName = i.getVar().getName();
        Op op = i.getOp();

        data.append("\n");
        data.append(varName);
        data.append("\t: .word ");

        switch (op.getType()) {
            case VAR:
                text.append("\n\tLDR r1, =").append(((Var) op).getName());
                text.append("\n\tLDR r0, [r1]");
                text.append("\n\tLDR r1, =").append(varName);
                text.append("\n\tSTR r0, [r1]");
                break;

            case CONST:
                if (((Const) op).getConstType().equals("int")) {
                    data.append(((Const) op).getValueI());
                }
                /*else{
                    valuef1 = ((Const)op).getValueF();
                    data.append(valuef1);
                }*/
        }
    }

    private void genAddI(AddI i) {
        genBinaryOp("ADD", i.getVar(), i.getOp1(), i.getOp2());
    }

    private void genSubI(SubI i) {
        genBinaryOp("SUB", i.getVar(), i.getOp1(), i.getOp2());
    }

    private void genCmp(Compare i) {
        Op op1 = i.getOp1(), op2 = i.getOp2();
        text.append("\n\tCMP ").append(op1).append(", ").append(op2);
    }


    private void genBle(BranchLe i) {
        text.append("\n\tBLE ").append(i.getLabel());
    }


    private void genBeq(BranchEq i) {
        text.append("\n\tBEQ ").append(i.getLabel());
    }


    private void genJump(Jump i) {
        text.append("\n\tBAL ").append(i.getLabel());
    }

    private void genReturn(Ret i) {
        Op op = i.getOperand();
        if (op instanceof Const) {
            text.append("\n\tMOV r12, #").append(((Const) op).getValueI());
        } else if (op instanceof Var) {
            text.append("\n\tLDR r12, =").append(((Var) op).getName());
            text.append("\n\tLDR r12, [r12]");
        }
        // TODO: remove this when you get rid of "ret"
        // (really ugly)
        text.append("\n\tLDR r0, =ret");
        text.append("\n\tSTR r12, [r0]");
    }

    public void writeAssembly(PrintStream out) {
        //footer for .text section and printing everything in the output_file
        if (data.length() > 7) {
            out.println(data.toString());
        }
        out.println(text.toString());
    }

}
