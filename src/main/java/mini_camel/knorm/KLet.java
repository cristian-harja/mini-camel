package mini_camel.knorm;

import mini_camel.type.Type;
import mini_camel.util.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class KLet extends KNode {
    @Nonnull
    public final SymDef id;

    @Nonnull
    public final KNode initializer;

    @Nonnull
    public final KNode ret;

    public KLet(SymDef id, KNode initializer, KNode ret) {
        this.id = id;
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
    public Type getDataType() {
        return id.type;
    }

    @Nonnull
    public String toString() {
        return String.format(
                "(let %s = %s in %s)",
                id.id, initializer, ret
        );
    }

}
