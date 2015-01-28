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
public final class ClosureApply implements Instr{
    @CheckForNull
    public final Var ret;

    @Nonnull
    public final Var cls;

    @Nonnull
    public final List<Operand> args;

    public ClosureApply(@Nullable Var ret, Var cls, List<Operand> l){
        this.ret = ret;
        this.cls = cls;
        args = Collections.unmodifiableList(l);
    }

    @Nonnull
    public Instr.Type getInstrType() {
        return Type.CLS_APPLY;
    }

    @Nonnull
    public String toString() {
        return String.format(
                "%sClosureApply(%s,%s)",
                ((ret != null) ? (ret.name + " := ") : ""),
                cls, args
        );
    }
}