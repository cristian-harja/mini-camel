package mini_camel.ir;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class AddI implements Instr {
    public final Var var;
    public final Operand op1, op2;

    public AddI(
            @Nonnull Var v,
            @Nonnull Operand operand1,
            @Nonnull Operand operand2
    ) {
        this.var = v;
        this.op1 = operand1;
        this.op2 = operand2;
    }

    @Override
    public Type getInstrType() {
        return Type.ADD_I;
    }

    @Override
    public String toString() {
        return var + " := " + op1 + " + " + op2;
    }

}
