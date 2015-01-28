package mini_camel.util;

import mini_camel.ast.AstNode;

import javax.annotation.Nonnull;

/**
 * <p>Represents an identifier in the source code; this class is mostly used
 * by the parser because it carries the name of the identifier but might also
 * carry a {@link ldf.java_cup.runtime.Symbol} object, which holds information
 * about its exact position in the source code.
 * </p>
 */
public final class Id extends AstNode {
    public final String id;

    public Id(String id) {
        this.id = id;
    }

    @Nonnull
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
