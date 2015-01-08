package mini_camel.ast;

public interface Visitor2<RetT, ArgT>{
    RetT visit(ArgT a, AstUnit e);
    RetT visit(ArgT a, AstBool e);
    RetT visit(ArgT a, AstInt e);
    RetT visit(ArgT a, AstFloat e);
    RetT visit(ArgT a, AstNot e);
    RetT visit(ArgT a, AstNeg e);
    RetT visit(ArgT a, AstAdd e);
    RetT visit(ArgT a, AstSub e);
    RetT visit(ArgT a, AstFNeg e);
    RetT visit(ArgT a, AstFAdd e);
    RetT visit(ArgT a, AstFSub e);
    RetT visit(ArgT a, AstFMul e);
    RetT visit(ArgT a, AstFDiv e);
    RetT visit(ArgT a, AstEq e);
    RetT visit(ArgT a, AstLE e);
    RetT visit(ArgT a, AstIf e);
    RetT visit(ArgT a, AstLet e);
    RetT visit(ArgT a, AstVar e);
    RetT visit(ArgT a, AstLetRec e);
    RetT visit(ArgT a, AstApp e);
    RetT visit(ArgT a, AstTuple e);
    RetT visit(ArgT a, AstLetTuple e);
    RetT visit(ArgT a, AstArray e);
    RetT visit(ArgT a, AstGet e);
    RetT visit(ArgT a, AstPut e);
    RetT visit(ArgT a, AstFunDef e);
}


