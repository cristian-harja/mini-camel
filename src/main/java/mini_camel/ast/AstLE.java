package mini_camel.ast;

import mini_camel.ir.Couple;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * Inequality between two numeric expressions.
 */
@Immutable
public final class AstLE extends AstExp {
    public final AstExp e1, e2;

    public AstLE(
            @Nonnull AstExp e1,
            @Nonnull AstExp e2
    ) {
        this.e1 = e1;
        this.e2 = e2;
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
        return "(" + e1.toString() + " <= " + e2.toString() + ")";
    }
}