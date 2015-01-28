package mini_camel.util;

import mini_camel.knorm.*;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface KVisitor1<T> {
    // Values
    T visit(KUnit k);
    T visit(KInt k);
    T visit(KFloat k);
    T visit(KVar k);
    T visit(KTuple k);

    // Integer operations
    T visit(KNeg k);
    T visit(KAdd k);
    T visit(KSub k);

    // Floating point operations
    T visit(KFNeg k);
    T visit(KFAdd k);
    T visit(KFDiv k);
    T visit(KFMul k);
    T visit(KFSub k);

    // Array operations
    T visit(KGet k);
    T visit(KPut k);

    // Let
    T visit(KLet k);
    T visit(KLetRec k);
    T visit(KLetTuple k);

    // Branch
    T visit(KIfEq k);
    T visit(KIfLE k);

    // Calling functions (only used in K-normalized code)
    T visit(KApp k);

    // Calling functions (after closure-converting the K-normalized code)
    T visit(ApplyClosure k);
    T visit(ApplyDirect k);
    T visit(ClosureMake k);

}
