package mini_camel.type;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * Represents a type variable (a type that is not known while parsing, but
 * will be assigned a concrete value by the type checker).
 */
@Immutable
public final class TVar extends Type {
    public final String v;

    public TVar(@Nonnull String v) {
        this.v = v;
    }

    public String toString() {
        return v;
    }
}
