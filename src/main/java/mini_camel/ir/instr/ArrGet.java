package mini_camel.ir.instr;

import mini_camel.ir.op.Operand;
import mini_camel.ir.op.Var;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class ArrGet implements Instr {
    @Nonnull
    public final Var output, array;

    @Nonnull
    public final Operand index;

    public ArrGet(Var output, Var array, Operand index) {
        this.output = output;
        this.array = array;
        this.index = index;
    }

    @Nonnull
    public Type getInstrType() {
        return Type.ARRAY_GET;
    }

    @Nonnull
    public String toString() {
        return String.format(
                "%s := %s[%s]",
                output, array, index
        );
    }
}
