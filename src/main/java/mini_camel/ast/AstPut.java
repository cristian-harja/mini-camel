package mini_camel.ast;

import mini_camel.visit.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * Array write operation (<code>array.(index)&lt;-value</code>).
 */
@Immutable
public final class AstPut extends AstExp {
    /**
     * The expression that is going to be interpreted as an array. Its data
     * type is expected to be {@link mini_camel.type.TArray}.
     */
    @Nonnull
    public final AstExp array;

    /**
     * The expression that will be interpreted as an index in the array. Its
     * data type is expected to be {@link mini_camel.type.TInt}.
     */
    @Nonnull
    public final AstExp index;

    /**
     * The expression that is going to be assigned to an element of the array.
     */
    @Nonnull
    public final AstExp value;

    public AstPut(AstExp array, AstExp index, AstExp value) {
        this.array = array;
        this.index = index;
        this.value = value;
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
    public String toString() {
        return "(" + array.toString() + ".(" + index.toString() + ") <- " + value.toString() + ")";
    }
}