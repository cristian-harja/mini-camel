package mini_camel.visit;

import mini_camel.knorm.*;
import mini_camel.util.KVisitor;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class KDummyVisitor implements KVisitor {
    public void visit(KUnit k) {
        // do nothing
    }

    public void visit(KInt k) {
        // do nothing
    }

    public void visit(KFloat k) {
        // do nothing
    }

    public void visit(KVar k) {
        // do nothing
    }

    public void visit(KTuple k) {
        // do nothing
    }

    public void visit(KNeg k) {
        // do nothing
    }

    public void visit(KAdd k) {
        // do nothing
    }

    public void visit(KSub k) {
        // do nothing
    }

    public void visit(KFNeg k) {
        // do nothing
    }

    public void visit(KFAdd k) {
        // do nothing
    }

    public void visit(KFDiv k) {
        // do nothing
    }

    public void visit(KFMul k) {
        // do nothing
    }

    public void visit(KFSub k) {
        // do nothing
    }

    public void visit(KGet k) {
        // do nothing
    }

    public void visit(KPut k) {
        // do nothing
    }

    public void visit(KLet k) {
        k.initializer.accept(this);
        k.ret.accept(this);
    }

    public void visit(KLetRec k) {
        k.fd.body.accept(this);
        k.ret.accept(this);
    }

    public void visit(KLetTuple k) {
        k.ret.accept(this);
    }

    public void visit(KIfEq k) {
        k.kThen.accept(this);
        k.kElse.accept(this);
    }

    public void visit(KIfLE k) {
        k.kThen.accept(this);
        k.kElse.accept(this);
    }

    public void visit(KApp k) {
        // do nothing
    }

    public void visit(ApplyClosure k) {
        // do nothing
    }

    public void visit(ApplyDirect k) {
        // do nothing
    }

    public void visit(ClosureMake k) {
        // do nothing
    }
}
