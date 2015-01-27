package mini_camel.visit;

import mini_camel.ast.*;
import mini_camel.util.SymRef;
import mini_camel.util.Visitor;

import javax.annotation.Nonnull;

/**
 * An implementation of the {@link mini_camel.util.Visitor} interface that just traverses the
 * AST recursively. Intended for use as a base class; you should override some
 * methods in order to only handle certain node types in your sub-class, while
 * ignoring the others.
 */
public abstract class DummyVisitor implements Visitor {

    public void visit(@Nonnull AstUnit e) {
        // do nothing
    }

    @Override
    public void visit(@Nonnull AstBool e) {
        // do nothing
    }

    @Override
    public void visit(@Nonnull AstInt e) {
        // do nothing
    }

    @Override
    public void visit(@Nonnull AstFloat e) {
        // do nothing
    }

    @Override
    public void visit(@Nonnull SymRef e) {
        // do nothing
    }

    @Override
    public void visit(@Nonnull AstErr e) {
        // do nothing
    }

    @Override
    public void visit(@Nonnull AstNot e) {
        e.e.accept(this);
    }

    @Override
    public void visit(@Nonnull AstNeg e) {
        e.e.accept(this);
    }

    @Override
    public void visit(@Nonnull AstFNeg e) {
        e.e.accept(this);
    }

    @Override
    public void visit(@Nonnull AstAdd e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(@Nonnull AstSub e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(@Nonnull AstFAdd e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(@Nonnull AstFSub e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(@Nonnull AstFMul e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(@Nonnull AstFDiv e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(@Nonnull AstEq e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(@Nonnull AstLE e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(@Nonnull AstIf e) {
        e.eCond.accept(this);
        e.eThen.accept(this);
        e.eElse.accept(this);
    }

    @Override
    public void visit(@Nonnull AstLet e) {
        e.initializer.accept(this);
        e.ret.accept(this);
    }

    @Override
    public void visit(@Nonnull AstLetRec e) {
        e.fd.accept(this);
        e.ret.accept(this);
    }

    @Override
    public void visit(@Nonnull AstApp e) {
        e.e.accept(this);
        for (AstExp arg : e.es) {
            arg.accept(this);
        }
    }

    @Override
    public void visit(@Nonnull AstTuple e) {
        for (AstExp arg : e.es) {
            arg.accept(this);
        }
    }

    @Override
    public void visit(@Nonnull AstLetTuple e) {
        e.initializer.accept(this);
        e.ret.accept(this);
    }

    @Override
    public void visit(@Nonnull AstArray e) {
        e.size.accept(this);
        e.initializer.accept(this);
    }

    @Override
    public void visit(@Nonnull AstGet e) {
        e.array.accept(this);
        e.index.accept(this);
    }

    @Override
    public void visit(@Nonnull AstPut e) {
        e.array.accept(this);
        e.index.accept(this);
        e.value.accept(this);
    }

    @Override
    public void visit(@Nonnull AstFunDef e) {
        e.body.accept(this);
    }
}
