package mini_camel.ast;

import mini_camel.util.Visitor;
import mini_camel.util.Visitor1;
import mini_camel.util.Visitor2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * Boolean negation of an expression ({@code !e}).
 */
@Immutable
public final class AstNot extends AstExp {
    /**
     * The boolean expressions being negated.
     */
    @Nonnull
    public final AstExp e;

    public AstNot(AstExp e) {
        this.e = e;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public <T> T accept(Visitor1<T> v) {
        return v.visit(this);
    }

    public <T, U> T accept(Visitor2<T, U> v, @Nullable U a) {
        return v.visit(a, this);
    }

    @Nonnull
    public String toString(){
        return "(Not " + e.toString() + ")";
    }
}