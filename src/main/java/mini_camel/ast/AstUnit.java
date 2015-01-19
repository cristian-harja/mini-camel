package mini_camel.ast;

import mini_camel.ir.instr.Couple;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * The "unit" / "void" expression (denoted by {@code ()} in the source code).
 */
@Immutable
public final class AstUnit extends AstExp {

    public void accept(@Nonnull Visitor v) {
        v.visit(this);
    }

    @Override
    public Couple accept(@Nonnull Visitor3 v) {return v.visit(this); }

    public <T, U> T accept(@Nonnull Visitor2<T, U> v, U a) {
        return v.visit(a, this);
    }

    public String toString(){
        return "()";
    }
}