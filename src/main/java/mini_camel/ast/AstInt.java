package mini_camel.ast;

import mini_camel.util.Visitor;
import mini_camel.util.Visitor1;
import mini_camel.util.Visitor2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * Signed integer literal.
 */
@Immutable
public final class AstInt extends AstExp {
    /**
     * The value of the integer literal.
     */
    public final int i;

    public AstInt(int i) {
        this.i = i;
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
        return Integer.toString(i);
    }
}