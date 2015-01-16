package mini_camel.type;

import javax.annotation.Nonnull;

public abstract class Type {
    private static int x = 0;

    @Nonnull
    public static Type gen() {
        return new TVar("t" + x++);
    }
    
}
