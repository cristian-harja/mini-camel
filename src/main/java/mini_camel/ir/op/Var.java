package mini_camel.ir.op;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class Var implements Operand {
    public final String name;

    public Var(@Nonnull String name) {
        this.name = name;
    }

    @Override
    public Type getOperandType() {
        return Type.VAR;
    }

    @Override
    public String toString() {
        return name;
    }
}
