package mini_camel.ir;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class ConstFloat implements Operand {
    public final float value;

    public ConstFloat(float value) {
        this.value = value;
    }

    @Override
    public Type getOperandType() {
        return Type.CONST_FLOAT;
    }

    @Override
    public String toString() {
        return Float.toString(value);
    }


}
