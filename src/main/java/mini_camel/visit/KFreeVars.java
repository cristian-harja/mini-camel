package mini_camel.visit;

import mini_camel.knorm.*;
import mini_camel.type.Type;
import mini_camel.util.SymDef;
import mini_camel.util.SymRef;
import mini_camel.util.SymTable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@ParametersAreNonnullByDefault
public final class KFreeVars extends KDummyVisitor {

    private List<SymRef> free = new ArrayList<>();
    private SymTable<Type> symbolTable = new SymTable<>();

    private KFreeVars() {
    }

    public static List<SymRef> compute(KNode root) {
        KFreeVars fv = new KFreeVars();
        root.accept(fv);
        return fv.free;
    }

    public void visit(KLet k) {
        symbolTable.push();
        {
            k.initializer.accept(this);
            symbolTable.put(k.id.id, k.id.type);
            k.ret.accept(this);
        }
        symbolTable.pop();
    }

    public void visit(KLetRec k) {
        symbolTable.push();
        {
            symbolTable.put(k.fd.name.id, k.fd.name.type);
            k.fd.body.accept(this);
            for (SymDef arg : k.fd.args) {
                symbolTable.put(arg.id, arg.type);
            }
            k.ret.accept(this);
        }
        symbolTable.pop();
    }

    public void visit(KLetTuple k) {
        symbolTable.push();
        {
            freeCheck(k.initializer);
            for (SymDef def : k.ids) {
                symbolTable.put(def.id, def.type);
            }
            k.ret.accept(this);
        }
        symbolTable.pop();

        k.ret.accept(this);
    }

    public void visit(ApplyClosure k) {
        freeCheck(k.functionObj);
        freeCheck(k.boundArguments);
    }

    public void visit(ApplyDirect k) {
        freeCheck(k.functionName);
        freeCheck(k.args);
    }

    public void visit(ClosureMake k) {
        freeCheck(k.functionName);
        freeCheck(k.freeArguments);
    }

    private void freeCheck(SymRef ref) {
        if (symbolTable.get(ref.id) != null) return;
        free.add(ref);
    }

    private void freeCheck(Collection<SymRef> refs) {
        for (SymRef ref : refs) {
            freeCheck(ref);
        }
    }

    public void visit(KVar k) {
        freeCheck(k.var);
    }

    public void visit(KTuple k) {
        freeCheck(k.items);
    }

    public void visit(KNeg k) {
        freeCheck(k.op);
    }

    public void visit(KAdd k) {
        freeCheck(k.op1);
        freeCheck(k.op2);
    }

    public void visit(KSub k) {
        freeCheck(k.op1);
        freeCheck(k.op2);
    }

    public void visit(KFNeg k) {
        freeCheck(k.op);
    }

    public void visit(KFAdd k) {
        freeCheck(k.op1);
        freeCheck(k.op2);
    }

    public void visit(KFSub k) {
        freeCheck(k.op1);
        freeCheck(k.op2);
    }

    public void visit(KFMul k) {
        freeCheck(k.op1);
        freeCheck(k.op2);
    }

    public void visit(KFDiv k) {
        freeCheck(k.op1);
        freeCheck(k.op2);
    }

    public void visit(KGet k) {
        freeCheck(k.array);
        freeCheck(k.index);
    }

    public void visit(KPut k) {
        freeCheck(k.array);
        freeCheck(k.index);
        freeCheck(k.value);
    }

    public void visit(KIfEq k) {
        freeCheck(k.op1);
        freeCheck(k.op2);
        super.visit(k);
    }

    public void visit(KIfLE k) {
        freeCheck(k.op1);
        freeCheck(k.op2);
        super.visit(k);
    }

    public void visit(KApp k) {
        freeCheck(k.f);
        freeCheck(k.args);
    }


}
