package mini_camel.type;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * Represents the integer data type. This is a singleton class, so instead of
 * using the constructor, just refer to {@link #INSTANCE}.
 */
@Immutable
public final class TInt extends Type {

    public static final TInt INSTANCE = new TInt();

    // Immutable singleton class. Use TInt.INSTANCE instead.
    private TInt() {}

    @Nonnull
    public String toString() {
        return "int";
    }

    @Nonnull
    public Kind getKind() {
        return Kind.INTEGER;
    }
}
