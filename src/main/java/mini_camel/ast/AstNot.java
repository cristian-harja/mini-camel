package mini_camel.ast;

import mini_camel.visit.*;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * Boolean negation of an expression ({@code !e}).
 */
@Immutable
public final class AstNot extends AstExp {
    public final AstExp e;

    public AstNot(@Nonnull AstExp e) {
        this.e = e;
    }

    public void accept(@Nonnull Visitor v) {
        v.visit(this);
    }

    public <T> T accept(@Nonnull Visitor1<T> v) {
        return v.visit(this);
    }

    public <T, U> T accept(@Nonnull Visitor2<T, U> v, U a) {
        return v.visit(a, this);
    }

    public String toString(){
        return "(Not " + e.toString() + ")";
    }
}