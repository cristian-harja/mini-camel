package mini_camel.transform;

import mini_camel.Id;
import mini_camel.SymTable;
import mini_camel.ast.AstExp;
import mini_camel.ast.AstLet;
import mini_camel.ast.AstVar;

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
}
