package mini_camel.ast;

import mini_camel.util.Visitor;
import mini_camel.util.Visitor1;
import mini_camel.util.Visitor2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * Floating point negation of one expression (<code>-&#46;e</code>).
 */
@Immutable
public final class AstFNeg extends AstExp {
    /**
     * The expression being negated. Its data type is expected to be
     * {@link mini_camel.type.TFloat}.
     */
    @Nonnull
    public final AstExp e;

    public AstFNeg(AstExp e) {
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
        return "-.(" + e.toString() + ")";
    }
}