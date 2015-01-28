package mini_camel.ir.op;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class ConstInt implements Operand {
    public final int value;

    public ConstInt(int value) {
        this.value = value;
    }

    @Nonnull
    public Type getOperandType() {
        return Type.CONST_INT;
    }

    @Nonnull
    public String toString() {
        return Integer.toString(value);
    }


}
