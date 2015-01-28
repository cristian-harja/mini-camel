package mini_camel.visit;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import mini_camel.ast.*;
import mini_camel.util.SymDef;
import mini_camel.util.SymRef;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ParametersAreNonnullByDefault
public final class FreeVars extends DummyVisitor {

    private Set<String> globals = new HashSet<>();
    private Multiset<String> bound = HashMultiset.create();
    private Set<SymRef> free = new HashSet<>();
    private Set<String> freeStr = new HashSet<>();

    private FreeVars() {
    }

    public static FreeVars compute(AstExp root) {
        return compute(root, Collections.<String>emptyList());
    }

    public static FreeVars compute(
            AstExp root,
            Collection<String> globals
    ) {
        FreeVars fvv = new FreeVars();
        fvv.globals.addAll(globals);
        root.accept(fvv);
        return fvv;
    }

    public Set<SymRef> getFreeVariables() {
        return free;
    }

    public Set<String> getFreeNames() {
        return freeStr;
    }

    public void visit(AstLet e) {
        String id = e.decl.id;
        e.initializer.accept(this);
        bound.add(id);
        e.ret.accept(this);
        bound.remove(id);
    }

    public void visit(SymRef e) {
        String id = e.id;
        if (bound.contains(id)) return;
        if (globals.contains(id)) return;
        free.add(e);
        freeStr.add(id);
    }

    public void visit(AstLetRec e) {
        String id = e.fd.decl.id;
        bound.add(id);
        e.fd.accept(this);
        e.ret.accept(this);
        bound.remove(id);
    }

    public void visit(AstLetTuple e) {
        e.initializer.accept(this);
        bound.addAll(e.getIdentifierList());
        e.ret.accept(this);
        for (SymDef s : e.ids) {
            // Can't use removeAll() because it removes all occurrences, not
            // just one.
            bound.remove(s.id);
        }
    }

    public void visit(AstFunDef e) {
        bound.addAll(e.getArgumentNames());
        e.body.accept(this);
        for (SymDef s : e.args) {
            // Can't use removeAll() because it removes all occurrences, not
            // just one.
            bound.remove(s.id);
        }
    }
}
