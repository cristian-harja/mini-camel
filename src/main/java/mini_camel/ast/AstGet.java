package mini_camel.ast;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * Array read operation ({@code e1.(e2)}).
 */
@Immutable
public final class AstGet extends AstExp {
    public final AstExp e1, e2;

    public AstGet(
            @Nonnull AstExp e1,
            @Nonnull AstExp e2
    ) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public void accept(@Nonnull Visitor v) {
        v.visit(this);
    }

    public <T, U> T accept(@Nonnull Visitor2<T, U> v, U a) {
        return v.visit(a, this);
    }

    public String toString(){
        return "(" + e1.toString() + ".(" + e2.toString() + "))";
    }
}