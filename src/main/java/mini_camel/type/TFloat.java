package mini_camel.type;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * Represents the "float" data type.
 */
@Immutable
public final class TFloat extends Type {

    public static final TFloat INSTANCE = new TFloat();

    // Immutable singleton class. Use TFloat.INSTANCE instead.
    private TFloat() {}

    @Nonnull
    public String toString() {
        return "float";
    }

    @Nonnull
    public Kind getKind() {
        return Kind.FLOAT;
    }
}
