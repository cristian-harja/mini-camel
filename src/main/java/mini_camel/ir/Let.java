package mini_camel.ir;

/**
 * Created by lina on 1/13/15.
 */
public class Let extends Operation
{
    Var leq;
    Op op;
    Operation operation;

    public Let(Var v, Op o)
    {
        leq = v;
        op = o;
    }

    public Let(Var v, Operation operation)
    {
        leq = v;
        this.operation = operation;
    }

    public void printInfo()
    {
        super.printInfo();
        System.out.println("Variable : "+leq.varName);
        op.printInfo();
    }
}
