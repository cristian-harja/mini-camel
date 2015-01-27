package mini_camel.ast;

import mini_camel.visit.*;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * Integer negation of one expression (<code>-&#46;e</code>).
 */
@Immutable
public final class AstNeg extends AstExp {
    /**
     * The operands of the negation. Its data type is expected to be
     * {@link mini_camel.type.TInt}.
     */
    @Nonnull
    public final AstExp e;

    public AstNeg(AstExp e) {
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
        return "-(" + e.toString() + ")";
    }
}