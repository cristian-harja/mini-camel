package mini_camel.ir.instr;

import mini_camel.ir.op.Operand;
import mini_camel.ir.op.Var;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class Assign implements Instr {
    public final Var var;
    public final Operand op;

    public Assign(@Nonnull Var v, @Nonnull Operand o) {
        var = v;
        op = o;
    }

    @Override
    public Type getInstrType() {
        return Type.ASSIGN;
    }

    @Override
    public String toString() {
        return var + " := " + op;
    }


}
