package mini_camel.ir.op;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class Var implements Operand {
    @Nonnull
    public final String name;

    public Var(String name) {
        this.name = name;
    }

    @Nonnull
    public Type getOperandType() {
        return Type.VAR;
    }

    @Nonnull
    public String toString() {
        return name;
    }
}
