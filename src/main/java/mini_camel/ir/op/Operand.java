package mini_camel.ir.op;

import javax.annotation.Nonnull;

public interface Operand {
    enum Type {
        VAR,
        CONST_INT,
        CONST_FLOAT,
        LABEL
    }

    @Nonnull
    Type getOperandType();

    @Nonnull
    String toString();
}
