package mini_camel.ir;

import java.util.List;

/**
 * Created by yassine on 1/13/15.
 */
public class FunDef extends Instr
{
    private Var f;
    private List<Var> args;
    private List<Operation> body;


    public FunDef(Var name, List<Var> l, List<Operation> body)
    {
        f = name;
        args = l;
        this.body = body;
    }

    public void addArg(Var v) {args.add(v);}

    public void addOp(Operation op) {body.add(op);}

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public String toString() {
        return null;
    }



}
