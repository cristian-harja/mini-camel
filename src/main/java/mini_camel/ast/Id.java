package mini_camel.ast;

import javax.annotation.Nonnull;

/**
 * Represents an identifier in the source code.
 */
public final class Id extends AstNode {
    public final String id;

    public Id(@Nonnull String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }

    static int x = -1;

    static public Id gen() {
        x++;
        return new Id("?v" + x);
    }
}