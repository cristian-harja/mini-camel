package mini_camel.ir.op;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class ConstInt implements Operand {
    public final int value;

    public ConstInt(int value) {
        this.value = value;
    }

    @Override
    public Type getOperandType() {
        return Type.CONST_INT;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }


}
