package mini_camel.transform;

import mini_camel.Id;
import mini_camel.SymTable;
import mini_camel.ast.*;

import java.util.ArrayList;
import java.util.List;

public class AlphaConv extends AstTransformHelper<AlphaConv.Ctx> {


    public static class Ctx {
        private int lastId = 0;

        private Ctx() {}
        private SymTable<Id> reMapping = new SymTable<>();

        private Id newId(Id id) {
            return new Id(id.id + "_" + (++lastId));
        }
    }

    public AstExp applyTransform(AstExp astNode) {
        return recursiveVisit(new Ctx(), astNode);
    }

    @Override
    public AstExp visit(Ctx ctx, AstLet e) {
        Id old_id = e.id;
        Id new_id = ctx.newId(old_id);
        AstExp new_e1 = recursiveVisit(ctx, e.e1);
        AstExp new_e2;

        ctx.reMapping.push();
        {
            ctx.reMapping.put(old_id.id, new_id);
            new_e2 = recursiveVisit(ctx, e.e2);
        }
        ctx.reMapping.pop();

        return new AstLet(new_id, e.t, new_e1, new_e2);
    }

    @Override
    public AstExp visit(Ctx ctx, AstVar e) {
        Id old_id = e.id;
        Id new_id = ctx.reMapping.get(old_id.id);
        if (new_id == null || new_id.id.equals(old_id.id)) return e;
        return new AstVar(new_id);
    }


    @Override
    public AstExp visit(Ctx ctx, AstLetRec e) {
        AstFunDef old_fd = e.fd;
        Id old_id = old_fd.id;
        AstFunDef new_fd = (AstFunDef)recursiveVisit(ctx, old_fd);
        Id new_id = new_fd.id;
        AstExp old_e = e.e;
        AstExp new_e;

        ctx.reMapping.push();
        {
            ctx.reMapping.put(old_id.id, new_id);
            new_e = recursiveVisit(ctx, old_e);
        }
        ctx.reMapping.pop();

        // if (new_id == null || new_id.id.equals(old_id.id)) return e;
        if (old_fd == new_fd && old_e == new_e) return e;

        return new AstLetRec(new_fd, new_e);
    }


    @Override
    public AstExp visit(Ctx ctx, AstFunDef e) {
        Id old_id = e.id;
        Id new_id = ctx.newId(old_id);

        AstExp old_e = e.e;
        AstExp new_e;

        List<Id> old_args = e.args;
        List<Id> new_args = new ArrayList<>();



        ctx.reMapping.push();
        {
            ctx.reMapping.put(old_id.id, new_id);
            for (Id old_arg : old_args) {
                Id new_arg = ctx.newId(old_arg);
                new_args.add(new_arg);
                ctx.reMapping.put(old_arg.id, new_arg);
            }
            new_e = recursiveVisit(ctx, old_e);
        }
        ctx.reMapping.pop();

        if (new_id == old_id && new_args.equals(old_args) && new_e == old_e) return e;
        return new AstFunDef(new_id, e.type, new_args, new_e);
    }


    @Override
    public AstExp visit(Ctx ctx, AstApp e) {
        AstExp old_e = e.e;
        AstExp new_e;

        List<AstExp> old_es = e.es;
        List<AstExp> new_es = new ArrayList<>();

        ctx.reMapping.push();
        {
            for (AstExp old_esi : old_es) {
                new_es.add(recursiveVisit(ctx, old_esi));
            }
            new_e = recursiveVisit(ctx, old_e);
        }
        ctx.reMapping.pop();

        if (new_e == old_e && new_es.equals(old_es)) return e;
        return new AstApp(new_e, new_es);
    }

}
