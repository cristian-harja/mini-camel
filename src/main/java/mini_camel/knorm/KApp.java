package mini_camel.knorm;

import mini_camel.type.Type;
import mini_camel.util.KVisitor;
import mini_camel.util.KVisitor1;
import mini_camel.util.KVisitor2;
import mini_camel.util.SymRef;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.Collections;
import java.util.List;

@Immutable
public final class KApp extends KNode {
    @Nonnull
    public final SymRef f;

    @Nonnull
    public final List<SymRef> args;

    @Nonnull
    public final Type returnedType;

    public KApp(SymRef f, List<SymRef> args, Type returnedType) {
        this.f = f;
        this.args = Collections.unmodifiableList(args);
        this.returnedType = returnedType;
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
        return returnedType;
    }

    @Nonnull
    public String toString() {
        return f.id + args;
    }
}
