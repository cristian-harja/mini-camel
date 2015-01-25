package mini_camel.visit;

import mini_camel.ast.Id;
import mini_camel.util.SymTable;
import mini_camel.ast.*;

import javax.annotation.Nonnull;

public final class BetaReduction extends TransformHelper {

    private SymTable<AstVar> reMapping = new SymTable<>();

    private BetaReduction() {
    }

    public static AstExp compute(AstExp astNode) {
        return astNode.accept(new BetaReduction());
    }


    public AstExp visit(@Nonnull AstVar e) {
        AstExp new_e = reMapping.get(e.id.id);
        return (new_e == null) ? e : new_e;
    }


    public AstExp visit(@Nonnull AstLet e){
        Id old_id = e.id;
        AstExp new_e1 = e.e1.accept(this);
        AstExp new_e2;

        if(new_e1 instanceof AstVar){
            reMapping.push();
            {
                reMapping.put(old_id.id,(AstVar)new_e1);
                new_e2 = e.e2.accept(this);
            }
            reMapping.pop();
        }
        else {
            new_e2 = e.e2.accept(this);
        }

        return new AstLet(old_id, e.id_type, new_e1, new_e2);
    }



}
