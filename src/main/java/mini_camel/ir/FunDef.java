package mini_camel.ir;

import java.util.List;

/**
 * Created by yassine on 1/13/15.
 */
public class FunDef
{
    public final Var f;
    public final List<Var> args;
    public final List<Instr> body;


    public FunDef(Var name, List<Var> l, List<Instr> body)
    {
        f = name;
        args = l;
        this.body = body;
    }

    public void addArg(Var v) {args.add(v);}

    public void addOp(Operation op) {body.add(op);}

    @Override
    public String toString() {
        return f.toString() + "(" + args + ")";
    }

}
