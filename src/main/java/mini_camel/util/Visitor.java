package mini_camel.util;

import mini_camel.ast.*;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * A basic implementation of the "visitor" pattern, where each method
 * is an AST node and the return type of the {@code visit} method is
 * {@code void}.
 */
@ParametersAreNonnullByDefault
public interface Visitor {
    void visit(AstUnit e);
    void visit(AstBool e);
    void visit(AstInt e);
    void visit(AstFloat e);
    void visit(AstNot e);
    void visit(AstNeg e);
    void visit(AstAdd e);
    void visit(AstSub e);
    void visit(AstFNeg e);
    void visit(AstFAdd e);
    void visit(AstFSub e);
    void visit(AstFMul e);
    void visit(AstFDiv e);
    void visit(AstEq e);
    void visit(AstLE e);
    void visit(AstIf e);
    void visit(AstLet e);
    void visit(SymRef e);
    void visit(AstLetRec e);
    void visit(AstApp e);
    void visit(AstTuple e);
    void visit(AstLetTuple e);
    void visit(AstArray e);
    void visit(AstGet e);
    void visit(AstPut e);
    void visit(AstFunDef e);
    void visit(AstErr e);
}


