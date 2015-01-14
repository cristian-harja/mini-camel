package mini_camel.ir;

/**
 * Created by lina and Yassine on 1/12/15.
 */
public class Sub extends Operation
{
    Var var;
    Op op1;
    Op op2;

    public Sub(Var v, Op operand1, Op operand2)
    { this.var = v; this.op1 = operand1; this.op2 = operand2;}

    public void printInfo()
    {
        super.printInfo();
        var.printInfo();
        op1.printInfo();
        op2.printInfo();
    }
}
