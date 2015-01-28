package mini_camel.type;

import javax.annotation.Nonnull;

public abstract class Type {
    private static int x = 0;

    public enum Kind {
        VARIABLE,
        UNIT,
        BOOL,
        INTEGER,
        FLOAT,
        ARRAY,
        FUNCTION,
        TUPLE
    }

    @Nonnull
    public static Type gen() {
        return new TVar("t" + x++);
    }

    @Nonnull
    public abstract String toString();

    @Nonnull
    public abstract Kind getKind();
}
