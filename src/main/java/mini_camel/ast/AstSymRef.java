package mini_camel.ast;

import mini_camel.visit.Visitor;
import mini_camel.visit.Visitor1;
import mini_camel.visit.Visitor2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Denotes a symbol reference.
 */
public final class AstSymRef extends AstExp {
    /**
     * Name of the identifier being referenced.
     */
    public final String id;

    public AstSymRef(String id) {
        this.id = id;
    }

    public AstSymRef(Id id) {
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
