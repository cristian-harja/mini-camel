package mini_camel.visit;

import mini_camel.ast.*;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public final class FunctionSize extends DummyVisitor {

    private int numberOfOperation;

    private FunctionSize() {
    }

    public static int compute(AstExp astNode) {
        FunctionSize fs = new FunctionSize();
        astNode.accept(fs);
        return fs.numberOfOperation;
    }

    public void visit(AstSub e) {
        numberOfOperation ++;
        e.e1.accept(this);
        e.e2.accept(this);
    }
    public void visit(AstFSub e) {
        numberOfOperation ++;
        e.e1.accept(this);
        e.e2.accept(this);
    }

    public void visit(AstAdd e) {
        numberOfOperation ++;
        e.e1.accept(this);
        e.e2.accept(this);
    }

    public void visit(AstFMul e) {
        numberOfOperation ++;
        e.e1.accept(this);
        e.e2.accept(this);
    }

    public void visit(AstFDiv e) {
        numberOfOperation ++;
        e.e1.accept(this);
        e.e2.accept(this);
    }

    public void visit(AstEq e) {
        numberOfOperation ++;
        e.e1.accept(this);
        e.e2.accept(this);
    }

    public void visit(AstLet e) {
        numberOfOperation ++;
        e.ret.accept(this);
    }

}
