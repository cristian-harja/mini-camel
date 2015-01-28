package mini_camel.ir.instr;

import mini_camel.ir.op.Operand;

import javax.annotation.Nonnull;
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

    @Nonnull
    public Type getInstrType() {
        return Type.RETURN;
    }

    @Nonnull
    public String toString() {
        return op == null ? "RET" : ("RET " + op);
    }
}
