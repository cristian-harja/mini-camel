package mini_camel.ir.instr;

import mini_camel.ir.op.Operand;
import mini_camel.ir.op.Var;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.Collections;
import java.util.List;

@Immutable
public final class DirApply implements Instr {
    @CheckForNull
    public final Var ret;

    @Nonnull
    public final String name;

    @Nonnull
    public final List<Operand> args;

    public DirApply(@Nullable Var ret, String s, List<Operand> l) {
        this.ret = ret;
        name = s;
        args = Collections.unmodifiableList(l);
    }
    @Nonnull
    public Type getInstrType() {
        return Type.CALL;
    }

    @Nonnull
    public String toString() {
        return String.format(
                "%sCALL %s%s",
                ((ret != null)? (ret.name + " := ") : ""),
                name, args
        );
    }

}
