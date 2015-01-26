package mini_camel.transform;

import mini_camel.ast.*;

import javax.annotation.Nonnull;

/**
 * Created by lina on 1/21/15.
 */
public class NumberOperation extends DummyVisitor {

    private int numberOfOperation;

    public NumberOperation() {
        numberOfOperation = 0;
    }

    public int applyTransform(@Nonnull AstExp astNode) {
        astNode.accept(this);
        return numberOfOperation;
    }

    @Override
    public void visit(@Nonnull AstSub e) {
        numberOfOperation ++;
        e.e1.accept(this);
        e.e2.accept(this);
    }
    @Override
    public void visit(@Nonnull AstFSub e) {
        numberOfOperation ++;
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(@Nonnull AstAdd e) {
        numberOfOperation ++;
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(@Nonnull AstFMul e) {
        numberOfOperation ++;
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(@Nonnull AstFDiv e) {
        numberOfOperation ++;
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(@Nonnull AstEq e) {
        numberOfOperation ++;
        e.e1.accept(this);
        e.e2.accept(this);
    }
    @Override
    public void visit(@Nonnull AstLet e) {
        numberOfOperation ++;
        e.e2.accept(this);
    }

}
