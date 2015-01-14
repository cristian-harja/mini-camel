package mini_camel.type;

import javax.annotation.concurrent.Immutable;

/**
 * Represents the boolean data type. This is a singleton class, so instead of
 * using the constructor, just refer to {@link #INSTANCE}.
 */
@Immutable
public final class TBool extends Type {

    public static final TBool INSTANCE = new TBool();

    // Immutable singleton class. Use TBool.INSTANCE instead.
    private TBool() {}

    @Override
    public String toString() {
        return "bool";
    }
}