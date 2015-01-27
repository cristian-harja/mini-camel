package mini_camel.visit;

import mini_camel.ast.*;
import mini_camel.util.SymRef;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public class Elim extends TransformHelper2<Elim.Ctx> {

    public Elim(){}

    public static AstExp compute(AstExp astNode) {
        UnusedVar cff = new UnusedVar();
        Set<String> unused = cff.applyTransform(astNode);
        Ctx ctx = new Ctx(unused);
        return astNode.accept(new Elim(), ctx);
    }

    public static class Ctx {
        private Set<String> unused = new HashSet<>();

        private Ctx(Set<String> s) {unused = s;}

        private Ctx() {}

    }

    /**
     * This is the method that initiates the actual transformation.
     *
     * @param astNode input AST
     * @return transformed AST
     */
    public AstExp applyTransform(@Nonnull AstExp astNode) {
        UnusedVar cff = new UnusedVar();
        Set<String> unused = cff.applyTransform(astNode);
        Ctx ctx = new Ctx(unused);
        AstExp e = recursiveVisit(ctx, astNode);
        return e;

    }

    /**
     * When encountering a `let` expression, we try to rename the identifier
     * bound by it to something unique within the current environment.
     */
    @Override
    public AstExp visit(Ctx ctx, @Nonnull AstLet e) {
        if(ctx.unused.contains(e.decl.id))
        {
            return recursiveVisit(ctx, e.ret);
        }
        return super.visit(ctx, e);
    }

    /**
     * When encountering the usage of a variable, we check whether it was
     * renamed by the current transformation and return its new name.
     */
    @Override
    public AstExp visit(Ctx ctx, @Nonnull SymRef e) {
        if(ctx.unused.contains(e.toString()))
        {
            return null;
        }
        return e;
    }


    @Override
    public AstExp visit(Ctx ctx, @Nonnull AstLetRec e) {
        return e;

    }


}
