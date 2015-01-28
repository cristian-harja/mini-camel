package mini_camel.ir.instr;

import mini_camel.ir.op.Operand;
import mini_camel.ir.op.Var;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Collections;
import java.util.List;

@Immutable
public final class ClsMake implements Instr{
    @Nonnull
    public final Var v;

    @Nonnull
    public final Label fun;

    @Nonnull
    public final List<Operand> free_args;

    public ClsMake(Var v, Label l, List<Operand> list){
        this.v = v;
        fun = l;
        free_args = Collections.unmodifiableList(list);
    }

    @Nonnull
    public Instr.Type getInstrType() {
        return Instr.Type.CLS_MAKE;
    }

    @Nonnull
    public String toString() {
        return String.format(
                "%s := ClosureMake(%s,%s)",
                v.name, fun.name, free_args.toString()
        );
    }
}
