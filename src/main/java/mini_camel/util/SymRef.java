package mini_camel.util;

import mini_camel.ast.AstExp;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

/**
 * Denotes a symbol reference.
 */
@Immutable
@ParametersAreNonnullByDefault
public final class SymRef extends AstExp {
    /**
     * Name of the identifier being referenced.
     */
    public final String id;

    public SymRef(String id) {
        this.id = id;
    }

    public SymRef(Id id) {
        this(id.id);
        setSymbol(id.getSymbol());
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
        return id;
    }

}
