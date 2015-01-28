package mini_camel.ir.op;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

@Immutable
@ParametersAreNonnullByDefault
public final class Var implements Operand {
    @Nonnull
    public final String name;

    @CheckForNull
    public final mini_camel.type.Type type;

    public Var(String name) {
        this.name = name;
        this.type = null;
    }

    public Var(String name, @Nullable mini_camel.type.Type type) {
        this.name = name;
        this.type = type;
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
