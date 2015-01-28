package mini_camel.ir.instr;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class Jump implements Instr {
    @Nonnull
    public final Label label;

    public Jump(Label l) {
        label = l;
    }

    @Nonnull
    public Type getInstrType() {
        return Type.JUMP;
    }

    @Nonnull
    public String toString() {
        return "JMP " + label.name;
    }
}
