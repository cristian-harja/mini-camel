package mini_camel.ast;

import mini_camel.visit.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * Floating point division (<code>e1 /&#46; e2</code>).
 */
@Immutable
public final class AstFDiv extends AstExp {
    /**
     * The operands of the division. Their data type is expected to be
     * {@link mini_camel.type.TFloat}.
     */
    @Nonnull
    public final AstExp e1, e2;

    public AstFDiv(AstExp e1, AstExp e2) {
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
        return "(" + e1.toString() + " /. " + e2.toString() + ")";
    }
}
