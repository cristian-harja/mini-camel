package mini_camel.visit;

import mini_camel.knorm.*;
import mini_camel.util.SymDef;
import mini_camel.util.SymRef;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
abstract class KNormalizeHelper {

    private int x = 0;

    protected SymDef newSymDef(KNode normalized) {
        return new SymDef(
                "tmp" + (++x),
                normalized.getDataType()
        );
    }

    protected static interface Handler1 {
        KNode apply(SymRef var);
    }

    protected static interface Handler2 {
        KNode apply(SymRef var1, SymRef var2);
    }

    protected static interface Handler3 {
        KNode apply(SymRef var1, SymRef var2, SymRef var3);
    }

    protected static interface HandlerN {
        KNode apply(SymRef[] vars);
    }

    protected final static Handler1 HANDLE_NEG_I = new Handler1() {
        public KNode apply(SymRef var) {
            return new KNeg(var);
        }
    };

    protected final static Handler2 HANDLE_ADD_I = new Handler2() {
        public KNode apply(SymRef var1, SymRef var2) {
            return new KAdd(var1, var2);
        }
    };

    protected final static Handler2 HANDLE_SUB_I = new Handler2() {
        public KNode apply(SymRef var1, SymRef var2) {
            return new KSub(var1, var2);
        }
    };

    protected final static Handler1 HANDLE_NEG_F = new Handler1() {
        public KNode apply(SymRef var) {
            return new KFNeg(var);
        }
    };

    protected final static Handler2 HANDLE_ADD_F = new Handler2() {
        public KNode apply(SymRef var1, SymRef var2) {
            return new KFAdd(var1, var2);
        }
    };

    protected final static Handler2 HANDLE_SUB_F = new Handler2() {
        public KNode apply(SymRef var1, SymRef var2) {
            return new KFSub(var1, var2);
        }
    };

    protected final static Handler2 HANDLE_MUL_F = new Handler2() {
        public KNode apply(SymRef var1, SymRef var2) {
            return new KFMul(var1, var2);
        }
    };

    protected final static Handler2 HANDLE_DIV_F = new Handler2() {
        public KNode apply(SymRef var1, SymRef var2) {
            return new KFDiv(var1, var2);
        }
    };

    protected final static Handler2 HANDLE_EQ = new Handler2() {
        public KNode apply(SymRef var1, SymRef var2) {
            return new KIfEq(var1, var2, KInt.CONST_1, KInt.CONST_0);
        }
    };

    protected final static Handler2 HANDLE_LE = new Handler2() {
        public KNode apply(SymRef var1, SymRef var2) {
            return new KIfLE(var1, var2, KInt.CONST_1, KInt.CONST_0);
        }
    };

    protected final static Handler3 HANDLE_PUT = new Handler3() {
        @Override
        public KNode apply(SymRef var1, SymRef var2, SymRef var3) {
            return new KPut(var1, var2, var3);
        }
    };

    @Nonnull
    protected KNode insert_let1(
            KNode normalized,
            Handler1 handler
    ) {
        if (normalized instanceof KVar) {
            return handler.apply(((KVar) normalized).var);
        }

        SymDef destVar = newSymDef(normalized);

        return new KLet(
                destVar,
                normalized,
                handler.apply(new SymRef(destVar.id))
        );
    }

    @Nonnull
    protected KNode insert_let2(
            KNode normalized1,
            KNode normalized2,
            Handler2 handler
    ) {
        SymDef letSymbol1;
        SymDef letSymbol2;
        SymRef result1;
        SymRef result2;
        KNode handledResult;

        if ((normalized1 instanceof KVar)) {
            letSymbol1 = null;
            result1 = ((KVar) normalized1).var;
        } else {
            letSymbol1 = newSymDef(normalized1);
            result1 = letSymbol1.makeRef();
        }

        if ((normalized2 instanceof KVar)) {
            letSymbol2 = null;
            result2 = ((KVar) normalized2).var;
        } else {
            letSymbol2 = newSymDef(normalized2);
            result2 = letSymbol2.makeRef();
        }

        handledResult = handler.apply(result1, result2);

        if (letSymbol1 != null) {
            handledResult = new KLet(
                    letSymbol1,
                    normalized1,
                    handledResult
            );
        }

        if (letSymbol2 != null) {
            handledResult = new KLet(
                    letSymbol2,
                    normalized2,
                    handledResult
            );
        }

        return handledResult;
    }

    @Nonnull
    protected KNode insert_let3(
            KNode normalized1,
            KNode normalized2,
            KNode normalized3,
            Handler3 handler
    ) {
        SymDef letSymbol1;
        SymDef letSymbol2;
        SymDef letSymbol3;
        SymRef result1;
        SymRef result2;
        SymRef result3;
        KNode handledResult;

        if ((normalized1 instanceof KVar)) {
            letSymbol1 = null;
            result1 = ((KVar) normalized1).var;
        } else {
            letSymbol1 = newSymDef(normalized1);
            result1 = letSymbol1.makeRef();
        }

        if ((normalized2 instanceof KVar)) {
            letSymbol2 = null;
            result2 = ((KVar) normalized2).var;
        } else {
            letSymbol2 = newSymDef(normalized2);
            result2 = letSymbol2.makeRef();
        }

        if ((normalized3 instanceof KVar)) {
            letSymbol3 = null;
            result3 = ((KVar) normalized3).var;
        } else {
            letSymbol3 = newSymDef(normalized3);
            result3 = letSymbol3.makeRef();
        }

        handledResult = handler.apply(result1, result2, result3);

        if (letSymbol1 != null) {
            handledResult = new KLet(
                    letSymbol1,
                    normalized1,
                    handledResult
            );
        }

        if (letSymbol2 != null) {
            handledResult = new KLet(
                    letSymbol2,
                    normalized2,
                    handledResult
            );
        }

        if (letSymbol3 != null) {
            handledResult = new KLet(
                    letSymbol3,
                    normalized3,
                    handledResult
            );
        }

        return handledResult;
    }

    @Nonnull
    protected KNode insert_letN(
            KNode[] normalized,
            HandlerN handler
    ) {
        int n = normalized.length;
        SymDef[] letSymbols = new SymDef[n];
        SymRef[] results = new SymRef[n];
        KNode handledResult;

        for (int i = 0; i < n; ++i) {
            KNode node = normalized[i];
            if (node instanceof KVar) {
                results[i] = ((KVar) node).var;
            } else {
                letSymbols[i] = newSymDef(node);
                results[i] = letSymbols[i].makeRef();
            }
        }

        handledResult = handler.apply(results);

        for (int i = 0; i < n; ++i) {
            if (letSymbols[i] == null) continue;
            handledResult = new KLet(
                    letSymbols[i],
                    normalized[i],
                    handledResult
            );
        }

        return handledResult;
    }
}
