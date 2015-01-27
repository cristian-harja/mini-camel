package mini_camel.knorm;

import mini_camel.type.Type;
import mini_camel.util.KVisitor;
import mini_camel.util.KVisitor1;
import mini_camel.util.KVisitor2;
import mini_camel.util.SymRef;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public final class KTuple extends KNode {
    @Nonnull
    public final List<SymRef> items;

    @Nonnull
    public final Type type;

    public KTuple(List<SymRef> items, Type type) {
        this.items = Collections.unmodifiableList(items);
        this.type = type;
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
        return type;
    }

    @Nonnull
    public String toString() {
        return items.toString();
    }
}
