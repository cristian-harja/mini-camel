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
public final class KGet extends KNode {
    @Nonnull
    public final SymRef array, index;

    @Nonnull
    public final Type elementType;

    public KGet(SymRef array, SymRef index, Type elementType) {
        this.array = array;
        this.index = index;
        this.elementType = elementType;
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
        return elementType;
    }

    @Nonnull
    public String toString() {
        return array + ".(" + index + ")";
    }
}
