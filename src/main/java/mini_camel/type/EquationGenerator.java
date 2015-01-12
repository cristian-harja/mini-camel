package mini_camel.type;

import javafx.util.Pair;
import mini_camel.SymTable;
import mini_camel.ast.*;

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
                exprIsOfType(e.e1, BOOL);
                exprIsOfType(e.e2, type);
                exprIsOfType(e.e3, type);
            }

            @Override
            public void visit(@Nonnull AstLet e) {
                exprIsOfType(e.e1, e.id_type);
                env.push();
                {
                    env.put(e.id.id, e.id_type);
                    exprIsOfType(e.e2, type);
                }
                env.pop();
            }

            @Override
            public void visit(@Nonnull AstVar e) {
                typeEquals(type, env.get(e.id.id));
            }

            @Override
            public void visit(@Nonnull AstLetRec e) {
                env.push();
                {
                    env.put(e.fd.id.id, e.fd.functionType);
                    exprIsOfType(e.fd, e.fd.functionType);
                    exprIsOfType(e.e, type);
                }
                env.pop();
            }

            /*
            @Override
            public void visit(AstFunDef e) {
                int i, numArgs = e.args.size();
                Type returnType = Type.gen();
                List<Type> argTypes = new ArrayList<>(numArgs);

                for (i = 0; i < numArgs; ++i) {
                    argTypes.add(Type.gen());
                }

                Type functionType = returnType;

                for (i = numArgs - 1; i >= 0; --i) {
                    functionType = new TFun(argTypes.get(i), functionType);
                }

                for (i = 0; i < numArgs; ++i) {
                    env.put(e.args.get(i).id, argTypes.get(i));
                }

                typeEquals(type, functionType);
                exprIsOfType(e.e, returnType);

                for (i = 0; i < numArgs; ++i) {
                    env.remove(e.args.get(i).id);
                }
            }
            */

            @Override
            public void visit(@Nonnull AstFunDef e) {
                int i, numArgs = e.args.size();

                env.push();
                {
                    for (i = 0; i < numArgs; ++i) {
                        env.put(e.args.get(i).id, e.argTypes.get(i));
                    }

                    //typeEquals(type, e.functionType);
                    exprIsOfType(e.e, e.returnType);
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
                int i, n = e.ids.size();
                exprIsOfType(e.e1, new TTuple(e.ts));

                env.push();
                {
                    for (i = 0; i < n; ++i) {
                        env.put(e.ids.get(i).id, e.ts.get(i));
                    }

                    exprIsOfType(e.e2, type);
                }
                env.pop();
            }

            @Override
            public void visit(@Nonnull AstArray e) {
                Type elementType = Type.gen();
                exprIsOfType(e.e1, INT);
                exprIsOfType(e.e2, elementType);
                typeEquals(type, new TArray(elementType));
            }

            @Override
            public void visit(@Nonnull AstGet e) {
                exprIsOfType(e.e1, new TArray(type));
                exprIsOfType(e.e2, INT);
            }

            @Override
            public void visit(@Nonnull AstPut e) {
                Type t = Type.gen();
                exprIsOfType(e.e1, new TArray(t));
                exprIsOfType(e.e2, INT);
                exprIsOfType(e.e3, t);
                typeEquals(type, UNIT);
            }
        });
    }

}
