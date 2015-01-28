package mini_camel.util;

import mini_camel.knorm.*;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface KVisitor {
    // Values
    void visit(KUnit k);
    void visit(KInt k);
    void visit(KFloat k);
    void visit(KVar k);
    void visit(KTuple k);

    // Integer operations
    void visit(KNeg k);
    void visit(KAdd k);
    void visit(KSub k);

    // Floating point operations
    void visit(KFNeg k);
    void visit(KFAdd k);
    void visit(KFDiv k);
    void visit(KFMul k);
    void visit(KFSub k);

    // Array operations
    void visit(KGet k);
    void visit(KPut k);

    // Let
    void visit(KLet k);
    void visit(KLetRec k);
    void visit(KLetTuple k);

    // Branch
    void visit(KIfEq k);
    void visit(KIfLE k);

    // Calling functions (only used in K-normalized code)
    void visit(KApp k);

    // Calling functions (after closure-converting the K-normalized code)
    void visit(ApplyClosure k);
    void visit(ApplyDirect k);
    void visit(ClosureMake k);
}
