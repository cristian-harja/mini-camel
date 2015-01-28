package mini_camel.ir.instr;

import mini_camel.ir.op.Operand;
import mini_camel.ir.op.Var;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class ArrPut implements Instr {
    @Nonnull
    public final Var array;

    @Nonnull
    public final Operand index, value;

    public ArrPut(Var array, Operand index, Operand value) {
        this.array = array;
        this.index = index;
        this.value = value;
    }

    @Nonnull
    public Type getInstrType() {
        return Type.ARRAY_PUT;
    }

    @Nonnull
    public String toString() {
        return String.format(
                "%s[%s] := %s",
                array, index, value
        );
    }
}
