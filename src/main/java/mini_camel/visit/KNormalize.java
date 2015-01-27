package mini_camel.visit;

import mini_camel.ast.*;
import mini_camel.knorm.*;
import mini_camel.type.TTuple;
import mini_camel.type.Type;
import mini_camel.util.SymDef;
import mini_camel.util.SymRef;
import mini_camel.util.SymTable;
import mini_camel.util.Visitor1;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

/**
 * Implements K-normalization, which converts an AST from its general form,
 * into an equivalent representation, but simplified such that operations
 * are always performed on variables and not sub-expressions. Complex
 * expressions are decomposed into a sequence of nested {@code let}s.
 */
@ParametersAreNonnullByDefault
@SuppressWarnings("unused")
public final class KNormalize extends KNormalizeHelper
        implements Visitor1<KNode> {

    private SymTable<Type> symbolTable = new SymTable<>();

    private KNormalize() {
    }

    public static KNode compute(AstExp root, Map<String, Type> globals) {
        KNormalize kn = new KNormalize();
        kn.symbolTable.push();
        kn.symbolTable.top().putAll(globals);
        return root.accept(kn);
    }

    public static KNode compute(AstExp parseTree) {
        return compute(parseTree, Collections.<String, Type>emptyMap());
    }

    public KNode visit(AstUnit e) {
        return KUnit.INSTANCE;
    }

    public KNode visit(AstBool e) {
        return e.b ? KInt.CONST_1 : KInt.CONST_0;
    }

    public KNode visit(AstInt e) {
        return new KInt(e.i);
    }

    public KNode visit(AstFloat e) {
        return new KFloat(e.f);
    }

    public KNode visit(AstNot e) {
        AstIf astIf = new AstIf(e.e, new AstBool(false), new AstBool(true));
        return astIf.accept(this);
    }

    public KNode visit(AstNeg e) {
        return insert_let1(
                e.e.accept(this),
                HANDLE_NEG_I
        );
    }

    public KNode visit(AstAdd e) {
        return insert_let2(
                e.e1.accept(this),
                e.e2.accept(this),
                HANDLE_ADD_I
        );
    }

    public KNode visit(AstSub e) {
        return insert_let2(
                e.e1.accept(this),
                e.e2.accept(this),
                HANDLE_SUB_I
        );
    }

    public KNode visit(AstFNeg e) {
        return insert_let1(
                e.e.accept(this),
                HANDLE_NEG_F
        );
    }

    public KNode visit(AstFAdd e) {
        return insert_let2(
                e.e1.accept(this),
                e.e2.accept(this),
                HANDLE_ADD_F
        );
    }

    public KNode visit(AstFSub e) {
        return insert_let2(
                e.e1.accept(this),
                e.e2.accept(this),
                HANDLE_SUB_F
        );
    }

    public KNode visit(AstFMul e) {
        return insert_let2(
                e.e1.accept(this),
                e.e2.accept(this),
                HANDLE_MUL_F
        );
    }

    public KNode visit(AstFDiv e) {
        return insert_let2(
                e.e1.accept(this),
                e.e2.accept(this),
                HANDLE_DIV_F
        );
    }

    public KNode visit(AstEq e) {
        return insert_let2(
                e.e1.accept(this),
                e.e2.accept(this),
                HANDLE_EQ
        );
    }

    public KNode visit(AstLE e) {
        return insert_let2(
                e.e1.accept(this),
                e.e2.accept(this),
                HANDLE_LE
        );
    }

    public KNode visit(final AstIf e) {
        final AstExp cond = e.eCond;

        if (cond instanceof AstBool) {
            return (((AstBool) cond).b ? e.eThen : e.eElse).accept(this);
        }

        if (cond instanceof AstNeg) {
            AstExp astIf = new AstIf(((AstNeg) cond).e, e.eElse, e.eThen);
            return astIf.accept(this);
        }

        final KNode kThen = e.eThen.accept(this);
        final KNode kElse = e.eElse.accept(this);

        AstExp op1 = null, op2 = null;
        boolean isEq, isLe = false;

        if (isEq = (cond instanceof AstEq)) {
            AstEq eq = ((AstEq) cond);
            op1 = eq.e1;
            op2 = eq.e2;
        } else if (isLe = (cond instanceof AstLE)) {
            AstLE le = ((AstLE) cond);
            op1 = le.e1;
            op2 = le.e2;
        }

        if (isEq || isLe) {
            Handler2 handler = new Handler2() {
                public KNode apply(SymRef var1, SymRef var2) {
                    return ((cond instanceof AstEq)
                            ? new KIfEq(var1, var2, kThen, kElse)
                            : new KIfLE(var1, var2, kThen, kElse)
                    );
                }
            };
            return insert_let2(
                    op1.accept(this),
                    op2.accept(this),
                    handler
            );
        }

        // not `if(Bool(b))`
        // not `if(Not(e))`
        // not `if(Eq(e1, e2))`
        // not `if(LE(e1, e2))`

        return insert_let2(
                e.eCond.accept(this),
                KInt.CONST_0,
                new Handler2() {
                    public KNode apply(SymRef var, SymRef const0) {
                        return new KIfEq(var, const0, kElse, kThen);
                    }
                }
        );
    }

    public KNode visit(AstLet e) {
        KNode initializer;
        KNode ret;
        symbolTable.push();
        {
            initializer = e.initializer.accept(this);
            symbolTable.put(e.decl.id, e.decl.type);
            ret = e.ret.accept(this);
        }
        symbolTable.pop();
        return new KLet(e.decl, initializer, ret);
    }

    public KNode visit(SymRef e) {
        Type t = symbolTable.get(e.id);
        if (t == null) {
            // Should never happen if the code was checked for free variables.
            // Still, this is wouldn't be an incident; we just create a new
            // type variable and let other parts of the compiler deal with it.
            t = Type.gen();
        }
        return new KVar(e, t);
    }

    public KNode visit(AstLetRec e) {
        KNode ret;
        KNode fdBody;
        symbolTable.push();
        {
            symbolTable.put(e.fd.decl.id, e.fd.decl.type);
            ret = e.ret.accept(this);
            for (SymDef arg : e.fd.args) {
                symbolTable.put(arg.id, arg.type);
            }
            fdBody = e.fd.body.accept(this);
        }
        symbolTable.pop();

        return new KLetRec(new KFunDef(e.fd.decl, e.fd.args, fdBody), ret);
    }

    public KNode visit(AstApp e) {
        final int n = e.es.size();
        final KNode[] arguments = new KNode[n];

        final KNode callee = e.e.accept(this);

        for (int i = 0; i < n; ++i) {
            arguments[i] = e.es.get(i).accept(this);
        }

        // Behold...
        return insert_let1(callee, new Handler1() {
            public KNode apply(final SymRef functionVar) {
                return insert_letN(arguments, new HandlerN() {
                    public KNode apply(SymRef[] vars) {
                        return new KApp(
                                functionVar,
                                Arrays.asList(vars),
                                callee.getDataType()
                        );
                    }
                });
            }
        });

    }

    public KNode visit(AstTuple e) {
        int n = e.es.size();
        KNode[] items = new KNode[n];
        Type[] types = new Type[n];

        for (int i = 0; i < n; ++i) {
            items[i] = e.es.get(i).accept(this);
            types[i] = items[i].getDataType();
        }

        final TTuple type = new TTuple(Arrays.asList(types));

        return insert_letN(items, new HandlerN() {
            public KNode apply(SymRef[] vars) {
                return new KTuple(Arrays.asList(vars), type);
            }
        });
    }

    public KNode visit(final AstLetTuple e) {
        KNode kInit = e.initializer.accept(this);
        Handler1 handler = new Handler1() {
            public KNode apply(SymRef varInitializer) {
                KNode ret;
                symbolTable.push();
                {
                    for (SymDef id : e.ids) {
                        symbolTable.put(id.id, id.type);
                    }
                    ret = e.ret.accept(KNormalize.this);
                }
                symbolTable.pop();

                return new KLetTuple(e.ids, varInitializer, ret);
            }
        };
        return insert_let1(kInit, handler);
    }

    public KNode visit(AstArray e) {
        return insert_let2(
                e.size.accept(this),
                e.initializer.accept(this),
                HANDLE_ARRAY
        );
    }

    public KNode visit(AstGet e) {
        final KNode kArray = e.array.accept(this);
        final KNode kIndex = e.index.accept(this);
        final Handler2 handler = new Handler2() {
            public KNode apply(SymRef var1, SymRef var2) {
                return new KGet(var1, var2, kArray.getDataType());
            }
        };
        return insert_let2(kArray, kIndex, handler);
    }

    public KNode visit(AstPut e) {
        return insert_let3(
                e.array.accept(this),
                e.index.accept(this),
                e.value.accept(this),
                HANDLE_PUT
        );
    }

    public KNode visit(AstFunDef e) {
        throw new RuntimeException("" +
                "Can't apply transformation (K-normalization) directly on " +
                "an `AstFunDef` object. Consider applying the transform on " +
                "the containing `AstLetRec`."
        );
    }

    public KNode visit(AstErr e) {
        throw new RuntimeException("" +
                "Can't apply transformation (K-normalization) on an " +
                "`AstErr` object. This operation should not be attempted " +
                "if there were parse errors."
        );
    }

}
