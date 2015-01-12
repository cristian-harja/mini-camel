package mini_camel.transform;

import mini_camel.Id;
import mini_camel.SymTable;
import mini_camel.ast.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Implements &alpha;-conversion, which is a transformation on ASTs that
 * makes sure no symbol name is bound twice by nested {@code let} expressions.
 * </p>
 * <p>Example: {@code let x = 3 in let x = 6 in x + x}. This will be turned
 * into something like {@code let x_1 = 3 in let x_2 = 6 in x_2 + x_2}, which
 * is used as input to other stages of compilation.
 * </p>
 */
public final class AlphaConv extends AstTransformHelper<AlphaConv.Ctx> {

    /**
     * A helper class, providing context (the first argument) to the recursive
     * calls in {@link AlphaConv AlphaConv}. In this particular case, it has
     * (among other things) a symbol table to keep track of renamed symbols.
     */
    public static class Ctx {
        // To help generate new unique variable names.
        private int lastId = 0;

        // For mapping renamed variables.
        private SymTable<Id> reMapping = new SymTable<>();

        // This class can only be instantiated from within `AlphaConv`.
        private Ctx() {}

        // The method that renames an identifier.
        private Id newId(Id id) {
            return new Id(id.id + "_" + (++lastId));
        }
    }

    /**
     * This is the method that initiates the actual transformation.
     *
     * @param astNode input AST
     * @return transformed AST
     */
    public AstExp applyTransform(@Nonnull AstExp astNode) {
        return recursiveVisit(new Ctx(), astNode);
    }

    /**
     * When encountering a `let` expression, we try to rename the identifier
     * bound by it to something unique within the current environment.
     */
    @Override
    public AstExp visit(Ctx ctx, @Nonnull AstLet e) {
        // Rename the identifier introduced by this `let` expression
        Id old_id = e.id;
        Id new_id = ctx.newId(old_id);

        // Recursively perform any modifications on the body of the `let`
        AstExp new_e1 = recursiveVisit(ctx, e.e1);
        AstExp new_e2;

        ctx.reMapping.push();
        {
            ctx.reMapping.put(old_id.id, new_id);
            new_e2 = recursiveVisit(ctx, e.e2);
        }
        ctx.reMapping.pop();

        return new AstLet(new_id, e.id_type, new_e1, new_e2);
    }

    /**
     * When encountering the usage of a variable, we check whether it was
     * renamed by the current transformation and return its new name.
     */
    @Override
    public AstExp visit(Ctx ctx, @Nonnull AstVar e) {
        // Locate (in the environment) the symbol being referenced here.
        Id old_id = e.id;
        Id new_id = ctx.reMapping.get(old_id.id);

        // If the symbol was not found, return the old name.
        if (new_id == null || new_id.id.equals(old_id.id)) return e;

        // If the name was changed, return the new AST node.
        return new AstVar(new_id);
    }


    @Override
    public AstExp visit(Ctx ctx, @Nonnull AstLetRec e) {
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
    public AstExp visit(Ctx ctx, @Nonnull AstFunDef e) {
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
        return new AstFunDef(new_id, new_args, new_e);
    }


    @Override
    public AstExp visit(Ctx ctx, @Nonnull AstApp e) {
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
