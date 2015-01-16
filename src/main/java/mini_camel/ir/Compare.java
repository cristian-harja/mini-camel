package mini_camel.ir;

public class Compare extends Instr {
    private Op op1;
    private Op op2;

    public Compare(Op operand1, Op operand2) {
        op1 = operand1;
        op2 = operand2;
    }

    public Op getOp1() {
        return op1;
    }

    public Op getOp2() {
        return op2;
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public String toString() {
        return null;
    }
}
