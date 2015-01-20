package mini_camel.ir.instr;

import mini_camel.ir.op.Operand;
import mini_camel.ir.op.Var;

import javax.annotation.Nonnull;

public final class DivF implements Instr {
    public final Var var;
    public final Operand op1, op2;

    public DivF(
            @Nonnull Var v,
            @Nonnull Operand operand1,
            @Nonnull Operand operand2
    ) {
        var = v;
        op1 = operand1;
        op2 = operand2;
    }

    @Override
    public Type getInstrType() {
        return Type.DIV_F;
    }

    @Override
    public String toString() {
        return var + " := " + op1 + " /. " + op2;
    }

}
