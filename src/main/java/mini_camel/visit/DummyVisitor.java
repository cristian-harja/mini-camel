package mini_camel.visit;

import mini_camel.ast.*;
import mini_camel.util.SymRef;
import mini_camel.util.Visitor;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * An implementation of the {@link mini_camel.util.Visitor} interface that just traverses the
 * AST recursively. Intended for use as a base class; you should override some
 * methods in order to only handle certain node types in your sub-class, while
 * ignoring the others.
 */
@ParametersAreNonnullByDefault
public abstract class DummyVisitor implements Visitor {

    public void visit(AstUnit e) {
        // do nothing
    }

    public void visit(AstBool e) {
        // do nothing
    }

    public void visit(AstInt e) {
        // do nothing
    }

    public void visit(AstFloat e) {
        // do nothing
    }

    public void visit(SymRef e) {
        // do nothing
    }

    public void visit(AstErr e) {
        // do nothing
    }

    public void visit(AstNot e) {
        e.e.accept(this);
    }

    public void visit(AstNeg e) {
        e.e.accept(this);
    }

    public void visit(AstFNeg e) {
        e.e.accept(this);
    }

    public void visit(AstAdd e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    public void visit(AstSub e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    public void visit(AstFAdd e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    public void visit(AstFSub e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    public void visit(AstFMul e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    public void visit(AstFDiv e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    public void visit(AstEq e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    public void visit(AstLE e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    public void visit(AstIf e) {
        e.eCond.accept(this);
        e.eThen.accept(this);
        e.eElse.accept(this);
    }

    public void visit(AstLet e) {
        e.initializer.accept(this);
        e.ret.accept(this);
    }

    public void visit(AstLetRec e) {
        e.fd.accept(this);
        e.ret.accept(this);
    }

    public void visit(AstApp e) {
        e.e.accept(this);
        for (AstExp arg : e.es) {
            arg.accept(this);
        }
    }

    public void visit(AstTuple e) {
        for (AstExp arg : e.es) {
            arg.accept(this);
        }
    }

    public void visit(AstLetTuple e) {
        e.initializer.accept(this);
        e.ret.accept(this);
    }

    public void visit(AstArray e) {
        e.size.accept(this);
        e.initializer.accept(this);
    }

    public void visit(AstGet e) {
        e.array.accept(this);
        e.index.accept(this);
    }

    public void visit(AstPut e) {
        e.array.accept(this);
        e.index.accept(this);
        e.value.accept(this);
    }

    public void visit(AstFunDef e) {
        e.body.accept(this);
    }
}
