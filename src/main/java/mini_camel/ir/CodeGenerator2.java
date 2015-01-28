package mini_camel.ir;

import mini_camel.ir.instr.*;
import mini_camel.ir.instr.ClsMake;
import mini_camel.ir.op.*;
import mini_camel.knorm.*;
import mini_camel.type.TUnit;
import mini_camel.type.Type;
import mini_camel.util.KVisitor;
import mini_camel.util.SymDef;
import mini_camel.util.SymRef;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
@SuppressWarnings("unused")
public class CodeGenerator2 implements KVisitor {

    private List<Instr> code = new ArrayList<>();
    private Stack<Var> assignStack = new Stack<>();

    private Var dest;

    private Label functionBegin;
    private Map<String, Var> free = new LinkedHashMap<>();
    private Map<String, Var> args = new LinkedHashMap<>();
    private Map<String, Var> locals = new LinkedHashMap<>();
    private Map<String, Label> globals = new LinkedHashMap<>();

    private static final ConstInt CONST_0 = new ConstInt(0);
    private static final ConstInt CONST_1 = new ConstInt(1);

    private CodeGenerator2() {
    }

    private Function getResult() {
        return new Function(
                functionBegin,
                new ArrayList<>(free.values()),
                new ArrayList<>(args.values()),
                new ArrayList<>(locals.values()),
                code
        );
    }

    private static Function compile(KFunDef fd, Map<String, Type> globals) {
        CodeGenerator2 cg = new CodeGenerator2();
        cg.functionBegin = new Label(fd.name.id);

        for (Map.Entry<String, Type> e : globals.entrySet()) {
            cg.globals.put(e.getKey(), new Label(e.getKey()));
        }
        for (SymDef arg : fd.args) {
            cg.args.put(arg.id, new Var(arg.id, arg.type));
        }
        for (SymRef arg : fd.freeVars) {
            cg.free.put(arg.id, new Var(arg.id, null));
        }

        fd.body.accept(cg);

        return cg.getResult();
    }

    public static List<Function> compile(
            Program p,
            Map<String, Type> predefs,
            String mainName
    ) {
        KFunDef fd = new KFunDef(
                new SymDef(mainName, TUnit.INSTANCE),
                Collections.<SymDef>emptyList(),
                p.mainBody
        );

        Collection<KFunDef> topLevel = p.topLevel.values();
        Map<String, Type> globals = new LinkedHashMap<>();
        globals.putAll(predefs);

        for (KFunDef funDef : topLevel) {
            globals.put(funDef.name.id, funDef.name.type);
        }

        List<Function> result = new ArrayList<>(p.topLevel.size() + 1);

        result.add(compile(fd, globals));
        for (KFunDef funDef : topLevel) {
            result.add(compile(funDef, globals));
        }

        return result;
    }


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

        throw new RuntimeException(
                "Unknown reference " + ref.id + " found when generating IR"
        );
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
            code.add(new ArrPut(dest, cons(i), find(k.items.get(0))));
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
        pushVar(newLocal(k.id));
        k.initializer.accept(this);
        popVar();
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
            Var dest = find(k.ids.get(0).makeRef());
            code.add(new ArrGet(dest, array, cons(i)));
        }
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
        code.add(new DirApply(dest, k.f.id, args));
    }

    public void visit(ApplyClosure k) {
        List<Operand> args = new ArrayList<>(k.boundArguments.size());
        for (SymRef ref : k.boundArguments) {
            args.add(find(ref));
        }
        code.add(new ClsApply(dest, find(k.functionObj), args));
    }

    public void visit(ApplyDirect k) {
        List<Operand> args = new ArrayList<>(k.args.size());
        for (SymRef ref : k.args) {
            args.add(find(ref));
        }
        code.add(new DirApply(dest, k.functionName.id, args));
    }

    public void visit(ClosureMake k) {
        List<Operand> args = new ArrayList<>(k.freeArguments.size());
        for (SymRef ref : k.freeArguments) {
            args.add(find(ref));
        }
        code.add(new ClsMake(dest, new Label(k.functionName.id), args));
    }
}
