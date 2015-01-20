package mini_camel.ast;

import mini_camel.ir.instr.Couple;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * Floating point negation of one expression (<code>-&#46;e</code>).
 */
@Immutable
public final class AstFNeg extends AstExp {
    public final AstExp e;

    public AstFNeg(@Nonnull AstExp e) {
        this.e = e;
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
        return "-.(" + e.toString() + ")";
    }
}