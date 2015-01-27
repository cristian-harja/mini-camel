package mini_camel.visit;

import mini_camel.ast.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class Inlining extends TransformHelper2<Inlining.Ctx> {

    public Inlining() {
    }

    public static AstExp compute(AstExp astNode) {
        FunNumOp in = new FunNumOp();
        List<String> l = in.applyTransform(astNode);

        RecursiveCheck r = new RecursiveCheck();
        List<String> rf = r.applyTransform(astNode);

        l.removeAll(rf);

        if (l.size() == 0) {
            return astNode;
        }

        Ctx ctx = new Ctx(l);
        return astNode.accept(new Inlining(), ctx);
    }

    public static class Ctx {
        private List<String> l;
        private List<AstFunDef> afd;

        private Ctx(){}

        private Ctx(List<String> l) {
            this.l = l;
            this.afd = new ArrayList<>();
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
        AstExp e = recursiveVisit(ctx, astNode);
        return e;
    }

    public AstExp visit(Ctx ctx, @Nonnull AstLetRec e) {
        if (ctx.l.contains(e.fd.decl.id)) {
            ctx.afd.add(e.fd);
        }
        return recursiveVisit(ctx, e.ret);
    }


    public AstExp visit(Ctx ctx, @Nonnull AstApp e)
    {
        int index = 0;
        for(AstFunDef iterator : ctx.afd)
        {
            if(iterator.decl.id.equals(e.e.toString()))
            {
                break;
            }
            index++;
        }
        int bool = 0;
        for(AstExp iterator : e.es)
        {
            if(iterator instanceof AstApp)
            {
                bool ++;
            }
        }
        if(bool == 0)
        {
            return inline(ctx.afd.get(index), e);
        }
        AstExp tmp = recursiveVisit(ctx,e.es.get(0));
        AstLet tmp2 = inline(ctx.afd.get(index), e);
        return new AstLet(
                ctx.afd.get(index).args.get(0),
                tmp, ctx.afd.get(index).body
        );

    }


    public AstLet inline(AstFunDef fd, AstApp e) {
        AstLet l = null;
        if (fd.args.size() == 1) {
            l = new AstLet(fd.args.get(0), e.es.get(0), fd.body);
        } else {
            l = new AstLet(
                    fd.args.get(fd.args.size() - 1),
                    e.es.get(fd.args.size() - 1),
                    fd.body
            );
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
