package mini_camel.ast;

import mini_camel.util.Visitor;
import mini_camel.util.Visitor1;
import mini_camel.util.Visitor2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * Array creation (<code>Array.Create &lt;size&gt; &lt;initializer&gt;</code>).
 */
@Immutable
public final class AstArray extends AstExp {
    /**
     * The size of the array to be created.
     */
    @Nonnull
    public final AstExp size;

    /**
     * The initial value for every element of the array.
     */
    @Nonnull
    public final AstExp initializer;

    public AstArray(AstExp size, AstExp initializer) {
        this.size = size;
        this.initializer = initializer;
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
        return "(Array.Create " +
                size.toString() + " " +
                initializer.toString() + ")";
    }
}