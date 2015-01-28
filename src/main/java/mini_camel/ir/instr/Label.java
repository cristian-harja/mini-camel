package mini_camel.ir.instr;

import mini_camel.ir.op.Operand;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class Label implements Instr, Operand {

    private static int x = 0;

    @Nonnull
    public final String name;

    public Label(String name) {
        this.name = name;
    }

    @Nonnull
    public static Label gen() {
        return new Label("l"+x++);
    }

    @Nonnull
    public Instr.Type getInstrType() {
        return Instr.Type.LABEL;
    }

    @Nonnull
    public Operand.Type getOperandType() {
        return Operand.Type.LABEL;
    }

    @Nonnull
    public String toString() {
        return name + ":";
    }
}
