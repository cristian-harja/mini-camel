package mini_camel.knorm;

import mini_camel.type.Type;
import mini_camel.util.KVisitor;
import mini_camel.util.KVisitor1;
import mini_camel.util.KVisitor2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class KNode {

    public abstract void accept(KVisitor v);

    public abstract <T> T accept(KVisitor1<T> v);

    public abstract <T, U> T accept(KVisitor2<T, U> v, @Nullable U a);

    @Nonnull
    public abstract Type getDataType();

    @Nonnull
    public abstract String toString();
}
