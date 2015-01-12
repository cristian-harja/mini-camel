package mini_camel.type;

import javafx.util.Pair;

import java.util.*;

public class EquationSolver {

    Map<String, Type> solution;
    List<Pair<Type, Type>> circular;
    Deque<Pair<Type, Type>> workingSet;
    List<Pair<Type, Type>> unifyErrors;

    public EquationSolver(List<Pair<Type, Type>> equations) {
        solution = new LinkedHashMap<>();
        unifyErrors = new ArrayList<>();
        circular = new ArrayList<>();
        workingSet = new ArrayDeque<>(equations.size() * 2);
        workingSet.addAll(equations);
        solveEquations();
    }

    public Map<String, Type> getSolution() {
        return solution;
    }

    private void solveEquations() {
        while (!workingSet.isEmpty()) {
            // remove one equation from the set
            Pair<Type, Type> currentEquation = workingSet.pop();

            // get the two types from the equality, try cast them to TVar
            Type t1 = currentEquation.getKey();
            Type t2 = currentEquation.getValue();
            TVar var1 = t1 instanceof TVar ? (TVar) t1 : null;
            TVar var2 = t2 instanceof TVar ? (TVar) t2 : null;


            if (var1 != null) {
                if (var2 != null && var1.v.equals(var2.v)) {
                    continue;
                }
                if (isRecursive(var1, t2)) {
                    circular.add(currentEquation);
                } else {
                    substituteAll(var1, t2);
                }
                continue;
            }

            if (var2 != null) {
                if (isRecursive(var2, t1)) {
                    circular.add(currentEquation);
                } else {
                    substituteAll(var2, t1);
                }
                continue;
            }

            unify(currentEquation);
        }
        solution = Collections.unmodifiableMap(solution);
        circular = Collections.unmodifiableList(circular);
        unifyErrors = Collections.unmodifiableList(unifyErrors);
    }

    private void unify(Pair<Type, Type> equation) {
        Type t1 = equation.getKey(), t2 = equation.getValue();
        if (t1.getClass() != t2.getClass()) {
            unifyErrors.add(equation);
            return;
        }
        if (t1 instanceof TArray) {
            TArray arr1 = (TArray) t1;
            TArray arr2 = (TArray) t2;
            workingSet.add(new Pair<>(
                    arr1.elementType,
                    arr2.elementType
            ));
            return;
        }
        if (t1 instanceof TFun) {
            TFun fun1 = (TFun) t1;
            TFun fun2 = (TFun) t2;
            workingSet.add(new Pair<>(fun1.arg, fun2.arg));
            workingSet.add(new Pair<>(fun1.ret, fun2.ret));
            return;
        }
        if (t1 instanceof TTuple) {
            TTuple tuple1 = (TTuple) t1;
            TTuple tuple2 = (TTuple) t2;
            if (tuple1.items.size() != tuple2.items.size()) {
                unifyErrors.add(equation);
                return;
            }
            for (int i = 0, n = tuple1.items.size(); i < n; ++i) {
                workingSet.add(new Pair<>(
                        tuple1.items.get(i),
                        tuple2.items.get(i)
                ));
            }
            return;
        }

    }

    private void substituteAll(TVar var, Type with) {
        Pair<Type, Type> eq;
        int i, n;

        solution.put(var.v, with);

        for (i = 0, n = workingSet.size(); i < n; ++i) {
            eq = workingSet.pop();
            eq = substituteInEquation(var, with, eq);
            workingSet.addLast(eq);
        }

        substituteInList(var, with, unifyErrors);
        substituteInList(var, with, circular);
    }

    private void substituteInList(
            TVar var, Type with,
            List<Pair<Type, Type>> list
    ) {
        int i, n = list.size();

        for (i = 0; i < n; ++i) {
            list.set(i, substituteInEquation(
                    var, with, list.get(i)
            ));
        }
    }

    private Pair<Type, Type> substituteInEquation(
            TVar var,
            Type with,
            Pair<Type, Type> eq
    ) {
        Type t1 = eq.getKey();
        Type t2 = eq.getValue();
        Type t3 = substituteInType(var, with, t1);
        Type t4 = substituteInType(var, with, t2);
        if (t1 != t3 || t2 != t4) {
            eq = new Pair<>(t3, t4);
        }
        return eq;
    }

    private Type substituteInType(TVar var, Type with, Type oldType) {
        if (oldType instanceof TUnit) return oldType;
        if (oldType instanceof TBool) return oldType;
        if (oldType instanceof TInt) return oldType;
        if (oldType instanceof TFloat) return oldType;
        if (oldType instanceof TVar) {
            if (var.v.equals(((TVar) oldType).v)) {
                return with;
            } else {
                return oldType;
            }
        }
        if (oldType instanceof TArray) {
            Type elementType = ((TArray) oldType).elementType;
            Type newElement = substituteInType(var, with, elementType);
            if (elementType == newElement) return oldType;
            return new TArray(newElement);
        }
        if (oldType instanceof TTuple) {
            boolean changed = false;
            TTuple oldTuple = (TTuple) oldType;
            List<Type> newItems = new ArrayList<>(oldTuple.items.size());
            for (Type item : oldTuple.items) {
                Type newItem = substituteInType(var, with, item);
                if (newItem != item) changed = true;
                newItems.add(newItem);
            }
            return changed ? new TTuple(newItems) : oldTuple;
        }
        if (oldType instanceof TFun) {
            TFun fun = (TFun) oldType;
            Type newArg = substituteInType(var, with, fun.arg);
            Type newRet = substituteInType(var, with, fun.ret);
            if (newArg == fun.arg && newRet == fun.ret) return oldType;
            return new TFun(newArg, newRet);
        }
        throw new IllegalArgumentException(oldType.getClass().getName());

    }

    private static boolean isRecursive(TVar var, Type type) {
        if (type instanceof TUnit) return false;
        if (type instanceof TBool) return false;
        if (type instanceof TInt) return false;
        if (type instanceof TFloat) return false;
        if (type instanceof TVar) {
            return var.v.equals(((TVar) type).v);
        }
        if (type instanceof TArray) {
            return isRecursive(var, ((TArray) type).elementType);
        }
        if (type instanceof TTuple) {
            for (Type item : ((TTuple) type).items) {
                if (isRecursive(var, item)) {
                    return true;
                }
            }
            return false;
        }
        if (type instanceof TFun) {
            TFun fun = (TFun) type;
            return isRecursive(var, fun.arg) || isRecursive(var, fun.ret);
        }
        throw new IllegalArgumentException(type.getClass().getName());
    }

}
