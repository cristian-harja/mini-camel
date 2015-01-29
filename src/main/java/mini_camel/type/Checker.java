package mini_camel.type;

import mini_camel.util.Pair;
import mini_camel.ast.AstExp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class Checker {

    AstExp program;
    EquationGenerator gen;
    List<Pair<Type, Type>> input;
    List<Pair<Type, Type>> equations;
    EquationSolver solver;
    Map<String, Type> solution;

    public Checker(AstExp pgm, Map<String, Type> predefs) {
        program = pgm;
        gen = new EquationGenerator();
        input = gen.genEquations(pgm, predefs);
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

    public Type concreteType(Type t) {
        switch (t.getKind()) {
            case VARIABLE :
                Type newType = concreteType(solution.get(((TVar) t).v));
                return newType != null ? newType : t;

            case ARRAY:
                Type oldElementType = ((TArray) t).elementType;
                Type newElementType = concreteType(oldElementType);
                if (oldElementType == newElementType) return t;
                return new TArray(newElementType);

            case FUNCTION:
                TFun fun = (TFun) t;
                Type arg = concreteType(fun.arg);
                Type ret = concreteType(fun.ret);
                if (arg == fun.arg && ret == fun.ret) return t;
                return new TFun(arg, ret);

            case TUPLE:
                boolean changed = false;
                TTuple oldTuple = (TTuple) t;
                List<Type> newItems = new ArrayList<>(oldTuple.items.size());
                for (Type item : oldTuple.items) {
                    Type newItem = concreteType(item);
                    if (newItem != item) changed = true;
                    newItems.add(newItem);
                }
                return changed ? new TTuple(newItems) : oldTuple;

            default:
                return t;
        }
    }

    public boolean wellTyped() {
        return solver.unifyErrors.isEmpty() && solver.circular.isEmpty();
    }

    public List<Pair<Type, Type>> getErrors() {
        return solver.unifyErrors;
    }
}
