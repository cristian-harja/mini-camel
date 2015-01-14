package mini_camel;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import mini_camel.ast.*;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public class FreeVarVisitor implements Visitor{

    Multiset<String> bound = HashMultiset.create();
    Set<String> free = new HashSet<>();

    @Override
    public void visit(@Nonnull AstUnit e) {
        //nothing
    }

    @Override
    public void visit(@Nonnull AstBool e) {
        //nothing
    }

    @Override
    public void visit(@Nonnull AstInt e) {
        //nothing
    }

    @Override
    public void visit(@Nonnull AstFloat e) {
        //nothing
    }

    @Override
    public void visit(@Nonnull AstNot e) {
        e.e.accept(this); //visite e' (not e')
    }

    @Override
    public void visit(@Nonnull AstNeg e) {
        e.e.accept(this); //visite e' (neg e')
    }

    @Override
    public void visit(@Nonnull AstAdd e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(@Nonnull AstSub e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(@Nonnull AstFNeg e) {
        e.e.accept(this);
    }

    @Override
    public void visit(@Nonnull AstFAdd e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(@Nonnull AstFSub e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(@Nonnull AstFMul e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(@Nonnull AstFDiv e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(@Nonnull AstEq e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(@Nonnull AstLE e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(@Nonnull AstIf e) {
        e.e1.accept(this);
        e.e2.accept(this);
        e.e3.accept(this);
    }

    @Override
    public void visit(@Nonnull AstLet e) {
        e.e1.accept(this);
        bound.add(e.id.id);
        e.e2.accept(this);
        bound.remove(e.id.id);
    }

    @Override
    public void visit(@Nonnull AstVar e) {
        String id = e.id.id;
        if (bound.contains(id)) return;
        free.add(id);
    }

    @Override
    public void visit(@Nonnull AstLetRec e) {
        bound.add(e.fd.id.id);
        e.fd.accept(this);
        e.e.accept(this);
        bound.remove(e.fd.id.id);
    }

    @Override
    public void visit(@Nonnull AstApp e) {
        e.e.accept(this);
        for (AstExp item : e.es) {
            item.accept(this);
        }
    }

    @Override
    public void visit(@Nonnull AstTuple e) {
        for (AstExp item : e.es) {
            item.accept(this);
        }
    }

    @Override
    public void visit(@Nonnull AstLetTuple e) {
        e.e1.accept(this);
        for (Id item : e.ids) {
            bound.add(item.id);
        }
        e.e2.accept(this);
    }

    @Override
    public void visit(@Nonnull AstArray e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(@Nonnull AstGet e) {
        e.e1.accept(this);
        e.e2.accept(this);
    }

    @Override
    public void visit(@Nonnull AstPut e) {
        e.e1.accept(this);
        e.e2.accept(this);
        e.e3.accept(this);
    }

    @Override
    public void visit(@Nonnull AstFunDef e) {
        for (Id item : e.args) {
            bound.add(item.id);
        }
        e.e.accept(this);
        for (Id item : e.args) {
            bound.remove(item.id);
        }
    }
}
