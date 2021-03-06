package mini_camel.knorm;

import mini_camel.type.TFloat;
import mini_camel.type.Type;
import mini_camel.util.KVisitor;
import mini_camel.util.KVisitor1;
import mini_camel.util.KVisitor2;
import mini_camel.util.SymRef;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class KFDiv extends KNode {
    @Nonnull
    public final SymRef op1, op2;

    public KFDiv(SymRef op1, SymRef op2) {
        this.op1 = op1;
        this.op2 = op2;
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
        return op1 + " /. " + op2;
    }

}
