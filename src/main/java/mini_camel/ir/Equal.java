package mini_camel.ir;

/**
 * Created by lina on 1/13/15.
 */
public class Equal extends Operation
{
    Var leq;
    Op op;

    public Equal(Var v, Op o)
    {
        leq = v;
        op = o;
    }

    public Equal(Var v)
    {
        leq = v;
    }

    public void printInfo()
    {
        super.printInfo();
        System.out.println("Variable : "+leq.varName);
        op.printInfo();
    }
}
