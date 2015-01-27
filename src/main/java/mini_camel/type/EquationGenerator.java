package mini_camel.type;

import mini_camel.ast.*;
import mini_camel.util.Pair;
import mini_camel.util.SymTable;
import mini_camel.visit.Visitor;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EquationGenerator {

    static final Type UNIT = TUnit.INSTANCE;
    static final Type BOOL = TBool.INSTANCE;
    static final Type INT = TInt.INSTANCE;
    static final Type FLOAT = TFloat.INSTANCE;

    public List<Pair<Type, Type>> genEquations(final AstExp exp) {
        List<Pair<Type, Type>> out = new ArrayList<>();
        SymTable<Type> env = new SymTable<>();
        env.push();
        env.put("print_newline", new TFun(UNIT, UNIT));
        env.put("print_int", new TFun(INT, UNIT));

        Type floatFun = new TFun(FLOAT, FLOAT);

        env.put("abs_float", floatFun);
        env.put("sqrt", floatFun);
        env.put("sin", floatFun);
        env.put("cos", floatFun);

        env.put("float_of_int", new TFun(INT, FLOAT));
        env.put("int_of_float", new TFun(FLOAT, INT));
        env.put("truncate", new TFun(FLOAT, INT));

        genEquations(out, env, exp, UNIT);
        return out;
    }

    private void genEquations(
            final List<Pair<Type, Type>> out,
            final SymTable<Type> env,
            final AstExp exp,
            final Type type
    ) {
        exp.accept(new Visitor() {

            private void typeEquals(Type t1, Type t2) {
                out.add(new Pair<>(t1, t2));
            }

            private void exprIsOfType(AstExp e, Type t) {
                if (e == exp) {
                    throw new RuntimeException("Possibly infinite recursion");
                }
                genEquations(out, env, e, t);
            }

            @Override
            public void visit(@Nonnull AstErr e) {
                // no equations are generated for an erroneous AST node
            }

            @Override
            public void visit(@Nonnull AstUnit e) {
                typeEquals(type, UNIT);
            }

            @Override
            public void visit(@Nonnull AstBool e) {
                typeEquals(type, BOOL);
            }

            @Override
            public void visit(@Nonnull AstInt e) {
                typeEquals(type, INT);
            }

            @Override
            public void visit(@Nonnull AstFloat e) {
                typeEquals(type, FLOAT);
            }

            @Override
            public void visit(@Nonnull AstNot e) {
                typeEquals(type, BOOL);
                exprIsOfType(e.e, BOOL);
            }

            @Override
            public void visit(@Nonnull AstNeg e) {
                typeEquals(type, INT);
                exprIsOfType(e.e, INT);
            }

            @Override
            public void visit(@Nonnull AstFNeg e) {
                typeEquals(type, FLOAT);
                exprIsOfType(e.e, FLOAT);
            }

            @Override
            public void visit(@Nonnull AstAdd e) {
                typeEquals(type, INT);
                exprIsOfType(e.e1, INT);
                exprIsOfType(e.e2, INT);
            }

            @Override
            public void visit(@Nonnull AstSub e) {
                typeEquals(type, INT);
                exprIsOfType(e.e1, INT);
                exprIsOfType(e.e2, INT);
            }

            @Override
            public void visit(@Nonnull AstFAdd e) {
                typeEquals(type, FLOAT);
                exprIsOfType(e.e1, FLOAT);
                exprIsOfType(e.e2, FLOAT);
            }

            @Override
            public void visit(@Nonnull AstFSub e) {
                typeEquals(type, FLOAT);
                exprIsOfType(e.e1, FLOAT);
                exprIsOfType(e.e2, FLOAT);
            }

            @Override
            public void visit(@Nonnull AstFMul e) {
                typeEquals(type, FLOAT);
                exprIsOfType(e.e1, FLOAT);
                exprIsOfType(e.e2, FLOAT);
            }

            @Override
            public void visit(@Nonnull AstFDiv e) {
                typeEquals(type, FLOAT);
                exprIsOfType(e.e1, FLOAT);
                exprIsOfType(e.e2, FLOAT);
            }

            @Override
            public void visit(@Nonnull AstEq e) {
                Type t = Type.gen();
                typeEquals(type, BOOL);
                exprIsOfType(e.e1, t);
                exprIsOfType(e.e2, t);
            }

            @Override
            public void visit(@Nonnull AstLE e) {
                Type t = Type.gen();
                typeEquals(type, BOOL);
                exprIsOfType(e.e1, t);
                exprIsOfType(e.e2, t);
            }

            @Override
            public void visit(@Nonnull AstIf e) {
                exprIsOfType(e.eCond, BOOL);
                exprIsOfType(e.eThen, type);
                exprIsOfType(e.eElse, type);
            }

            @Override
            public void visit(@Nonnull AstLet e) {
                exprIsOfType(e.initializer, e.decl.type);
                env.push();
                {
                    env.put(e.decl.id, e.decl.type);
                    exprIsOfType(e.ret, type);
                }
                env.pop();
            }

            @Override
            public void visit(@Nonnull AstSymRef e) {
                typeEquals(type, env.get(e.id));
            }

            @Override
            public void visit(@Nonnull AstLetRec e) {
                env.push();
                {
                    env.put(e.fd.decl.id, e.fd.decl.type);
                    exprIsOfType(e.fd, e.fd.decl.type);
                    exprIsOfType(e.ret, type);
                }
                env.pop();
            }

            @Override
            public void visit(@Nonnull AstFunDef e) {

                env.push();
                {
                    for (AstSymDef arg : e.args) {
                        env.put(arg.id, arg.type);
                    }

                    //typeEquals(type, e.functionType);
                    exprIsOfType(e.body, e.returnType);
                }
                env.pop();
            }

            private void curry(
                    Type calleeType,
                    Iterator<AstExp> arguments,
                    Type returnType
            ) {
                if (!arguments.hasNext()) {
                    typeEquals(calleeType, returnType);
                    return;
                }
                TFun functionType = new TFun();
                typeEquals(calleeType, functionType);

                AstExp arg = arguments.next();
                exprIsOfType(arg, functionType.arg);

                curry(functionType.ret, arguments, returnType);
            }

            @Override
            public void visit(@Nonnull AstApp e) {
                Type calleeType = Type.gen();
                exprIsOfType(e.e, calleeType);
                curry(calleeType, e.es.iterator(), type);
            }

            @Override
            public void visit(@Nonnull AstTuple e) {
                List<Type> items = new ArrayList<>(e.es.size());
                for (AstExp item : e.es) {
                    Type t = Type.gen();
                    exprIsOfType(item, t);
                    items.add(t);
                }
                typeEquals(type, new TTuple(items));
            }

            @Override
            public void visit(@Nonnull AstLetTuple e) {
                exprIsOfType(e.initializer, new TTuple(e.getIdentifierTypes()));

                env.push();
                {
                    for (AstSymDef sym : e.ids) {
                        env.put(sym.id, sym.type);
                    }

                    exprIsOfType(e.ret, type);
                }
                env.pop();
            }

            @Override
            public void visit(@Nonnull AstArray e) {
                Type elementType = Type.gen();
                exprIsOfType(e.size, INT);
                exprIsOfType(e.initializer, elementType);
                typeEquals(type, new TArray(elementType));
            }

            @Override
            public void visit(@Nonnull AstGet e) {
                exprIsOfType(e.array, new TArray(type));
                exprIsOfType(e.index, INT);
            }

            @Override
            public void visit(@Nonnull AstPut e) {
                Type t = Type.gen();
                exprIsOfType(e.array, new TArray(t));
                exprIsOfType(e.index, INT);
                exprIsOfType(e.value, t);
                typeEquals(type, UNIT);
            }
        });
    }

}
