package mini_camel.transform;

import mini_camel.Id;
import mini_camel.SymTable;
import mini_camel.ast.*;
/**
 * Created by mommess on 09/01/15.
 */
public class BetaReduc extends AstTransformHelper<BetaReduc.Ctx> {

    public static class Ctx {
        private SymTable<AstVar> reMapping = new SymTable<>();

        private Ctx() {}

    }

    public AstExp applyTransform(AstExp astNode) {
        return recursiveVisit(new Ctx(), astNode);
    }


    public AstExp visit(Ctx ctx, AstVar e) {
        AstExp new_e = ctx.reMapping.get(e.id.id);
        return (new_e == null) ? e : new_e;
    }


    public AstExp visit(Ctx ctx, AstLet e){
        Id old_id = e.id;
        AstExp new_e1 = recursiveVisit(ctx, e.e1);
        AstExp new_e2;

        if(new_e1 instanceof AstVar){
            ctx.reMapping.push();
            {
                ctx.reMapping.put(old_id.id,(AstVar)new_e1);
                new_e2 = recursiveVisit(ctx, e.e2);
            }
            ctx.reMapping.pop();
        }
        else {
            new_e2 = recursiveVisit(ctx, e.e2);
        }

        return new AstLet(old_id, e.t, new_e1, new_e2);
    }



}
