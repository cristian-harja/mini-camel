package mini_camel.ir;

import mini_camel.ir.instr.Instr;
import mini_camel.ir.instr.Label;
import mini_camel.ir.op.Var;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import java.util.Collections;
import java.util.List;

@Immutable
@ParametersAreNonnullByDefault
public final class Function {
    @Nonnull
    public final Label name;

    @Nonnull
    public final List<Var> free;

    @Nonnull
    public final List<Var> args;

    @Nonnull
    public final List<Var> locals;

    @Nonnull
    public final List<Instr> body;

    public Function(
            Label name,
            List<Var> args,
            List<Var> locals,
            List<Instr> body
    ) {
        this.name = name;
        this.free = Collections.emptyList();
        this.args = Collections.unmodifiableList(args);
        this.locals = Collections.unmodifiableList(locals);
        this.body = Collections.unmodifiableList(body);
    }

    public Function(
            Label name,
            List<Var> free,
            List<Var> args,
            List<Var> locals,
            List<Instr> body
    ) {
        this.name = name;
        this.free = Collections.unmodifiableList(free);
        this.args = Collections.unmodifiableList(args);
        this.locals = Collections.unmodifiableList(locals);
        this.body = Collections.unmodifiableList(body);
    }

    @Override
    public String toString() {
        return name.name + args;
    }

}
