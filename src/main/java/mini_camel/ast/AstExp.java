package mini_camel.ast;

import mini_camel.visit.Visitor;
import mini_camel.visit.Visitor1;
import mini_camel.visit.Visitor2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Base class for all (or most of the) AST nodes.
 */
public abstract class AstExp extends AstNode {

    /**
     * Implements part of the "visitor" design pattern. Calls the appropriate
     * version of {@link Visitor}{@code .visit()}, depending on the type of
     * the current AST node.
     *
     * @param v The {@code Visitor} object that needs to examine this object.
     */
    public abstract void accept(Visitor v);

    /**
     * Implements part of the "visitor" design pattern, but using the
     * alternative {@link Visitor1} interface, which also allows specifying
     * a return value (of type {@code T}).
     *
     * @param v   The {@code Visitor1} that needs to examine this object.
     * @param <T> Return type of the {@code .visit()} method.
     * @return The same value returned by the called {@code visit()} method.
     */
    public abstract <T> T accept(Visitor1<T> v);

    /**
     * Implements part of the "visitor" design pattern, but using the
     * alternative {@link Visitor2} interface, which also allows specifying
     * an argument ({@code arg} of type {@code U}) and a return value (of
     * type {@code T}).
     *
     * @param v   The {@code Visitor2} that needs to examine this object.
     * @param a   A user-defined argument to serve as context.
     * @param <T> Return type of the {@code .visit()} method.
     * @param <U> Type of the user-supplied argument to {@code .visit()}.
     * @return The same value returned by the called {@code visit()} method.
     */
    public abstract <T, U> T accept(Visitor2<T, U> v, @Nullable U a);
}
