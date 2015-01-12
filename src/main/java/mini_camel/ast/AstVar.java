package mini_camel.ast;

import mini_camel.Id;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * Reference to a variable / named symbol.
 */
@Immutable
public final class AstVar extends AstExp {
    public final Id id;

    public AstVar(@Nonnull Id id) {
        this.id = id;
    }

    public void accept(@Nonnull Visitor v) {
        v.visit(this);
    }

    public <T, U> T accept(@Nonnull Visitor2<T, U> v, U a) {
        return v.visit(a, this);
    }

    public String toString(){
        return id.toString();
    }
}