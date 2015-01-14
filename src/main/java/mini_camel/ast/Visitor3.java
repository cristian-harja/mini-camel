package mini_camel.ast;


import mini_camel.ir.Couple;

import javax.annotation.Nonnull;

/**
 * A implementation of the "visitor" pattern for AST nodes, using generics.
 * The {@code visit} method returns a value of type {@code Couple}, and also
 * takes an extra (user-defined) argument of type {@code ArgT}.
 *
 */
public interface Visitor3{
    Couple visit( AstUnit e);
    Couple visit( AstBool e);
    Couple visit( AstInt e);
    Couple visit( AstFloat e);
    Couple visit( AstNot e);
    Couple visit( AstNeg e);
    Couple visit( AstAdd e);
    Couple visit( AstSub e);
    Couple visit( AstFNeg e);
    Couple visit( AstFAdd e);
    Couple visit( AstFSub e);
    Couple visit( AstFMul e);
    Couple visit( AstFDiv e);
    Couple visit( AstEq e);
    Couple visit( AstLE e);
    Couple visit( AstIf e);
    Couple visit( AstLet e);
    Couple visit( AstVar e);
    Couple visit( AstLetRec e);
    Couple visit( AstApp e);
    Couple visit( AstTuple e);
    Couple visit( AstLetTuple e);
    Couple visit( AstArray e);
    Couple visit( AstGet e);
    Couple visit( AstPut e);
    Couple visit( AstFunDef e);
    
}




