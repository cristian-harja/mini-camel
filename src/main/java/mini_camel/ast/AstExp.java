package mini_camel.ast;

import mini_camel.visit.*;

import javax.annotation.Nonnull;

/**
 * Base class for all (or most of the) AST nodes.
 */
public abstract class AstExp extends AstNode {

    /**
     * Implements part of the "visitor" design pattern. Calls the appropriate
     * version of {@link mini_camel.visit.Visitor}{@code .visit()}, depending on the type of
     * the current AST node.
     */
    public abstract void accept(@Nonnull Visitor v);

    /**
     * Implements part of the "visitor" design pattern, only using the
     * alternative {@link mini_camel.visit.Visitor1} interface, which also allows specifying
     * a return value (of type {@code T}).
     */
    public abstract <T> T accept(@Nonnull Visitor1<T> v);

    /**
     * Implements part of the "visitor" design pattern, only using the
     * alternative {@link mini_camel.visit.Visitor2} interface, which also allows specifying
     * an argument ({@code arg} of type {@code U}} and a return value (of
     * type {@code T}).
     */
    public abstract <T, U> T accept(@Nonnull Visitor2<T, U> v, U arg);

    /**
     * Implements the "visitor" design pattern for K-normalized code.
     * Calls the appropriate version of {@link VisitorK}{@code .visit*()},
     * depending on the type of the current AST node.
     *
     * @throws RuntimeException When the AST is not K-normalized. The
     *         exception is raised explicitly when an AST node was found
     *         which shouldn't even exist if it were K-normalized. It is
     *         raised implicitly (as a {@link ClassCastException}) when
     *         the overriding {@code accept} method casts its children
     *         to their expected types (without checking them first) and
     *         they do not correspond.
     */
    public <T, U> T accept(@Nonnull VisitorK<T, U> v, U arg) {
        throw new RuntimeException("Unexpected " +
                getClass().getCanonicalName() +
                " in a (supposedly) K-normalized AST"
        );
    }

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
