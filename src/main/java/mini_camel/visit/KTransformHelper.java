package mini_camel.visit;

import mini_camel.knorm.*;
import mini_camel.util.KVisitor1;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Counterpart of {@link TransformHelper}, for K-normalized code.
 */
@ParametersAreNonnullByDefault
public abstract class KTransformHelper implements KVisitor1<KNode> {

    @Nonnull
    public KNode visit(KUnit k) {
        return k;
    }

    @Nonnull
    public KNode visit(KInt k) {
        return k;
    }

    @Nonnull
    public KNode visit(KFloat k) {
        return k;
    }

    @Nonnull
    public KNode visit(KVar k) {
        return k;
    }

    @Nonnull
    public KNode visit(KTuple k) {
        return k;
    }

    @Nonnull
    public KNode visit(KArray k) {
        return k;
    }

    @Nonnull
    public KNode visit(KNeg k) {
        return k;
    }

    @Nonnull
    public KNode visit(KAdd k) {
        return k;
    }

    @Nonnull
    public KNode visit(KSub k) {
        return k;
    }

    @Nonnull
    public KNode visit(KFNeg k) {
        return k;
    }

    @Nonnull
    public KNode visit(KFAdd k) {
        return k;
    }

    @Nonnull
    public KNode visit(KFDiv k) {
        return k;
    }

    @Nonnull
    public KNode visit(KFMul k) {
        return k;
    }

    @Nonnull
    public KNode visit(KFSub k) {
        return k;
    }

    @Nonnull
    public KNode visit(KGet k) {
        return k;
    }

    @Nonnull
    public KNode visit(KPut k) {
        return k;
    }

    @Nonnull
    public KNode visit(KLet k) {
        KNode new_init = k.initializer.accept(this);
        KNode new_ret = k.ret.accept(this);
        if (new_init == k.initializer && new_ret == k.ret) return k;
        return new KLet(k.id, new_init, new_ret);
    }

    @Nonnull
    public KNode visit(KLetRec k) {
        KNode new_body = k.fd.body.accept(this);
        KNode new_ret = k.ret.accept(this);
        KFunDef new_fd = ((new_body == k.fd.body)
                ? new KFunDef(k.fd.name, k.fd.args, new_body)
                : k.fd
        );
        if (new_fd == k.fd && new_ret == k.ret) return k;
        return new KLetRec(new_fd, new_ret);
    }

    @Nonnull
    public KNode visit(KLetTuple k) {
        KNode new_ret = k.ret.accept(this);
        if (new_ret == k.ret) return k;
        return new KLetTuple(k.ids, k.initializer, new_ret);
    }

    @Nonnull
    public KNode visit(KIfEq k) {
        KNode new_then = k.kThen.accept(this);
        KNode new_else = k.kElse.accept(this);
        if (new_then == k.kThen && new_else == k.kElse) return k;
        return new KIfEq(k.op1, k.op2, new_then, new_else);
    }

    @Nonnull
    public KNode visit(KIfLE k) {
        KNode new_then = k.kThen.accept(this);
        KNode new_else = k.kElse.accept(this);
        if (new_then == k.kThen && new_else == k.kElse) return k;
        return new KIfLE(k.op1, k.op2, new_then, new_else);
    }

    @Nonnull
    public KNode visit(KApp k) {
        return k;
    }

    @Nonnull
    public KNode visit(ApplyClosure k) {
        return k;
    }

    @Nonnull
    public KNode visit(ApplyDirect k) {
        return k;
    }

    @Nonnull
    public KNode visit(ClosureMake k) {
        return k;
    }
}
