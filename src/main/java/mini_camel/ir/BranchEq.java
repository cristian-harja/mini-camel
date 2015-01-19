package mini_camel.ir;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class BranchEq implements Instr {
    public final Label label;

    public BranchEq(@Nonnull Label l) {
        label = l;
    }

    @Override
    public Type getInstrType() {
        return Type.BEQ;
    }

    @Override
    public String toString() {
        return "BEQ " + label.name;
    }
}
