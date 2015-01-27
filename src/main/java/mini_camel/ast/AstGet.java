package mini_camel.ast;

import mini_camel.util.Visitor;
import mini_camel.util.Visitor1;
import mini_camel.util.Visitor2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * Array read operation ({@code array.(index)}).
 */
@Immutable
public final class AstGet extends AstExp {
    /**
     * The expression being interpreted as an array. Its data type is
     * expected to be {@link mini_camel.type.TArray}.
     */
    @Nonnull
    public final AstExp array;

    /**
     * The expression being interpreted as an array index. Its data type
     * is expected to be {@link mini_camel.type.TInt}.
     */
    @Nonnull
    public final AstExp index;

    public AstGet(AstExp e1, AstExp e2) {
        this.array = e1;
        this.index = e2;
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
        return "(" + array.toString() + ".(" + index.toString() + "))";
    }
}