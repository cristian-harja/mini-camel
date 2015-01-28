package mini_camel.type;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * Represents an array data type, where the elements are of a certain kind.
 */
@Immutable
public final class TArray extends Type {
    @Nonnull
    public final Type elementType;

    public TArray(Type t) {
        elementType = t;
    }

    @Nonnull
    public String toString() {
        return "[" + elementType + "]";
    }

    @Nonnull
    public Kind getKind() {
        return Kind.ARRAY;
    }
}