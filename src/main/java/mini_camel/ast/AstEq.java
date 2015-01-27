package mini_camel.ast;

import mini_camel.visit.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * Equality test for two expressions ({@code e1 == e2}).
 */
@Immutable
public final class AstEq extends AstExp {
    /**
     * The expressions being tested for equality.
     */
    @Nonnull
    public final AstExp e1, e2;

    public AstEq(AstExp e1, AstExp e2) {
        this.e1 = e1;
        this.e2 = e2;
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
        return "(" + e1.toString() + " == " + e2.toString() + ")";
    }
}
