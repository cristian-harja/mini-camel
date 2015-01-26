package mini_camel.transform;

import mini_camel.ast.*;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by lina on 1/19/15.
 */
public class UnusedVar extends DummyVisitor {

    private Set<String> right = new HashSet<>();
    private Set<String> left = new HashSet<>();
    private Set<String> unused = new HashSet<>();

    public UnusedVar() {
    }

    /**
     * This is the method that initiates the actual transformation.
     *
     * @param astNode input AST
     * @return transformed AST
     */
    public Set<String> applyTransform(@Nonnull AstExp astNode) {
        astNode.accept(this);
        left.removeAll(right);

        return left;

    }

    /**
     * When encountering a `let` expression, we try to rename the identifier
     * bound by it to something unique within the current environment.
     */
    @Override
    public void visit(@Nonnull AstLet e) {
        left.add(e.id.id);
        e.e1.accept(this);
        e.e2.accept(this);
    }

    /**
     * When encountering the usage of a variable, we check whether it was
     * renamed by the current transformation and return its new name.
     */
    @Override
    public void visit(@Nonnull AstVar e) {
        right.add(e.id.id);
    }


    @Override
    public void visit(@Nonnull AstLetRec e) {
        e.e.accept(this);
    }


}
