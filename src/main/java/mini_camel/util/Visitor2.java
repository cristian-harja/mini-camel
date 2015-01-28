package mini_camel.util;

import mini_camel.ast.*;

import javax.annotation.Nonnull;

/**
 * An implementation of the "visitor" pattern for AST nodes, using generics.
 * The {@code visit} method returns a value of type {@code RetT}, and also
 * takes an extra (user-defined) argument of type {@code ArgT}.
 *
 * @param <RetT> The generic type of the returned value
 * @param <ArgT> The generic type of the first argument.
 */
public interface Visitor2<RetT, ArgT>{
    RetT visit(ArgT a, @Nonnull AstUnit e);
    RetT visit(ArgT a, @Nonnull AstBool e);
    RetT visit(ArgT a, @Nonnull AstInt e);
    RetT visit(ArgT a, @Nonnull AstFloat e);
    RetT visit(ArgT a, @Nonnull AstNot e);
    RetT visit(ArgT a, @Nonnull AstNeg e);
    RetT visit(ArgT a, @Nonnull AstAdd e);
    RetT visit(ArgT a, @Nonnull AstSub e);
    RetT visit(ArgT a, @Nonnull AstFNeg e);
    RetT visit(ArgT a, @Nonnull AstFAdd e);
    RetT visit(ArgT a, @Nonnull AstFSub e);
    RetT visit(ArgT a, @Nonnull AstFMul e);
    RetT visit(ArgT a, @Nonnull AstFDiv e);
    RetT visit(ArgT a, @Nonnull AstEq e);
    RetT visit(ArgT a, @Nonnull AstLE e);
    RetT visit(ArgT a, @Nonnull AstIf e);
    RetT visit(ArgT a, @Nonnull AstLet e);
    RetT visit(ArgT a, @Nonnull SymRef e);
    RetT visit(ArgT a, @Nonnull AstLetRec e);
    RetT visit(ArgT a, @Nonnull AstApp e);
    RetT visit(ArgT a, @Nonnull AstTuple e);
    RetT visit(ArgT a, @Nonnull AstLetTuple e);
    RetT visit(ArgT a, @Nonnull AstArray e);
    RetT visit(ArgT a, @Nonnull AstGet e);
    RetT visit(ArgT a, @Nonnull AstPut e);
    RetT visit(ArgT a, @Nonnull AstFunDef e);
    RetT visit(ArgT a, @Nonnull AstErr e);
}


