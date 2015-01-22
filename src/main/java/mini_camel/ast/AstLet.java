package mini_camel.ast;

import mini_camel.type.Type;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * Let-expression ({@code let id = e1 in e2}).
 */
@Immutable
public final class AstLet extends AstExp {
    public final Id id;
    public final Type id_type;
    public final AstExp e1;
    public final AstExp e2;

    public AstLet(
            @Nonnull Id id,
            @Nonnull Type t,
            @Nonnull AstExp e1,
            @Nonnull AstExp e2
    ) {
        this.id = id;
        this.id_type = t;
        this.e1 = e1;
        this.e2 = e2;
    }

    public void accept(@Nonnull Visitor v) {
        v.visit(this);
    }

    public <T, U> T accept(@Nonnull Visitor2<T, U> v, U a) {
        return v.visit(a, this);
    }

    public <T, U> T accept(@Nonnull VisitorK<T, U> v, U a) {
        return v.visit(a, this);
    }


    public String toString(){
        return "(let " + id.id + " = " + e1 + " in " + e2 + ")";
    }
}