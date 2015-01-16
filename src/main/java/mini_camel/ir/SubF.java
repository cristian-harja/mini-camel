package mini_camel.ir;

public class SubF extends Instr
{
    Var var;
    Op op1;
    Op op2;

    public SubF(Var v, Op operand1, Op operand2)
    { this.var = v; this.op1 = operand1; this.op2 = operand2;}

    @Override
    public Type getType() {
        return Type.SUB_F;
    }

    @Override
    public String toString() {
        return var + " := " + op1 + " -. " + op2;
    }

    public Var getVar(){
        return var;
    }

    public Op getOp1() {
        return op1;
    }

    public Op getOp2() {
        return op2;
    }

}
