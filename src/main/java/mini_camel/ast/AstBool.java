package mini_camel.ast;

import mini_camel.util.Visitor;
import mini_camel.util.Visitor1;
import mini_camel.util.Visitor2;

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

    public static final AstBool TRUE = new AstBool(true);
    public static final AstBool FALSE = new AstBool(false);

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

    public static AstBool staticInstance(boolean b) {
        return b ? TRUE : FALSE;
    }
}
