package mini_camel.ast;

public abstract class DummyVisitor implements Visitor {

    public void visit(AstUnit e) {
        // do nothing
    }

    @Override
    public void visit(AstBool e) {
        // do nothing
    }

    @Override
    public void visit(AstInt e) {
        // do nothing
    }

    @Override
    public void visit(AstFloat e) {
        // do nothing
    }

    @Override
    public void visit(AstVar e) {
        // do nothing
    }

    @Override
    public void visit(AstNot e) {
        e.e.accept(this);
    }

    @Override
    public void visit(AstNeg e) {
        e.e.accept(this);
    }

    @Override
    public void visit(AstFNeg e) {
        e.e.accept(this);
    }

    @Override
    public void visit(AstAdd e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(AstSub e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(AstFAdd e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(AstFSub e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(AstFMul e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(AstFDiv e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(AstEq e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(AstLE e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(AstIf e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(AstLet e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(AstLetRec e) {
        e.fd.accept(this);
        e.e.accept(this);
    }

    @Override
    public void visit(AstApp e) {
        e.e.accept(this);
        for (AstExp arg : e.es) {
            arg.accept(this);
        }
    }

    @Override
    public void visit(AstTuple e) {
        for (AstExp arg : e.es) {
            arg.accept(this);
        }
    }

    @Override
    public void visit(AstLetTuple e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(AstArray e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(AstGet e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(AstPut e) {
        e.e1.accept(this);
        e.e2.accept(this);
        e.e3.accept(this);
    }

    @Override
    public void visit(AstFunDef e) {
        e.e.accept(this);
    }
}
