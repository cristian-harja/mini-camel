package mini_camel.ast;

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
    void visit(AstVar e);
    void visit(AstLetRec e);
    void visit(AstApp e);
    void visit(AstTuple e);
    void visit(AstLetTuple e);
    void visit(AstArray e);
    void visit(AstGet e);
    void visit(AstPut e);
    void visit(AstFunDef e);
}


