package mini_camel.util;

import mini_camel.ast.*;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * An implementation of the "visitor" pattern for AST nodes, using generics.
 * The {@code visit} method returns a value of type {@code RetT}.
 *
 * @param <RetT> The generic type of the returned value
 */
@ParametersAreNonnullByDefault
public interface Visitor1<RetT>{
    RetT visit(AstUnit e);
    RetT visit(AstBool e);
    RetT visit(AstInt e);
    RetT visit(AstFloat e);
    RetT visit(AstNot e);
    RetT visit(AstNeg e);
    RetT visit(AstAdd e);
    RetT visit(AstSub e);
    RetT visit(AstFNeg e);
    RetT visit(AstFAdd e);
    RetT visit(AstFSub e);
    RetT visit(AstFMul e);
    RetT visit(AstFDiv e);
    RetT visit(AstEq e);
    RetT visit(AstLE e);
    RetT visit(AstIf e);
    RetT visit(AstLet e);
    RetT visit(SymRef e);
    RetT visit(AstLetRec e);
    RetT visit(AstApp e);
    RetT visit(AstTuple e);
    RetT visit(AstLetTuple e);
    RetT visit(AstArray e);
    RetT visit(AstGet e);
    RetT visit(AstPut e);
    RetT visit(AstFunDef e);
    RetT visit(AstErr e);
}


