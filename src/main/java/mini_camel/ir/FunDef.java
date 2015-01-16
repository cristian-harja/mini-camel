package mini_camel.ir;

import java.util.Collections;
import java.util.List;

public class FunDef
{
    public final Label name;
    public final List<Var> args;
    public final List<Var> locals;
    public final List<Instr> body;

    public FunDef(
            Label name,
            List<Var> args,
            List<Var> locals,
            List<Instr> body
    ) {
        this.name = name;
        this.args = Collections.unmodifiableList(args);
        this.locals = Collections.unmodifiableList(locals);
        this.body = Collections.unmodifiableList(body);
    }

    @Override
    public String toString() {
        return name.toString() + "(" + args + ")";
    }

}
