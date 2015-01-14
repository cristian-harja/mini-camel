package mini_camel.ast;

import mini_camel.ir.Couple;

import javax.annotation.Nonnull;

/**
 * Base class for all (or most of the) AST nodes.
 */
public abstract class AstExp extends AstNode {

    /**
     * Implements part of the "visitor" design pattern. Calls the appropriate
     * version of {@link Visitor}{@code .visit()}, depending on the type of
     * the current AST node.
     */
    public abstract void accept(@Nonnull Visitor v);

    /**
     * Implements part of the "visitor" design pattern. Calls the appropriate
     * version of {@link Visitor}{@code .visit()}, depending on the type of
     * the current AST node.
     */
    public abstract Couple accept(@Nonnull Visitor3 v);

    /**
     * Implements part of the "visitor" design pattern, only using the
     * alternative {@link Visitor2} interface, which also allows specifying
     * a argument ({@code arg} of type {@code U}} and a return value (of type
     * {@code T}).
     */
    public abstract <T, U> T accept(@Nonnull Visitor2<T, U> v, U arg);

    /**
     * <p>For debugging purposes; returns a string representation of the AST
     * node (by reproducing the source code that let to this syntax tree).
     * </p>
     * <p>The graphical debugger that we are using displays the result of
     * {@code .toString} next to each variable, so this makes it easier for
     * us to understand the code represented by each AST object.
     * </p>
     */
    public abstract String toString();
}
