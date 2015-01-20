package mini_camel.ir.instr;

import mini_camel.ir.op.Operand;

import javax.annotation.Nullable;
import javax.annotation.CheckForNull;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class Ret implements Instr {

    @CheckForNull
    public final Operand op;

    public Ret(@Nullable Operand op) {
        this.op = op;
    }

    @Override
    public Type getInstrType() {
        return Type.RETURN;
    }

    @Override
    public String toString() {
        return "RET " + op;
    }
}
