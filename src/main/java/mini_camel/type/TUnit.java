package mini_camel.type;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * Represents the "unit" or "void" or "()" data type. This is a singleton
 * class, so instead of using the constructor, just refer to {@link #INSTANCE}.
 */
@Immutable
public final class TUnit extends Type {

    public static final TUnit INSTANCE = new TUnit();

    // Immutable singleton class. Use TUnit.INSTANCE instead.
    private TUnit() {}

    @Nonnull
    public String toString() {
        return "()";
    }

    @Nonnull
    public Kind getKind() {
        return Kind.UNIT;
    }
}
