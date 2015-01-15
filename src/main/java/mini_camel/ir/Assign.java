package mini_camel.ir;

/**
 * Created by lina on 1/13/15.
 */
public class Assign extends Operation
{
    Var leq;
    Op op;

    public Assign(Var v, Op o)
    {
        leq = v;
        op = o;
    }

    @Override
    public Type getType() {
        return Type.ASSIGN;
    }

    public Var getVar(){return leq;}

    public Op getOp(){return op;}

    @Override
    public String toString() {
        return leq + " := " + op;
    }


}
