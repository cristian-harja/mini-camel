package mini_camel.ir;

import javax.annotation.Nonnull;

public class Ret extends Instr {

    Op operand;

    public Ret(@Nonnull Op operand) {
        this.operand = operand;
    }

    public Op getOperand() {
        return operand;
    }

    @Override
    public Type getType() {
        return Type.RETURN;
    }

    @Override
    public String toString() {
        return "RET " + operand;
    }
}
