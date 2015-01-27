package mini_camel.knorm;

import mini_camel.util.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.Collections;
import java.util.List;

@Immutable
public final class KLetTuple extends KNode {
    @Nonnull
    public final List<SymDef> ids;

    @Nonnull
    public final SymRef initializer;

    @Nonnull
    public final KNode ret;

    public KLetTuple(List<SymDef> ids, SymRef initializer, KNode ret) {
        this.ids = Collections.unmodifiableList(ids);
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
                "let %s = %s in (%s)",
                ids, initializer.id, ret.toString()
        );
    }

}
