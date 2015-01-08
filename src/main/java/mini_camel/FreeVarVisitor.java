package mini_camel;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import mini_camel.ast.*;

import java.util.HashSet;
import java.util.Set;

public class FreeVarVisitor implements Visitor{

    Multiset<String> bound = HashMultiset.create();
    Set<String> free = new HashSet<>();

    @Override
    public void visit(AstUnit e) {
        //nothing
    }

    @Override
    public void visit(AstBool e) {
        //nothing
    }

    @Override
    public void visit(AstInt e) {
        //nothing
    }

    @Override
    public void visit(AstFloat e) {
        //nothing
    }

    @Override
    public void visit(AstNot e) {
        e.e.accept(this); //visite e' (not e')
    }

    @Override
    public void visit(AstNeg e) {
        e.e.accept(this); //visite e' (neg e')
    }

    @Override
    public void visit(AstAdd e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(AstSub e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(AstFNeg e) {
        e.e.accept(this);
    }

    @Override
    public void visit(AstFAdd e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(AstFSub e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(AstFMul e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(AstFDiv e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(AstEq e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(AstLE e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(AstIf e) {
        e.e1.accept(this);
        e.e2.accept(this);
        e.e3.accept(this);
    }

    @Override
    public void visit(AstLet e) {
        e.e1.accept(this);
        bound.add(e.id.id);
        e.e2.accept(this);
        bound.remove(e.id.id);
    }

    @Override
    public void visit(AstVar e) {
        String id = e.id.id;
        if (bound.contains(id)) return;
        free.add(id);
    }

    @Override
    public void visit(AstLetRec e) {
        bound.add(e.fd.id.id);
        e.fd.accept(this);
        e.e.accept(this);
        bound.remove(e.fd.id.id);
    }

    @Override
    public void visit(AstApp e) {
        e.e.accept(this);
        for (AstExp item : e.es) {
            item.accept(this);
        }
    }

    @Override
    public void visit(AstTuple e) {
        for (AstExp item : e.es) {
            item.accept(this);
        }
    }

    @Override
    public void visit(AstLetTuple e) {
        e.e1.accept(this);
        for (Id item : e.ids) {
            bound.add(item.id);
        }
        e.e2.accept(this);
    }

    @Override
    public void visit(AstArray e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(AstGet e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(AstPut e) {
        e.e1.accept(this);
        e.e2.accept(this);
        e.e3.accept(this);
    }

    @Override
    public void visit(AstFunDef e) {
        for (Id item : e.args) {
            bound.add(item.id);
        }
        e.e.accept(this);
        for (Id item : e.args) {
            bound.remove(item.id);
        }
    }
}
