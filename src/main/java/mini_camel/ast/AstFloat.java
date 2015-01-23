package mini_camel.ast;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * Floating point literal.
 */
@Immutable
public final class AstFloat extends AstExp {
    public final float f;

    public AstFloat(float f) {
        this.f = f;
    }

    public void accept(@Nonnull Visitor v) {
        v.visit(this);
    }

    public <T, U> T accept(@Nonnull Visitor2<T, U> v, U a) {
        return v.visit(a, this);
    }

    public <T, U> T accept(@Nonnull VisitorK<T, U> v, U a) {
        return v.visit(a, this);
    }


    public String toString(){
        return Float.toString(f);
    }
}
