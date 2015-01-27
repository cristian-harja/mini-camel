package mini_camel.ast;

import mini_camel.visit.Visitor;
import mini_camel.visit.Visitor1;
import mini_camel.visit.Visitor2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * Addition of two integer expressions ({@code e1 + e2}).
 */
@Immutable
public final class AstAdd extends AstExp {
    /**
     * The operands of the sum. Their data type is expected to be
     * {@link mini_camel.type.TInt}.
     */
    @Nonnull
    public final AstExp e1, e2;

    public AstAdd(AstExp e1, AstExp e2) {
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
    public String toString() {
        return "(" + e1.toString() + " + " + e2.toString() + ")";
    }

}
