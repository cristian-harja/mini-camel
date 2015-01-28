package mini_camel.visit;

import com.google.common.collect.Lists;
import mini_camel.knorm.*;
import mini_camel.util.SymDef;
import mini_camel.util.SymRef;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
@SuppressWarnings("unused")
public final class ClosureConv extends KTransformHelper {

    List<KFunDef> topLevel = new ArrayList<>();
    Set<String> known = new HashSet<>();
    Set<String> env = new HashSet<>();

    private ClosureConv() {
    }

    public static Program compute(KNode root, Set<String> globals) {
        ClosureConv cc = new ClosureConv();
        KNode result;

        cc.known.addAll(globals);
        result = root.accept(cc);
        cc.topLevel = Lists.reverse(cc.topLevel);

        Map<String, KFunDef> topLevel = new LinkedHashMap<>();

        for (KFunDef fd : cc.topLevel) {
            topLevel.put(fd.name.id, fd);
        }

        return new Program(topLevel, result);
    }

    private static Program compute(KNode root) {
        return compute(root, Collections.<String>emptySet());
    }

    @Nonnull
    public KNode visit(KLetRec e) {
        KNode e1_new, e2_new;

        SymDef fSymbol = e.fd.name;
        String fName = fSymbol.id;
        Set<String> argNames = new HashSet<>();
        List<String> fv;

        for (SymDef id : e.fd.args) {
            argNames.add(id.id);
        }

        known.add(fName);
        env.add(fName);
        env.addAll(argNames);
        {
            e1_new = e.fd.body.accept(this);
            fv = SymRef.idList(KFreeVars.compute(e.fd.body));
            fv.removeAll(argNames);

            if (!fv.isEmpty()) {
                known.remove(fName);
                e1_new = e.fd.body.accept(this);
                fv = SymRef.idList(KFreeVars.compute(e.fd.body));
                fv.removeAll(argNames);
                fv.remove(fName);
            }

            topLevel.add(new KFunDef(
                    fSymbol,
                    e.fd.args,
                    fv,
                    e1_new
            ));

            env.removeAll(argNames);
            e2_new = e.ret.accept(this);

            if (SymRef.idList(KFreeVars.compute(e2_new)).contains(fName)) {
                List<SymRef> refs = new ArrayList<>(fv.size());
                for (String var : fv) {
                    refs.add(new SymRef(var));
                }
                e2_new = new KLet(
                        fSymbol, // reusing same ID ? fixme
                        new ClosureMake(fSymbol.makeRef(), refs),
                        e2_new
                );
            }
        }
        known.remove(fName);
        env.remove(fName);

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

    @Nonnull
    public KNode visit(KLet e) {
        KNode e_new;
        String id = e.id.id;
        env.add(id);
        e_new = super.visit(e);
        env.remove(id);
        return e_new;
    }

    @Nonnull
    public KNode visit(KLetTuple e) {
        List<String> args = new ArrayList<>(e.ids.size());
        for (SymDef id : e.ids) {
            args.add(id.id);
        }
        env.addAll(args);
        KNode e_new = super.visit(e);
        env.removeAll(args);
        return e_new;
    }

}
