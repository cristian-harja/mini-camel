package mini_camel.ast;

import mini_camel.visit.*;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * Boolean literal ({@code true} or {@code false}).
 */
@Immutable
public final class AstBool extends AstExp {
    public final boolean b;

    public AstBool(boolean b) {
        this.b = b;
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
        return b ? "True" : "False";
    }

}
