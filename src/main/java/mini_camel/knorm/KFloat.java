package mini_camel.knorm;

import mini_camel.type.TFloat;
import mini_camel.type.Type;
import mini_camel.util.KVisitor;
import mini_camel.util.KVisitor1;
import mini_camel.util.KVisitor2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class KFloat extends KNode {
    public final float f;

    public KFloat(float f) {
        this.f = f;
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
        return TFloat.INSTANCE;
    }

    @Nonnull
    public String toString() {
        return Float.toString(f);
    }

}
