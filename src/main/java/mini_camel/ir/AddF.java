package mini_camel.ir;

/**
 * Created by lina and Yassine on 1/12/15.
 */
public class AddF extends Operation
{
    Var var;
    Op op1;
    Op op2;

    public AddF(Var v, Op operand1, Op operand2)
    { this.var = v; this.op1 = operand1; this.op2 = operand2;}

    @Override
    public Type getType() {
        return Type.ADD_F;
    }

    @Override
    public String toString() {
        return var + " := " + op1 + " +. " + op2;
    }

    public void printInfo()
    {
        super.printInfo();
        var.printInfo();
        op1.printInfo();
        op2.printInfo();
    }

}
