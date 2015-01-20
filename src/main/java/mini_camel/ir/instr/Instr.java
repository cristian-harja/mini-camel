package mini_camel.ir.instr;

public interface Instr  {

    public static enum Type {
        ADD_I,  // int
        ADD_F,  // float
        SUB_I,  // int
        SUB_F,  // float
        ASSIGN, // assignment
        MUL_F,  // multiplication
        DIV_F,  // division
        CALL,   // function call
        LABEL,  // label
        JUMP,   // unconditional branch
        BRANCH, // branch (if equal / if less than or equal)
        RETURN  // return from function call
    }

    Type getInstrType();
    String toString();

}