package mini_camel.ir.instr;

import javax.annotation.Nonnull;

public interface Instr  {

    public static enum Type {
        ADD_I,  // int
        ADD_F,  // float
        SUB_I,  // int
        SUB_F,  // float
        ASSIGN, // assignment
        MUL_F,  // multiplication
        DIV_F,  // division
        CALL,   // function call (apply direct)
        CLS_APPLY, // closure apply
        CLS_MAKE,  // closure creation
        LABEL,  // label
        JUMP,   // unconditional branch
        BRANCH, // branch (if equal / if less than or equal)
        RETURN  // return from function call
    }

    @Nonnull
    Type getInstrType();

    @Nonnull
    String toString();

}