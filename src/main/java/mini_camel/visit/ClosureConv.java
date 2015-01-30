package mini_camel.visit;

import mini_camel.knorm.*;
import mini_camel.type.Type;
import mini_camel.util.SymDef;
import mini_camel.util.SymRef;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
@SuppressWarnings("unused")
public final class ClosureConv extends KTransformHelper {

    Map<String, KFunDef> topLevel = new LinkedHashMap<>();
    Set<String> known = new HashSet<>();
    Set<String> env = new HashSet<>();

    private ClosureConv() {
    }

    public static Program compute(KNode root, Set<String> globals) {
        ClosureConv cc = new ClosureConv();
        KNode result;

        cc.known.addAll(globals);
        result = root.accept(cc);

        return new Program(cc.topLevel, result);
    }

    private static Program compute(KNode root) {
        return compute(root, Collections.<String>emptySet());
    }

    @Nonnull
    public KNode visit(KLetRec e) {
        KNode e1_new, e2_new;
        String id = e.fd.name.id;
        List<SymRef> e1_free = new LinkedList<>(e.fd.freeVars);
        Iterator<SymRef> it;

        it = e1_free.iterator();
        while (it.hasNext()) {
            SymRef ref = it.next();
            if (known.contains(ref.id)) {
                it.remove();
            }
        }

        if (e1_free.isEmpty()) {
            known.add(id);
        }

        KFunDef fd = new KFunDef(
                e.fd.name, e.fd.args,
                e.fd.body.accept(this)
        );
        topLevel.put(id, fd);

        e2_new = e.ret.accept(this);

        List<SymRef> e2_free = KFreeVars.compute(e2_new);
        it = e2_free.iterator();
        while (it.hasNext()) {
            SymRef ref = it.next();
            if (!ref.id.equals(id)) {
                continue;
            }
            it.remove();
            e2_new = new ClosureMake(
                    new SymDef(id, Type.gen()), // fixme: type
                    fd.name.makeRef(),
                    e1_free,
                    e2_new
            );
            break;
        }

        return e2_new;

    }

    @Nonnull
    public KNode visit(KApp e) {
        SymRef f = e.f;
        if (known.contains(f.id)) {
            return new ApplyDirect(f, e.args);
        }
        return new ApplyClosure(f, e.args);
    }

}
