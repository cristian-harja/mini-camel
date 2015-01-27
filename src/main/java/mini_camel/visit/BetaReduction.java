package mini_camel.visit;

import mini_camel.ast.AstSymDef;
import mini_camel.util.SymTable;
import mini_camel.ast.*;

import javax.annotation.Nonnull;

public final class BetaReduction extends TransformHelper {

    private SymTable<AstSymRef> reMapping = new SymTable<>();

    private BetaReduction() {
    }

    public static AstExp compute(AstExp astNode) {
        return astNode.accept(new BetaReduction());
    }


    public AstExp visit(@Nonnull AstSymRef e) {
        AstExp new_e = reMapping.get(e.id);
        return (new_e == null) ? e : new_e;
    }


    public AstExp visit(@Nonnull AstLet e){
        AstSymDef old_id = e.decl;
        AstExp new_e1 = e.initializer.accept(this);
        AstExp new_e2;

        if(new_e1 instanceof AstSymRef){
            reMapping.push();
            {
                reMapping.put(old_id.id,(AstSymRef)new_e1);
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
