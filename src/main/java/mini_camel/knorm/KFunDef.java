package mini_camel.knorm;

import mini_camel.util.SymDef;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Collections;
import java.util.List;

@Immutable
public final class KFunDef {
    @Nonnull
    public final SymDef name;

    @Nonnull
    public final List<SymDef> args;

    @Nonnull
    public final List<String> freeVars;

    @Nonnull
    public final KNode body;

    public KFunDef(
            SymDef name,
            List<SymDef> args,
            KNode body
    ) {
        this(name, args, Collections.<String>emptyList(), body);
    }

    public KFunDef(
            SymDef name,
            List<SymDef> args,
            List<String> free,
            KNode body
    ) {
        this.name = name;
        this.args = Collections.unmodifiableList(args);
        freeVars = Collections.unmodifiableList(free);
        this.body = body;
    }
}