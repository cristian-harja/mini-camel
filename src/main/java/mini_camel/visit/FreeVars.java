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
    private Set<AstSymRef> free = new HashSet<>();
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

    public Set<AstSymRef> getFreeVariables() {
        return free;
    }

    public Set<String> getFreeNames() {
        return freeStr;
    }

    @Override
    public void visit(@Nonnull AstLet e) {
        String id = e.decl.id;
        e.initializer.accept(this);
        bound.add(id);
        e.ret.accept(this);
        bound.remove(id);
    }

    @Override
    public void visit(@Nonnull AstSymRef e) {
        String id = e.id;
        if (bound.contains(id)) return;
        if (globals.contains(id)) return;
        free.add(e);
        freeStr.add(id);
    }

    @Override
    public void visit(@Nonnull AstLetRec e) {
        String id = e.fd.decl.id;
        bound.add(id);
        e.fd.accept(this);
        e.ret.accept(this);
        bound.remove(id);
    }

    @Override
    public void visit(@Nonnull AstLetTuple e) {
        e.initializer.accept(this);
        bound.addAll(e.getIdentifierList());
        e.ret.accept(this);
        bound.removeAll(e.getIdentifierList());
    }

    @Override
    public void visit(@Nonnull AstFunDef e) {
        bound.addAll(e.getArgumentNames());
        e.body.accept(this);
        bound.removeAll(e.getArgumentNames());
    }
}
