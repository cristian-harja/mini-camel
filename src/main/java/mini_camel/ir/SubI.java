package mini_camel.ir;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class SubI implements Instr {
    public final Var var;
    public final Operand op1, op2;

    public SubI(
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
        return Type.SUB_I;
    }

    @Override
    public String toString() {
        return var + " := " + op1 + " - " + op2;
    }
}
