package mini_camel.ir;

import mini_camel.ir.instr.*;
import mini_camel.ir.op.ConstFloat;
import mini_camel.ir.op.ConstInt;
import mini_camel.ir.op.Operand;
import mini_camel.ir.op.Var;
import mini_camel.knorm.*;
import mini_camel.type.Checker;
import mini_camel.type.TFun;
import mini_camel.type.TUnit;
import mini_camel.type.Type;
import mini_camel.util.KVisitor;
import mini_camel.util.SymDef;
import mini_camel.util.SymRef;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
public final class CodeGenerator2 implements KVisitor {

    private List<Instr> code = new ArrayList<>();
    private Stack<Var> assignStack = new Stack<>();

    private Var dest;

    private Map<String, Var> free = new LinkedHashMap<>();
    private Map<String, Var> args = new LinkedHashMap<>();
    private Map<String, Var> locals = new LinkedHashMap<>();

    @Nonnull
    private Map<String, Type> globals;

    @Nonnull
    private Map<String, Integer> arity;

    @Nonnull
    private Map<String, Integer> arityFree;

    @CheckForNull
    private Checker typeChecker;

    private CodeGenerator2(
            @Nonnull Map<String, Type> globals,
            @Nonnull Map<String, Integer> arity,
            @Nonnull Map<String, Integer> freeArity,
            @Nullable Checker typeChecker
    ) {
        this.globals = globals;
        this.arity = arity;
        this.arityFree = freeArity;
        this.typeChecker = typeChecker;
    }

    public static List<Function> compile(
            @Nullable Map<String, Type> predefs,
            @Nullable Checker checker,
            @Nonnull Program p,
            @Nonnull String mainName
    ) {
        if (predefs == null) predefs = Collections.emptyMap();
        Map<String, Type> globals = new LinkedHashMap<>(predefs);
        Map<String, KFunDef> jobs = new LinkedHashMap<>(p.topLevel);
        Map<String, Integer> arity = new HashMap<>();
        Map<String, Integer> freeArity = new HashMap<>();

        jobs.put(mainName, new KFunDef(
                new SymDef(mainName, new TFun(TUnit.INSTANCE, TUnit.INSTANCE)),
                Collections.<SymDef>emptyList(),
                p.mainBody
        ));

        for (KFunDef fd : jobs.values()) {
            String id = fd.name.id;
            globals.put(id, checker.concreteType(fd.name.type));
            freeArity.put(id, fd.freeVars.size());
            Type functionType = checker.concreteType(fd.name.type);
            arity.put(id, takesVoid(functionType) ? 0 : fd.args.size());
        }

        for (String predef : predefs.keySet()) {
            arity.put(predef, 1);
        }

        List<Function> compiled = new ArrayList<>(p.topLevel.size() + 1);

        for (KFunDef fd2 : jobs.values()) {
            CodeGenerator2 cg = new CodeGenerator2(
                    globals, arity, freeArity, checker
            );
            compiled.add(cg.compile(fd2));
        }

        return compiled;
    }

    private Function compile(KFunDef fd) {
        for (SymDef arg : fd.args) {
            args.put(arg.id, new Var(arg.id, arg.type));
        }
        for (SymRef arg : fd.freeVars) {
            free.put(arg.id, new Var(arg.id, null));
        }

        Type funcType = concreteType(fd.name.type);
        if (returnsVoid(funcType)) {
            fd.body.accept(this);
            code.add(new Ret(null));
        } else {
            Type retType = ((TFun) funcType).ret;
            pushVar(newLocal(new SymDef("ret", retType)));
            fd.body.accept(this);
            code.add(new Ret(dest));
            popVar();
        }

        return new Function(
                new Label(fd.name.id),
                new ArrayList<>(free.values()),
                new ArrayList<>(args.values()),
                new ArrayList<>(locals.values()),
                code
        );
    }

    @Nonnull
    private Type concreteType(@Nullable Type t) {
        if (t == null) return Type.gen();
        if (typeChecker == null) return t;
        return typeChecker.concreteType(t);
    }

    @Nonnull
    private Type concreteType(String symbol) {
        return concreteType(globals.get(symbol));
    }

    private static boolean takesVoid(@Nullable Type funType) {
        if (funType == null) return false;
        if (funType.getKind() != Type.Kind.FUNCTION) return false;
        if (((TFun) funType).arg.getKind() != Type.Kind.UNIT) return false;
        return true;
    }

    private static boolean returnsVoid(@Nullable Type funType) {
        if (funType == null) return false;
        if (funType.getKind() != Type.Kind.FUNCTION) return false;
        if (((TFun) funType).ret.getKind() != Type.Kind.UNIT) return false;
        return true;
    }


    private static final ConstInt CONST_0 = new ConstInt(0);
    private static final ConstInt CONST_1 = new ConstInt(1);

    private ConstInt cons(int i) {
        if (i == 0) return CONST_0;
        if (i == 1) return CONST_1;
        return new ConstInt(i);
    }

    private ConstFloat cons(float f) {
        return new ConstFloat(f);
    }

    private void pushVar(Var v) {
        dest = v;
        assignStack.push(v);
    }

    private void popVar() {
        assignStack.pop();
        dest = assignStack.isEmpty() ? null : assignStack.peek();
    }

    private Var find(SymRef ref) {
        Var v;

        v = locals.get(ref.id);
        if (v != null) return v;

        v = args.get(ref.id);
        if (v != null) return v;

        v = free.get(ref.id);
        if (v != null) return v;

        v = new Var(ref.id, globals.get(ref.id));
        return v;
        /*
        if (v != null) return v;

        throw new RuntimeException(
                "Unknown reference " + ref.id + " found when generating IR"
        );
        */
    }

