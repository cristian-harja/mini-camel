package mini_camel.visit;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import mini_camel.ast.*;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class FreeVars extends DummyVisitor {

    private Set<String> globals = new HashSet<>();
    private Multiset<String> bound = HashMultiset.create();
    private Set<Id> free = new HashSet<>();
    private Set<String> freeStr = new HashSet<>();

    private FreeVars() {
    }

    public static FreeVars compute(@Nonnull AstExp root) {
        return compute(root, Collections.<String>emptyList());
    }

    public static FreeVars compute(
            @Nonnull AstExp root,
            @Nonnull Collection<String> globals
    ) {
        FreeVars fvv = new FreeVars();
        fvv.globals.addAll(globals);
        root.accept(fvv);
        return fvv;
    }

    public Set<Id> getFreeVariables() {
        return free;
    }

    public Set<String> getFreeNames() {
        return freeStr;
    }

    @Override
    public void visit(@Nonnull AstLet e) {
        e.e1.accept(this);
        bound.add(e.id.id);
        e.e2.accept(this);
        bound.remove(e.id.id);
    }

    @Override
    public void visit(@Nonnull AstVar e) {
        String id = e.id.id;
        if (bound.contains(id)) return;
        if (globals.contains(id)) return;
        free.add(e.id);
        freeStr.add(e.id.id);
    }

    @Override
    public void visit(@Nonnull AstLetRec e) {
        bound.add(e.fd.id.id);
        e.fd.accept(this);
        e.e.accept(this);
        bound.remove(e.fd.id.id);
    }

    @Override
    public void visit(@Nonnull AstLetTuple e) {
        e.e1.accept(this);
        for (Id item : e.ids) {
            bound.add(item.id);
        }
        e.e2.accept(this);
    }

    @Override
    public void visit(@Nonnull AstFunDef e) {
        for (Id item : e.args) {
            bound.add(item.id);
        }
        e.e.accept(this);
        for (Id item : e.args) {
            bound.remove(item.id);
        }
    }
}
