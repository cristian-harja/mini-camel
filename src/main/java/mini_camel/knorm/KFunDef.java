package mini_camel.knorm;

import mini_camel.util.SymDef;
import mini_camel.util.SymRef;
import mini_camel.visit.KFreeVars;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.*;

@Immutable
public final class KFunDef {
    @Nonnull
    public final SymDef name;

    @Nonnull
    public final List<SymDef> args;

    @Nonnull
    public final KNode body;

    @Nonnull
    public final List<SymRef> freeVars;

    public final boolean isRecursive;

    public KFunDef(
            SymDef name,
            List<SymDef> args,
            KNode body
    ) {
        this.name = name;
        this.args = Collections.unmodifiableList(args);
        this.body = body;

        boolean isRecursive = false;

        List<SymRef> free = KFreeVars.compute(body);
        Set<String> bound = new HashSet<>(args.size());
        for (SymDef def : args) {
            bound.add(def.id);
        }
        Iterator<SymRef> it = free.iterator();
        while (it.hasNext()) {
            SymRef ref = it.next();
            if (bound.contains(ref.id)) {
                it.remove();
            }
            if (ref.id.equals(name.id)) {
                isRecursive = true;
                it.remove();
            }
        }

        this.freeVars = Collections.unmodifiableList(free);
        this.isRecursive = isRecursive;
    }
}
