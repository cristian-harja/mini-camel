package mini_camel.ast;

import mini_camel.ir.Couple;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * Signed integer literal.
 */
@Immutable
public final class AstInt extends AstExp {
    public final int i;

    public AstInt(int i) {
        this.i = i;
    }

    public void accept(@Nonnull Visitor v) {
        v.visit(this);
    }

    @Override
    public Couple accept(@Nonnull Visitor3 v) {
        return v.visit(this);
    }

    public <T, U> T accept(@Nonnull Visitor2<T, U> v, U a) {
        return v.visit(a, this);
    }

    public String toString(){
        return Integer.toString(i);
    }
}