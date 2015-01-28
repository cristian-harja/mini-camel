package mini_camel.visit;

import mini_camel.ast.AstExp;
import mini_camel.ast.AstLet;
import mini_camel.ast.AstLetRec;
import mini_camel.util.SymRef;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Set;

@ParametersAreNonnullByDefault
public final class UnusedElim extends TransformHelper {

    private Set<String> unused;

    private UnusedElim(AstExp e) {
        unused = UnusedVar.compute(e);
    }

    /**
     * This is the method that initiates the actual transformation.
     *
     * @param astNode input AST
     * @return transformed AST
     */
    public static AstExp compute(AstExp astNode) {
        return astNode.accept(new UnusedElim(astNode));
    }

    @Nonnull
    public AstExp visit(AstLet e) {
        if (unused.contains(e.decl.id)) {
            return e.ret.accept(this);
        }
        return super.visit(e);
    }


}
