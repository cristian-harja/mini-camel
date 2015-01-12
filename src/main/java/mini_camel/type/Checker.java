package mini_camel.type;

import javafx.util.Pair;
import mini_camel.Id;
import mini_camel.ast.AstExp;
import mini_camel.ast.AstFunDef;
import mini_camel.ast.AstLet;
import mini_camel.ast.DummyVisitor;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Checker {

    AstExp program;
    EquationGenerator gen;
    List<Pair<Type, Type>> input;
    List<Pair<Type, Type>> equations;
    EquationSolver solver;
    Map<String, Type> solution;

    public Checker(AstExp pgm) {
        program = pgm;
        gen = new EquationGenerator();
        input = gen.genEquations(pgm);
        equations = new ArrayList<>(input);
        solver = new EquationSolver(equations);
        solution = solver.getSolution();
    }

    public AstExp getProgram() {
        return program;
    }

    public List<Pair<Type, Type>> getEquations() {
        return equations;
    }

    public Map<String, Type> getSolution() {
        return solution;
    }

    @SuppressWarnings("unused")
    public Type getSymbolType(final String symbolName) {
        final Type[] found = new Type[1];
        program.accept(new DummyVisitor() {
            @Override
            public void visit(@Nonnull AstLet e) {
                if (e.id.id.equals(symbolName)) {
                    found[0] = e.id_type;
                }
                e.e1.accept(this);
                e.e2.accept(this);
            }

            @Override
            public void visit(@Nonnull AstFunDef e) {
                if (e.id.id.equals(symbolName)) {
                    found[0] = e.functionType;
                    return;
                }
                List<Id> args = e.args;
                for (int i = 0; i < args.size(); i++) {
                    Id exp = args.get(i);
                    if (exp.id.equals(symbolName)) {
                        found[0] = e.argTypes.get(i);
                        return;
                    }
                }
                e.e.accept(this);
            }
        });
        return concreteType(found[0]);
    }

    public Type concreteType(Type t) {
        if (t instanceof TVar) {
            Type newType = concreteType(solution.get(((TVar) t).v));
            return newType != null ? newType : t;
        }
        if (t instanceof TArray) {
            Type oldElementType = ((TArray) t).elementType;
            Type newElementType = concreteType(oldElementType);
            if (oldElementType == newElementType) return t;
            return new TArray(newElementType);
        }
        if (t instanceof TFun) {
            TFun fun = (TFun) t;
            Type arg = concreteType(fun.arg);
            Type ret = concreteType(fun.ret);
            if (arg == fun.arg && ret == fun.ret) return t;
            return new TFun(arg, ret);
        }
        if (t instanceof TTuple) {
            boolean changed = false;
            TTuple oldTuple = (TTuple) t;
            List<Type> newItems = new ArrayList<>(oldTuple.items.size());
            for (Type item : oldTuple.items) {
                Type newItem = concreteType(item);
                if (newItem != item) changed = true;
                newItems.add(newItem);
            }
            return changed ? new TTuple(newItems) : oldTuple;
        }
        return t;
    }

    public boolean wellTyped() {
        return solver.unifyErrors.isEmpty() && solver.circular.isEmpty();
    }

    public List<Pair<Type, Type>> getErrors() {
        return solver.unifyErrors;
    }
}
