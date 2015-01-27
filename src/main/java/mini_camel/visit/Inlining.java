package mini_camel.visit;

import mini_camel.ast.*;

import javax.annotation.Nonnull;
import java.util.List;

public class Inlining extends TransformHelper2<Inlining.Ctx> {

    public Inlining() {
    }

    public static class Ctx {
        private List<String> l;

        private Ctx(List<String> l) {
            this.l = l;
        }

    }

    /**
     * This is the method that initiates the actual transformation.
     *
     * @param astNode input AST
     * @return transformed AST
     */
    public AstExp applyTransform(@Nonnull AstExp astNode) {
        FunNumOp in = new FunNumOp();
        List<String> l = in.applyTransform(astNode);

        RecursiveCheck r = new RecursiveCheck();
        List<String> rf = r.applyTransform(astNode);

        l.removeAll(rf);

        if (l.size() == 0) {
            return astNode;
        }

        Ctx ctx = new Ctx(l);
        System.out.println("Je recursiveVisit");
        AstExp e = recursiveVisit(ctx, astNode);
        System.out.println("Jai fini");
        return e;
    }

    public AstExp visit(Ctx ctx, @Nonnull AstLetRec e) {
        if (ctx.l.contains(e.fd.decl.id)) {
            System.out.println("Je passe ici " + e.fd.decl.toString());
            if (e.ret instanceof AstApp) {
                System.out.println("App : "+e.ret.toString());
                System.out.println("Expr : "+e.toString());
                return inline(e.fd, (AstApp) e.ret);
            }

        }
        return recursiveVisit(ctx, e.ret);
    }



    public AstLet inline(AstFunDef fd, AstApp e) {
        AstLet l = null;
        if (fd.args.size() == 1) {
            l = new AstLet(fd.args.get(0), e.es.get(0), fd.body);
        } else {
            l = new AstLet(fd.args.get(fd.args.size() - 1), e.es.get(fd.args.size() - 1), fd.body);
            int i = fd.args.size() - 2;
            while (i != -1) {
                AstLet tmp = new AstLet(fd.args.get(i), e.es.get(i), l);
                l = tmp;
                i--;
            }

        }
        return l;
    }


}
