package mini_camel.visit;

import mini_camel.ast.*;
import mini_camel.util.SymRef;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashSet;
import java.util.Set;

@ParametersAreNonnullByDefault
public final class UnusedVar extends DummyVisitor {

    private Set<String> right = new HashSet<>();
    private Set<String> left = new HashSet<>();

    private UnusedVar() {
    }

    public static Set<String> compute(AstExp astNode) {
        UnusedVar uv = new UnusedVar();
        astNode.accept(uv);
        uv.left.removeAll(uv.right);
        return uv.left;
    }

    public void visit(AstLet e) {
        left.add(e.decl.id);
        e.initializer.accept(this);
        e.ret.accept(this);
    }

    public void visit(SymRef e) {
        right.add(e.id);
    }

    public void visit(AstLetRec e) {
        e.ret.accept(this);
    }

}
