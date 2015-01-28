package mini_camel.util;

import mini_camel.knorm.*;

import javax.annotation.Nonnull;

public interface KVisitor2<T, U> {
    // Values
    T visit(U a, @Nonnull KUnit k);
    T visit(U a, @Nonnull KInt k);
    T visit(U a, @Nonnull KFloat k);
    T visit(U a, @Nonnull KVar k);
    T visit(U a, @Nonnull KTuple k);
    T visit(U a, @Nonnull KArray k);

    // Integer operations
    T visit(U a, @Nonnull KNeg k);
    T visit(U a, @Nonnull KAdd k);
    T visit(U a, @Nonnull KSub k);

    // Floating point operations
    T visit(U a, @Nonnull KFNeg k);
    T visit(U a, @Nonnull KFAdd k);
    T visit(U a, @Nonnull KFDiv k);
    T visit(U a, @Nonnull KFMul k);
    T visit(U a, @Nonnull KFSub k);

    // Array operations
    T visit(U a, @Nonnull KGet k);
    T visit(U a, @Nonnull KPut k);

    // Let
    T visit(U a, @Nonnull KLet k);
    T visit(U a, @Nonnull KLetRec k);
    T visit(U a, @Nonnull KLetTuple k);

    // Branch
    T visit(U a, @Nonnull KIfEq k);
    T visit(U a, @Nonnull KIfLE k);

    // Calling functions (only used in K-normalized code)
    T visit(U a, @Nonnull KApp k);

    // Calling functions (after closure-converting the K-normalized code)
    T visit(U a, @Nonnull ApplyClosure k);
    T visit(U a, @Nonnull ApplyDirect k);
    T visit(U a, @Nonnull ClosureMake k);
}
