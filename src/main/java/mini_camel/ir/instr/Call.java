package mini_camel.ir.instr;

import mini_camel.ir.op.Operand;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Collections;
import java.util.List;

@Immutable
public final class Call implements Instr {
    public final String name;
    public final List<Operand> args;

    public Call(
            @Nonnull String s,
            @Nonnull List<Operand> l
    ) {
        name = s;
        args = Collections.unmodifiableList(l);
    }
    @Override
    public Type getInstrType() {
        return Type.CALL;
    }

    @Override
    public String toString() {
        return "CALL " + name + args;
    }

}
