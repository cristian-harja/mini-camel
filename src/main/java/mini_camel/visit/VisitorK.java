package mini_camel.visit;

import mini_camel.ast.*;
import mini_camel.type.Type;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * <p><b>Assuming the AST is K-normalized</b>, this interface helps implement
 * the "visitor" pattern, but with a twist.
 * </p>
 * <p>Some AST nodes (such as {@link mini_camel.ast.AstAdd} for example) declare their
 * children as being of type {@link mini_camel.ast.AstExp}, which can be really anything.
 * This allows for arbitrarily complex expressions in the source code.
 * </p>
 * <pHowever, after K-normalization, the AST gets a very specific structure,
 * where some AST nodes always have children of a very specific type. For
 * example, an {@code AstAdd}, after K-normalization, will have its both
 * children of type {@link mini_camel.ast.AstVar}, instead of the generic {@code AstExp}.
 * </p>
 * <p>In order to represent the normalized ASTs in a type-safe way, we could
 * have created a new set of classes (mostly identical to the set of "Ast*"
 * classes), but with some fields having slightly different types. We tried
 * to avoid this and came up with the {@code VisitorK} interface, which
 * <b>assuming an AST is K-normalized</b>, implements the {@code visit()}
 * methods in such a way that the children which are guaranteed to have a
 * certain type are exposed as arguments of these methods.
 * </p>
 * <p>For example, the {@link #visit(ArgT, mini_camel.ast.AstAdd, mini_camel.ast.Id, mini_camel.ast.Id) visit} method
 * corresponding to the {@code AstAdd} class has two extra parameters, which
 * will contain the two operands of the addition, but cast to their actual
 * type.
 * </p>
 * <p>The casts performed by the {@link mini_camel.ast.AstExp#accept(VisitorK, ArgT)} method
 * in each AST node are safe <p>if and only if the AST was K-normalized before
 * attempting to visit it</p>. Since the child nodes will simply be cast to
 * their expected types, attempting to visit an incorrect AST will yield a
 * {@link ClassCastException}.
 * </p>
 * <p>A lot of the {@code visit} methods in this class only have the two
 * "standard" arguments, because there are no special expectations for the
 * data type of the child nodes.
 * </p>
 */
public interface VisitorK<RetT, ArgT> {

    RetT visit(ArgT a, @Nonnull AstUnit e);

    RetT visit(ArgT a, @Nonnull AstInt e);

    RetT visit(ArgT a, @Nonnull AstFloat e);

    RetT visit(ArgT a, @Nonnull AstLet e);

    RetT visit(ArgT a, @Nonnull AstVar e);

    RetT visit(ArgT a, @Nonnull AstLetRec e);

    RetT visit(ArgT a, @Nonnull AstFunDef fd);

    RetT visit(
            ArgT a, @Nonnull AstNeg e,
            @Nonnull Id id
    );

    RetT visit(
            ArgT a, @Nonnull AstAdd e,
            @Nonnull Id op1,
            @Nonnull Id op2
    );

    RetT visit(
            ArgT a, @Nonnull AstSub e,
            @Nonnull Id op1,
            @Nonnull Id op2
    );

    RetT visit(
            ArgT a, @Nonnull AstFNeg e,
            @Nonnull Id id
    );

    RetT visit(
            ArgT a, @Nonnull AstFAdd e,
            @Nonnull Id op1,
            @Nonnull Id op2
    );

    RetT visit(
            ArgT a, @Nonnull AstFSub e,
            @Nonnull Id op1,
            @Nonnull Id op2
    );

    RetT visit(
            ArgT a, @Nonnull AstFMul e,
            @Nonnull Id op1,
            @Nonnull Id op2
    );

    RetT visit(
            ArgT a, @Nonnull AstFDiv e,
            @Nonnull Id op1,
            @Nonnull Id op2
    );

    /**
     * Mimics the existence of an AST node for `if` statements that only
     * check the equality of two expressions (no other boolean expressions are
     * allowed in K-normal form).
     */
    RetT visitIfEq(
            ArgT a, @Nonnull AstIf e,
            @Nonnull Id op1,
            @Nonnull Id op2,
            @Nonnull AstExp thenBranch,
            @Nonnull AstExp elseBranch
    );

    /**
     * Mimics the existence of an AST node for `if` statements that only
     * check the inequality of two expressions (no other boolean expressions
     * are allowed in K-normal form).
     */
    RetT visitIfLE(
            ArgT a, @Nonnull AstIf e,
            @Nonnull Id op1,
            @Nonnull Id op2,
            @Nonnull AstExp thenBranch,
            @Nonnull AstExp elseBranch
    );

    RetT visit(
            ArgT a, @Nonnull AstApp e,
            @Nonnull Id function,
            @Nonnull List<Id> args
    );

    RetT visit(
            ArgT a, @Nonnull AstTuple e,
            @Nonnull List<Id> items
    );

    RetT visit(
            ArgT a, @Nonnull AstLetTuple e,
            @Nonnull List<Id> id,
            @Nonnull List<Type> t,
            @Nonnull Id value,
            @Nonnull AstExp body
    );

    RetT visit(
            ArgT a, @Nonnull AstArray e,
            @Nonnull Id initialValue,
            @Nonnull Id numElements
    );

    RetT visit(
            ArgT a, @Nonnull AstGet e,
            @Nonnull Id array,
            @Nonnull Id index
    );

    RetT visit(
            ArgT a, @Nonnull AstPut e,
            @Nonnull Id array,
            @Nonnull Id index,
            @Nonnull Id value
    );

}
