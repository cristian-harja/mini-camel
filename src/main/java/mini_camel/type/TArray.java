package mini_camel.type;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * Represents an array data type, where the elements are of a certain kind.
 */
@Immutable
public final class TArray extends Type {
    public final Type elementType;

    public TArray(@Nonnull Type t) {
        elementType = t;
    }

    @Override
    public String toString() {
        return "[" + elementType + "]";
    }
}