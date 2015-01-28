package mini_camel.type;

import mini_camel.ast.*;
import mini_camel.util.*;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@ParametersAreNonnullByDefault
public class EquationGenerator {

    static final Type UNIT = TUnit.INSTANCE;
    static final Type BOOL = TBool.INSTANCE;
    static final Type INT = TInt.INSTANCE;
    static final Type FLOAT = TFloat.INSTANCE;

    public List<Pair<Type, Type>> genEquations(
            final AstExp exp,
            final Map<String, Type> predefs
    ) {
        List<Pair<Type, Type>> out = new ArrayList<>();
        SymTable<Type> env = new SymTable<>();
        env.push();
        env.putAll(predefs);
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

            public void visit(AstErr e) {
                // no equations are generated for an erroneous AST node
            }

            public void visit(AstUnit e) {
                typeEquals(type, UNIT);
            }

            public void visit(AstBool e) {
                typeEquals(type, BOOL);
            }

            public void visit(AstInt e) {
                typeEquals(type, INT);
            }

            public void visit(AstFloat e) {
                typeEquals(type, FLOAT);
            }

            public void visit(AstNot e) {
                typeEquals(type, BOOL);
                exprIsOfType(e.e, BOOL);
            }

            public void visit(AstNeg e) {
                typeEquals(type, INT);
                exprIsOfType(e.e, INT);
            }

            public void visit(AstFNeg e) {
                typeEquals(type, FLOAT);
                exprIsOfType(e.e, FLOAT);
            }

            public void visit(AstAdd e) {
                typeEquals(type, INT);
                exprIsOfType(e.e1, INT);
                exprIsOfType(e.e2, INT);
            }

            public void visit(AstSub e) {
                typeEquals(type, INT);
                exprIsOfType(e.e1, INT);
                exprIsOfType(e.e2, INT);
            }

            public void visit(AstFAdd e) {
                typeEquals(type, FLOAT);
                exprIsOfType(e.e1, FLOAT);
                exprIsOfType(e.e2, FLOAT);
            }

            public void visit(AstFSub e) {
                typeEquals(type, FLOAT);
                exprIsOfType(e.e1, FLOAT);
                exprIsOfType(e.e2, FLOAT);
            }

            public void visit(AstFMul e) {
                typeEquals(type, FLOAT);
                exprIsOfType(e.e1, FLOAT);
                exprIsOfType(e.e2, FLOAT);
            }

            public void visit(AstFDiv e) {
                typeEquals(type, FLOAT);
                exprIsOfType(e.e1, FLOAT);
                exprIsOfType(e.e2, FLOAT);
            }

            public void visit(AstEq e) {
                Type t = Type.gen();
                typeEquals(type, BOOL);
                exprIsOfType(e.e1, t);
                exprIsOfType(e.e2, t);
            }

            public void visit(AstLE e) {
                Type t = Type.gen();
                typeEquals(type, BOOL);
                exprIsOfType(e.e1, t);
                exprIsOfType(e.e2, t);
            }

            public void visit(AstIf e) {
                exprIsOfType(e.eCond, BOOL);
                exprIsOfType(e.eThen, type);
                exprIsOfType(e.eElse, type);
            }

            public void visit(AstLet e) {
                exprIsOfType(e.initializer, e.decl.type);
                env.push();
                {
                    env.put(e.decl.id, e.decl.type);
                    exprIsOfType(e.ret, type);
                }
                env.pop();
            }

            public void visit(SymRef e) {
                typeEquals(type, env.get(e.id));
            }

            public void visit(AstLetRec e) {
                env.push();
                {
                    env.put(e.fd.decl.id, e.fd.decl.type);
                    exprIsOfType(e.fd, e.fd.decl.type);
                    exprIsOfType(e.ret, type);
                }
                env.pop();
            }

            public void visit(AstFunDef e) {

                env.push();
                {
                    for (SymDef arg : e.args) {
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

            public void visit(AstApp e) {
                Type calleeType = Type.gen();
                exprIsOfType(e.e, calleeType);
                curry(calleeType, e.es.iterator(), type);
            }

            public void visit(AstTuple e) {
                List<Type> items = new ArrayList<>(e.es.size());
                for (AstExp item : e.es) {
                    Type t = Type.gen();
                    exprIsOfType(item, t);
                    items.add(t);
                }
                typeEquals(type, new TTuple(items));
            }

            public void visit(AstLetTuple e) {
                exprIsOfType(e.initializer, new TTuple(e.getIdentifierTypes()));

                env.push();
                {
                    for (SymDef sym : e.ids) {
                        env.put(sym.id, sym.type);
                    }

                    exprIsOfType(e.ret, type);
                }
                env.pop();
            }

            public void visit(AstArray e) {
                Type elementType = Type.gen();
                exprIsOfType(e.size, INT);
                exprIsOfType(e.initializer, elementType);
                typeEquals(type, new TArray(elementType));
            }

            public void visit(AstGet e) {
                exprIsOfType(e.array, new TArray(type));
                exprIsOfType(e.index, INT);
            }

            public void visit(AstPut e) {
                Type t = Type.gen();
                exprIsOfType(e.array, new TArray(t));
                exprIsOfType(e.index, INT);
                exprIsOfType(e.value, t);
                typeEquals(type, UNIT);
            }
        });
    }

}
