package mini_camel.util;

import mini_camel.ast.*;

import javax.annotation.Nonnull;

/**
 * A basic implementation of the "visitor" pattern, where each method
 * is an AST node and the return type of the {@code visit} method is
 * {@code void}.
 */
public interface Visitor {
    void visit(@Nonnull AstUnit e);
    void visit(@Nonnull AstBool e);
    void visit(@Nonnull AstInt e);
    void visit(@Nonnull AstFloat e);
    void visit(@Nonnull AstNot e);
    void visit(@Nonnull AstNeg e);
    void visit(@Nonnull AstAdd e);
    void visit(@Nonnull AstSub e);
    void visit(@Nonnull AstFNeg e);
    void visit(@Nonnull AstFAdd e);
    void visit(@Nonnull AstFSub e);
    void visit(@Nonnull AstFMul e);
    void visit(@Nonnull AstFDiv e);
    void visit(@Nonnull AstEq e);
    void visit(@Nonnull AstLE e);
    void visit(@Nonnull AstIf e);
    void visit(@Nonnull AstLet e);
    void visit(@Nonnull SymRef e);
    void visit(@Nonnull AstLetRec e);
    void visit(@Nonnull AstApp e);
    void visit(@Nonnull AstTuple e);
    void visit(@Nonnull AstLetTuple e);
    void visit(@Nonnull AstArray e);
    void visit(@Nonnull AstGet e);
    void visit(@Nonnull AstPut e);
    void visit(@Nonnull AstFunDef e);
    void visit(@Nonnull AstErr e);
}


