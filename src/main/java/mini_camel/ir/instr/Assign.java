package mini_camel.ir.instr;

import mini_camel.ir.op.Operand;
import mini_camel.ir.op.Var;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class Assign implements Instr {
    @Nonnull
    public final Var var;

    @Nonnull
    public final Operand op;

    public Assign(Var v, Operand o) {
        var = v;
        op = o;
    }

    @Nonnull
    public Type getInstrType() {
        return Type.ASSIGN;
    }

    @Nonnull
    public String toString() {
        return var + " := " + op;
    }


}
