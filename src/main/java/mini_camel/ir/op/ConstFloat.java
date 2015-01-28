package mini_camel.ir.op;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class ConstFloat implements Operand {
    public final float value;

    public ConstFloat(float value) {
        this.value = value;
    }

    @Nonnull
    public Type getOperandType() {
        return Type.CONST_FLOAT;
    }

    @Nonnull
    public String toString() {
        return Float.toString(value);
    }


}
