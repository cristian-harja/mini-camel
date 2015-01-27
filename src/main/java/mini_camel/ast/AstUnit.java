package mini_camel.ast;

import mini_camel.visit.*;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * The "unit" / "void" expression (denoted by {@code ()} in the source code).
 */
@Immutable
public final class AstUnit extends AstExp {

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
        return "()";
    }
}