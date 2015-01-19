package mini_camel.ir.instr;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class BranchLe implements Instr {
    public final Label label;

    public BranchLe(@Nonnull Label l) {
        label = l;
    }

    @Override
    public Type getInstrType() {
        return Type.BLE;
    }

    @Override
    public String toString() {
        return "BLE " + label.name;
    }
}
