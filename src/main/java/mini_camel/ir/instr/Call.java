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
public final class Call implements Instr {
    @CheckForNull
    public final Var ret;
    public final String name;
    public final List<Operand> args;

    public Call(
            @Nullable Var ret,
            @Nonnull String s,
            @Nonnull List<Operand> l
    ) {
        this.ret = ret;
        name = s;
        args = Collections.unmodifiableList(l);
    }
    @Override
    public Type getInstrType() {
        return Type.CALL;
    }

    @Override
    public String toString() {
        return ((ret != null)? (ret.name + " = ") : "") +
                "CALL " + name + args;
    }

}
