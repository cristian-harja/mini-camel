package mini_camel.transform;

import mini_camel.ast.Id;
import mini_camel.SymTable;
import mini_camel.ast.*;

import javax.annotation.Nonnull;

/**
 * Created by mommess on 09/01/15.
 */
public class ConstantFold extends AstTransformHelper<ConstantFold.Ctx> {

    public static class Ctx {
        private SymTable<AstExp> reMapping = new SymTable<>();

        private Ctx() {}

    }

    public AstExp applyTransform(AstExp astNode) {
        return recursiveVisit(new Ctx(), astNode);
    }


    //Example : if x = 5 is in the symbol table, it will return an AstInt of value 5
    // otherwise it does nothing
    public AstExp visit(Ctx ctx, @Nonnull AstVar e) {
        AstExp new_e = ctx.reMapping.get(e.id.id);
        return (new_e == null) ? e : new_e;
    }


    public AstExp visit(Ctx ctx, @Nonnull AstLet e){
        Id old_id = e.id;
        AstExp new_e1 = recursiveVisit(ctx, e.e1);
        AstExp new_e2;


        ctx.reMapping.push();
        {
            // Puts the mapping e.id -> e.e1 (its value) in the stack and transforms the expression e.e2
            ctx.reMapping.put(old_id.id, new_e1);
            new_e2 = recursiveVisit(ctx, e.e2);
        }
        ctx.reMapping.pop();

        return new AstLet(old_id, e.id_type, new_e1, new_e2);
    }



    public AstExp visit(Ctx ctx, @Nonnull AstAdd e){
        AstExp new_e1 = recursiveVisit(ctx, e.e1);
        AstExp new_e2 = recursiveVisit(ctx, e.e2);

        if(new_e1 instanceof AstInt && new_e2 instanceof  AstInt){
            int val = ((AstInt)new_e1).i + ((AstInt)new_e2).i;
            return new AstInt(val);
        }

        return new AstAdd(new_e1, new_e2);
    }


    public AstExp visit(Ctx ctx, @Nonnull AstSub e){
        AstExp new_e1 = recursiveVisit(ctx, e.e1);
        AstExp new_e2 = recursiveVisit(ctx, e.e2);


        if(new_e1 instanceof AstInt && new_e2 instanceof  AstInt){
            int val = ((AstInt)new_e1).i - ((AstInt)new_e2).i;
            return new AstInt(val);
        }

        return new AstSub(new_e1, new_e2);
    }


    public AstExp visit(Ctx ctx, @Nonnull AstNeg e){
        AstExp new_e = recursiveVisit(ctx, e.e);

        if(new_e instanceof AstInt){
            int val = - ((AstInt)new_e).i;
            return new AstInt(val);
        }

        return new AstNeg(new_e);
    }


    public AstExp visit(Ctx ctx, @Nonnull AstFNeg e){
        AstExp new_e = recursiveVisit(ctx, e.e);

        if(new_e instanceof AstFloat){
            float val = - ((AstFloat)new_e).f;
            return new AstFloat(val);
        }

        return new AstNeg(new_e);
    }


    public AstExp visit(Ctx ctx, @Nonnull AstFAdd e){
        AstExp new_e1 = recursiveVisit(ctx, e.e1);
        AstExp new_e2 = recursiveVisit(ctx, e.e2);

        if(new_e1 instanceof AstFloat && new_e2 instanceof  AstFloat){
            float val = ((AstFloat)new_e1).f + ((AstFloat)new_e2).f;
            return new AstFloat(val);
        }

        return new AstFAdd(new_e1, new_e2);
    }


    public AstExp visit(Ctx ctx, @Nonnull AstFSub e){
        AstExp new_e1 = recursiveVisit(ctx, e.e1);
        AstExp new_e2 = recursiveVisit(ctx, e.e2);

        if(new_e1 instanceof AstFloat && new_e2 instanceof  AstFloat){
            float val = ((AstFloat)new_e1).f - ((AstFloat)new_e2).f;
            return new AstFloat(val);
        }

        return new AstFAdd(new_e1, new_e2);
    }


    public AstExp visit(Ctx ctx, @Nonnull AstFMul e){
        AstExp new_e1 = recursiveVisit(ctx, e.e1);
        AstExp new_e2 = recursiveVisit(ctx, e.e2);

        if(new_e1 instanceof AstFloat && new_e2 instanceof  AstFloat){
            float val = ((AstFloat)new_e1).f * ((AstFloat)new_e2).f;
            return new AstFloat(val);
        }

        return new AstFAdd(new_e1, new_e2);
    }


    public AstExp visit(Ctx ctx, @Nonnull AstFDiv e){
        AstExp new_e1 = recursiveVisit(ctx, e.e1);
        AstExp new_e2 = recursiveVisit(ctx, e.e2);

        if(new_e1 instanceof AstFloat && new_e2 instanceof  AstFloat){
            float val = ((AstFloat)new_e1).f / ((AstFloat)new_e2).f;
            return new AstFloat(val);
        }

        return new AstFAdd(new_e1, new_e2);
    }

}
