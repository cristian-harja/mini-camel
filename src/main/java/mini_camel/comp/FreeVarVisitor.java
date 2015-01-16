package mini_camel.comp;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import mini_camel.ast.*;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class FreeVarVisitor extends DummyVisitor {

    private Set<String> globals = new HashSet<>();
    private Multiset<String> bound = HashMultiset.create();
    private Set<Id> free = new HashSet<>();
    private Set<Id> freeReadOnly = Collections.unmodifiableSet(free);

    public FreeVarVisitor() {
    }

    public FreeVarVisitor(@Nonnull Set<String> globals) {
        this.globals.addAll(globals);
    }

    public FreeVarVisitor(@Nonnull String ... globals) {
        Collections.addAll(this.globals, globals);
    }

    public Set<Id> getFreeVariables() {
        return freeReadOnly;
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