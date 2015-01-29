package mini_camel.ir.instr;

import mini_camel.ir.op.Operand;
import mini_camel.ir.op.Var;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class ArrNew implements Instr {
    @Nonnull
    public final Var var;

    @Nonnull
    public final Operand size;

    @CheckForNull
    public final Operand init;

    public ArrNew(Var var, Operand size, @Nullable Operand init) {
        this.var = var;
        this.size = size;
        this.init = init;
    }

    @Nonnull
    public Type getInstrType() {
        return Type.ARRAY_NEW;
    }

    @Nonnull
    public String toString() {
        return String.format(
                "%s := new Array(%s%s)",
                var.name, size.toString(),
                init == null ? "" : (", " + init.toString())
        );
    }
}
