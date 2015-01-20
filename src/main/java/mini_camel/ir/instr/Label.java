package mini_camel.ir.instr;

import mini_camel.ir.op.Operand;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class Label implements Instr, Operand {

    private static int x = 0;

    public final String name;

    public Label(@Nonnull String name) {
        this.name = name;
    }

    public static Label gen() {
        return new Label("l"+x++);
    }

    @Override
    public Instr.Type getInstrType() {
        return Instr.Type.LABEL;
    }

    @Override
    public Operand.Type getOperandType() {
        return Operand.Type.LABEL;
    }

    @Override
    public String toString() {
        return name + ":";
    }
}
