package mini_camel.visit;

import mini_camel.util.SymDef;
import mini_camel.util.SymRef;
import mini_camel.util.SymTable;
import mini_camel.ast.*;

import javax.annotation.Nonnull;

public final class BetaReduction extends TransformHelper {

    private SymTable<SymRef> reMapping = new SymTable<>();

    private BetaReduction() {
    }

    public static AstExp compute(AstExp astNode) {
        return astNode.accept(new BetaReduction());
    }


    public AstExp visit(@Nonnull SymRef e) {
        AstExp new_e = reMapping.get(e.id);
        return (new_e == null) ? e : new_e;
    }


    public AstExp visit(@Nonnull AstLet e){
        SymDef old_id = e.decl;
        AstExp new_e1 = e.initializer.accept(this);
        AstExp new_e2;

        if(new_e1 instanceof SymRef){
            reMapping.push();
            {
                reMapping.put(old_id.id,(SymRef)new_e1);
                new_e2 = e.ret.accept(this);
            }
            reMapping.pop();
        }
        else {
            new_e2 = e.ret.accept(this);
        }

        return new AstLet(old_id, new_e1, new_e2);
    }



}
