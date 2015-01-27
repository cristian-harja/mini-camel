package mini_camel.ast;

import mini_camel.visit.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * Boolean literal ({@code true} or {@code false}).
 */
@Immutable
public final class AstBool extends AstExp {
    /**
     * The value of the boolean literal.
     */
    public final boolean b;

    public AstBool(boolean b) {
        this.b = b;
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
        return Boolean.toString(b);
    }

}
