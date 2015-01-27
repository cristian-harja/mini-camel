package mini_camel.util;

import mini_camel.ast.*;

import javax.annotation.Nonnull;

/**
 * An implementation of the "visitor" pattern for AST nodes, using generics.
 * The {@code visit} method returns a value of type {@code RetT}.
 *
 * @param <RetT> The generic type of the returned value
 */
public interface Visitor1<RetT>{
    RetT visit(@Nonnull AstUnit e);
    RetT visit(@Nonnull AstBool e);
    RetT visit(@Nonnull AstInt e);
    RetT visit(@Nonnull AstFloat e);
    RetT visit(@Nonnull AstNot e);
    RetT visit(@Nonnull AstNeg e);
    RetT visit(@Nonnull AstAdd e);
    RetT visit(@Nonnull AstSub e);
    RetT visit(@Nonnull AstFNeg e);
    RetT visit(@Nonnull AstFAdd e);
    RetT visit(@Nonnull AstFSub e);
    RetT visit(@Nonnull AstFMul e);
    RetT visit(@Nonnull AstFDiv e);
    RetT visit(@Nonnull AstEq e);
    RetT visit(@Nonnull AstLE e);
    RetT visit(@Nonnull AstIf e);
    RetT visit(@Nonnull AstLet e);
    RetT visit(@Nonnull SymRef e);
    RetT visit(@Nonnull AstLetRec e);
    RetT visit(@Nonnull AstApp e);
    RetT visit(@Nonnull AstTuple e);
    RetT visit(@Nonnull AstLetTuple e);
    RetT visit(@Nonnull AstArray e);
    RetT visit(@Nonnull AstGet e);
    RetT visit(@Nonnull AstPut e);
    RetT visit(@Nonnull AstFunDef e);
    RetT visit(@Nonnull AstErr e);
}


