package mini_camel.ir;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class Jump implements Instr {
    public final Label label;

    public Jump(@Nonnull Label l) {
        label = l;
    }

    @Override
    public Type getInstrType() {
        return Type.JUMP;
    }

    @Override
    public String toString() {
        return "JMP " + label.name;
    }
}
