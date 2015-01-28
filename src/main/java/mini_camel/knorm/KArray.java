package mini_camel.knorm;

import mini_camel.type.TArray;
import mini_camel.type.Type;
import mini_camel.util.KVisitor;
import mini_camel.util.KVisitor1;
import mini_camel.util.KVisitor2;
import mini_camel.util.SymRef;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class KArray extends KNode {
    @Nonnull
    public final SymRef size;

    @Nonnull
    public final SymRef initialValue;

    @Nonnull
    public final Type elementType;

    @Nonnull
    public final Type arrayType;

    public KArray(SymRef size, SymRef initialValue, Type elementType) {
        this.size = size;
        this.initialValue = initialValue;
        this.elementType = elementType;
        this.arrayType = new TArray(elementType);
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
        return arrayType;
    }

    @Nonnull
    public String toString() {
        return String.format(
                "Array.Create[%s](%s)",
                size, initialValue
        );
    }
}
