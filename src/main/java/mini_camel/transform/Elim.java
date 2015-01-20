package mini_camel.transform;

import mini_camel.SymTable;
import mini_camel.ast.*;
import mini_camel.ir.Var;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lina on 1/19/15.
 */
public class Elim extends AstTransformHelper<Elim.Ctx> {

    public static class Ctx {
        private Set<String> unused = new HashSet<>();

        private Ctx(Set<String> s) {unused = s;}

    }

    public Elim(){}

    /**
     * This is the method that initiates the actual transformation.
     *
     * @param astNode input AST
     * @return transformed AST
     */
    public void applyTransform(@Nonnull AstExp astNode, @Nonnull Set<String> s) {
        Ctx ctx = new Ctx(s);
        recursiveVisit(ctx, astNode);
        /*for(String iterator : ctx.left)
        {
            System.out.println("Elements à gauche : "+iterator.toString());
        }
        for(String iterator : ctx.right)
        {
            System.out.println("Elements à droite : "+iterator.toString());
        }*/
        ctx.left.removeAll(ctx.right);
        for(String iterator : ctx.left)
        {
            System.out.println("Unused : "+iterator.toString());
        }

    }

    /**
     * When encountering a `let` expression, we try to rename the identifier
     * bound by it to something unique within the current environment.
     */
    @Override
    public AstExp visit(Ctx ctx, @Nonnull AstLet e) {
        ctx.left.add(e.id.id);
        recursiveVisit(ctx, e.e1);
        recursiveVisit(ctx, e.e2);
        return null;
    }

    /**
     * When encountering the usage of a variable, we check whether it was
     * renamed by the current transformation and return its new name.
     */
    @Override
    public AstExp visit(Ctx ctx, @Nonnull AstVar e) {
        ctx.right.add(e.id.id);
        return null;
    }


    @Override
    public AstExp visit(Ctx ctx, @Nonnull AstLetRec e) {
        return null;

    }


    @Override
    public AstExp visit(Ctx ctx, @Nonnull AstFunDef e) {
        return null;
    }


    @Override
    public AstExp visit(Ctx ctx, @Nonnull AstApp e) {
         return null;
    }



}