    public void visit(KUnit k) {
        // do nothing
    }

    public void visit(KInt k) {
        code.add(new Assign(dest, cons(k.i)));
    }

    public void visit(KFloat k) {
        code.add(new Assign(dest, cons(k.f)));
    }

    public void visit(KVar k) {
        code.add(new Assign(dest, find(k.var)));
    }

    public void visit(KTuple k) {
        int i, n = k.items.size();
        code.add(new ArrNew(dest, cons(n), null));
        for (i = 0; i < n; ++i) {
            code.add(new ArrPut(dest, cons(i), find(k.items.get(i))));
        }
    }

    public void visit(KArray k) {
        code.add(new ArrNew(dest, find(k.size), find(k.initialValue)));
    }

    public void visit(KNeg k) {
        code.add(new SubI(dest, cons(0), find(k.op)));
    }

    public void visit(KAdd k) {
        code.add(new AddI(dest, find(k.op1), find(k.op2)));
    }

    public void visit(KSub k) {
        code.add(new SubI(dest, find(k.op1), find(k.op2)));
    }

    public void visit(KFNeg k) {
        code.add(new SubF(dest, cons(0f), find(k.op)));
    }

    public void visit(KFAdd k) {
        code.add(new AddF(dest, find(k.op1), find(k.op2)));
    }

    public void visit(KFSub k) {
        code.add(new SubF(dest, find(k.op1), find(k.op2)));
    }

    public void visit(KFDiv k) {
        code.add(new DivF(dest, find(k.op1), find(k.op2)));
    }

    public void visit(KFMul k) {
        code.add(new MultF(dest, find(k.op1), find(k.op2)));
    }

    public void visit(KGet k) {
        code.add(new ArrGet(dest, find(k.array), find(k.index)));
    }

    public void visit(KPut k) {
        code.add(new ArrPut(find(k.array), find(k.index), find(k.value)));
    }

    public void visit(KLet k) {
        SymDef id = k.id;
        if (id.id.startsWith("?v")) {
            k.initializer.accept(this);
        } else {
            pushVar(newLocal(id));
            k.initializer.accept(this);
            popVar();
        }
        k.ret.accept(this);
    }

    private Var newLocal(SymDef id) {
        Var v = new Var(id.id, id.type);
        locals.put(id.id, v);
        return v;
    }

    public void visit(KLetRec k) {
        // fixme ?
        throw new RuntimeException(
                "Unexpected `KLetRec`; expecting closure-converted code"
        );
    }

    public void visit(KLetTuple k) {
        int i, n = k.ids.size();
        Var array = find(k.initializer);
        for (i = 0; i < n; ++i) {
            Var dest = newLocal(k.ids.get(i));
            code.add(new ArrGet(dest, array, cons(i)));
        }
        k.ret.accept(this);
    }

    private void emitBranch(
            boolean lessOrEqual,
            SymRef op1, SymRef op2,
            KNode kThen, KNode kElse
    ) {
        Label lThen = Label.gen();
        Label lElse = Label.gen();
        Label lEnd = Label.gen();
        code.add(new Branch(
                lessOrEqual,
                find(op1), find(op2),
                lThen, lElse
        ));
        code.add(lThen);
        kThen.accept(this);
        code.add(new Jump(lEnd));
        code.add(lElse);
        kElse.accept(this);
        code.add(lEnd);
    }

    public void visit(KIfEq k) {
        emitBranch(false, k.op1, k.op2, k.kThen, k.kElse);
    }

    public void visit(KIfLE k) {
        emitBranch(true, k.op1, k.op2, k.kThen, k.kElse);
    }

    public void visit(KApp k) {
        // Throw: Unexpected `KApp` in closure-converted code ?
        List<Operand> args = new ArrayList<>(k.args.size());
        for (SymRef ref : k.args) {
            args.add(find(ref));
        }
        String id = k.f.id;
        int arity = this.arity.get(id);
        if (arity > args.size()) {
            throw new RuntimeException(
                    "Sorry, currying not supported for: " + id
            );
        }
        if (arity == 0) args.clear();
        code.add(new DirApply(dest, id, args));
    }

    public void visit(ApplyClosure k) {
        List<Operand> args = new ArrayList<>(k.boundArguments.size());
        for (SymRef ref : k.boundArguments) {
            args.add(find(ref));
        }
        // fixme: implement currying or change type system to prevent it
        // since we can't prevent it at compile-time, will crash at runtime
        code.add(new ClsApply(dest, find(k.functionObj), args));
    }

    public void visit(ApplyDirect k) {
        List<Operand> args = new ArrayList<>(k.args.size());
        for (SymRef ref : k.args) {
            args.add(find(ref));
        }

        // Name of the function to call
        String id = k.functionName.id;
        Type t = globals.get(k.functionName);
        int arity = this.arity.get(id);
        if (arity > args.size()) {
            throw new RuntimeException(
                    "Sorry, currying not supported for: " + id
            );
        }
        if (arity == 0) args.clear();

        code.add(new DirApply(returnsVoid(t) ? null : dest, id, args));
    }

    public void visit(ClosureMake k) {
        List<Operand> args = new ArrayList<>(k.freeArguments.size());
        for (SymRef ref : k.freeArguments) {
            args.add(find(ref));
        }
        /*
        // fixme: is this necessary?
        String id = k.functionName.id;
        int arity = this.arityFree.get(id);
        if (arity > args.size()) {
            throw new RuntimeException(
                    "Sorry, currying not supported for: " + id
            );
        }
        if (arity == 0) args.clear();
        */

        pushVar(newLocal(k.target));
        code.add(new ClsMake(dest, new Label(k.functionName.id), args));
        popVar();
        k.ret.accept(this);
    }
}
