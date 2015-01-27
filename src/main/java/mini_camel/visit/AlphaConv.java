package mini_camel.visit;

import mini_camel.ast.AstSymDef;
import mini_camel.util.SymTable;
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
public final class AlphaConv extends TransformHelper {

    // To help generate new unique variable names.
    private int lastId = 0;

    // For mapping renamed variables.
    private SymTable<String> reMapping = new SymTable<>();

    // The method that renames an identifier.
    private String newId(String name) {
        return name + "_" + (++lastId);
    }

    private AlphaConv() {
    }

    /**
     * This is the method that initiates the actual transformation.
     *
     * @param astNode input AST
     * @return transformed AST
     */
    public static AstExp compute(@Nonnull AstExp astNode) {
        return astNode.accept(new AlphaConv());
    }

    /**
     * When encountering a `let` expression, we try to rename the identifier
     * bound by it to something unique within the current environment.
     */
    @Override
    public AstExp visit(@Nonnull AstLet e) {
        // Rename the identifier introduced by this `let` expression
        AstSymDef old_id = e.decl;
        AstSymDef new_id = old_id.rename(newId(old_id.id));

        // Recursively perform any modifications on the body of the `let`
        AstExp new_e1 = e.initializer.accept(this);
        AstExp new_e2;

        reMapping.push();
        {
            reMapping.put(old_id.id, new_id.id);
            new_e2 = e.ret.accept(this);
        }
        reMapping.pop();

        return new AstLet(new_id, new_e1, new_e2);
    }

    /**
     * When encountering the usage of a variable, we check whether it was
     * renamed by the current transformation and return its new name.
     */
    @Override
    public AstExp visit(@Nonnull AstSymRef e) {
        // Locate (in the environment) the symbol being referenced here.
        String old_id = e.id;
        String new_id = reMapping.get(old_id);

        // If the symbol was not found, return the old name.
        if (new_id == null || new_id.equals(old_id)) return e;

        // If the name was changed, return the new AST node.
        return new AstSymRef(new_id);
    }


    @Override
    public AstExp visit(@Nonnull AstLetRec e) {
        AstFunDef old_fd = e.fd;
        AstFunDef new_fd = (AstFunDef) old_fd.accept(this);
        AstExp old_e = e.ret;
        AstExp new_e;

        reMapping.push();
        {
            reMapping.put(old_fd.decl.id, new_fd.decl.id);
            new_e = old_e.accept(this);
        }
        reMapping.pop();

        // if (new_id == null || new_id.id.equals(old_id.id)) return e;
        if (old_fd == new_fd && old_e == new_e) return e;

        return new AstLetRec(new_fd, new_e);
    }


    @Override
    public AstExp visit(@Nonnull AstFunDef e) {
        AstSymDef old_id = e.decl;
        AstSymDef new_id = old_id.rename(newId(old_id.id));

        AstExp old_e = e.body;
        AstExp new_e;

        List<AstSymDef> old_args = e.args;
        List<AstSymDef> new_args = new ArrayList<>();


        reMapping.push();
        {
            reMapping.put(old_id.id, new_id.id);
            for (AstSymDef old_arg : old_args) {
                AstSymDef new_arg = old_arg.rename(newId(old_arg.id));
                new_args.add(new_arg);
                reMapping.put(old_arg.id, new_arg.id);
            }
            new_e = old_e.accept(this);
        }
        reMapping.pop();

        if (new_id == old_id && new_args.equals(old_args) && new_e == old_e) return e;
        return new AstFunDef(new_id, new_args, new_e);
    }


    @Override
    public AstExp visit(@Nonnull AstApp e) {
        AstExp old_e = e.e;
        AstExp new_e;

        List<AstExp> old_es = e.es;
        List<AstExp> new_es = new ArrayList<>();

        reMapping.push();
        {
            for (AstExp old_esi : old_es) {
                new_es.add(old_esi.accept(this));
            }
            new_e = old_e.accept(this);
        }
        reMapping.pop();

        if (new_e == old_e && new_es.equals(old_es)) return e;
        return new AstApp(new_e, new_es);
    }

}
