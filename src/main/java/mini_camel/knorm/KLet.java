package mini_camel.knorm;

import mini_camel.type.Type;
import mini_camel.util.KVisitor;
import mini_camel.util.KVisitor1;
import mini_camel.util.KVisitor2;
import mini_camel.util.SymRef;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class KLet extends KNode {
    @Nonnull
    public final SymRef id;

    @Nonnull
    public final Type t;

    @Nonnull
    public final KNode initializer;

    @Nonnull
    public final KNode ret;

    public KLet(SymRef id, Type t, KNode initializer, KNode ret) {
        this.id = id;
        this.t = t;
        this.initializer = initializer;
        this.ret = ret;
    }


    public void accept(KVisitor v) {
        v.visit(this);
    }

    public <T> T accept(KVisitor1<T> v) {
        return v.visit(this);
    }

    public <T, U> T accept(KVisitor2<T, U> v, @Nullable U a) {
        return v.visit(a, this);
    }

    @Nonnull
    public String toString() {
        return String.format(
                "let %s = (%s) in (%s)",
                id.id, initializer, ret
        );
    }

}
