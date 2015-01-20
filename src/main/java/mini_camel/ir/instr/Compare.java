package mini_camel.ir.instr;

import mini_camel.ir.instr.Instr;
import mini_camel.ir.op.Operand;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class Compare implements Instr {
    public final Operand op1, op2;

    public Compare(
            @Nonnull Operand operand1,
            @Nonnull Operand operand2
    ) {
        op1 = operand1;
        op2 = operand2;
    }

    @Override
    public Type getInstrType() {
        return Type.CMP;
    }

    @Override
    public String toString() {
        return "CMP " + op1 + ", " + op2;
    }
}
