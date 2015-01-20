package mini_camel.ir.op;

public interface Operand {
    enum Type {
        VAR,
        CONST_INT,
        CONST_FLOAT,
        LABEL
    }

    Type getOperandType();
}
