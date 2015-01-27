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
public final class KIfEq extends KNode {
    @Nonnull
    public final SymRef op1, op2;

    @Nonnull
    public final KNode kThen, kElse;

    public KIfEq(SymRef op1, SymRef op2, KNode kThen, KNode kElse) {
        this.op1 = op1;
        this.op2 = op2;
        this.kThen = kThen;
        this.kElse = kElse;
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
        return kThen.getDataType(); // same as kElse.getDataType()
    }

    @Nonnull
    public String toString() {
        return String.format(
                "if (%s = %s) then %s else %s",
                op1.id, op2.id,
                kThen.toString(),
                kElse.toString()
        );
    }

}
