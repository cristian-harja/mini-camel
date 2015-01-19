package mini_camel.ir;

import mini_camel.ir.instr.Instr;
import mini_camel.ir.instr.Label;
import mini_camel.ir.op.Var;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Collections;
import java.util.List;

@Immutable
public final class Function {
    public final Label name;
    public final List<Var> args;
    public final List<Var> locals;
    public final List<Instr> body;

    public Function(
            @Nonnull Label name,
            @Nonnull List<Var> args,
            @Nonnull List<Var> locals,
            @Nonnull List<Instr> body
    ) {
        this.name = name;
        this.args = Collections.unmodifiableList(args);
        this.locals = Collections.unmodifiableList(locals);
        this.body = Collections.unmodifiableList(body);
    }

    @Override
    public String toString() {
        return name.name + args;
    }

}
