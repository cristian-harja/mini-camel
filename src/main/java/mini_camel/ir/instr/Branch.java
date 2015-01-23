package mini_camel.ir.instr;

import mini_camel.ir.op.Operand;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class Branch implements Instr {

    public final boolean lessOrEqual;

    @Nonnull
    public final Operand op1;

    @Nonnull
    public final Operand op2;

    @Nonnull
    public final Label ifTrue;

    @Nonnull
    public final Label ifFalse;

    public Branch(
            boolean lessOrEqual,
            @Nonnull Operand op1,
            @Nonnull Operand op2,
            @Nonnull Label ifTrue,
            @Nonnull Label ifFalse

    ) {
        this.lessOrEqual = lessOrEqual;
        this.op1 = op1;
        this.op2 = op2;
        this.ifTrue = ifTrue;
        this.ifFalse = ifFalse;
    }

    @Override
    public Type getInstrType() {
        return Type.BRANCH;
    }

    @Override
    public String toString() {
        return "IF (" + op1 + (lessOrEqual ? " <= " : " = ") + op2 +
                ") THEN " + ifTrue.name +
                " ELSE " + ifFalse.name;
    }
}
