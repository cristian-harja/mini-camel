package mini_camel.knorm;

import mini_camel.util.KVisitor;
import mini_camel.util.KVisitor1;
import mini_camel.util.KVisitor2;
import mini_camel.util.SymRef;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class KPut extends KNode {
    @Nonnull
    public final SymRef array;

    @Nonnull
    public final SymRef index;

    @Nonnull
    public final SymRef value;

    public KPut(SymRef array, SymRef index, SymRef value) {
        this.array = array;
        this.index = index;
        this.value = value;
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
                "%s.(%s) <- %s",
                array, index, value
        );
    }

}
