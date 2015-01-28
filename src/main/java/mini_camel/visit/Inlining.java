package mini_camel.visit;

import mini_camel.ast.*;
import mini_camel.util.SymRef;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
public final class Inlining extends TransformHelper {

    private List<AstFunDef> afd = new ArrayList<>();
    private Collection<String> l;

    public Inlining(Collection<String> functionsToInline) {
        l = functionsToInline;
    }

    public static AstExp compute(AstExp astNode) {
        Map<String, AstFunDef> l = SizeFilter.compute(astNode, 15);
        l.keySet().removeAll(RecursiveCheck.compute(astNode));


        if (l.size() == 0) {
            return astNode;
        }

        Inlining i = new Inlining(l.keySet());
        return astNode.accept(i);
    }

    @Nonnull
    public AstExp visit(AstLetRec e) {
        if (l.contains(e.fd.decl.id)) {
            afd.add(e.fd);

        }
        return new AstLetRec(e.fd, e.ret.accept(this));
    }


    @Nonnull
    public AstExp visit(AstApp e) {
        if(e.e instanceof SymRef)
        {
            if(((SymRef)e.e).id.equals("print_int") ||((SymRef)e.e).id.equals("print_newline")){
                return e;
            }
        }
        int index = 0;
        for (AstFunDef iterator : afd) {
            if (iterator.decl.id.equals(e.e.toString())) {
                break;
            }
            index++;
        }

        int bool = 0;
        for (AstExp iterator : e.es) {
            if (iterator instanceof AstApp) {
                bool++;
            }
        }
        if (bool == 0) {
            return inline(afd.get(index), e);
        }
        AstExp tmp = e.es.get(0).accept(this);
        AstLet tmp2 = inline(afd.get(index), e); // ??? fixme
        return new AstLet(
                afd.get(index).args.get(0),
                tmp, afd.get(index).body
        );

    }


    public AstLet inline(AstFunDef fd, AstApp e) {
        AstLet l;
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

    private static class SizeFilter extends DummyVisitor {
        private int bound;
        private Map<String, AstFunDef> found = new LinkedHashMap<>();

        private SizeFilter(int bound) {
            this.bound = bound;
        }

        public static Map<String, AstFunDef> compute(AstExp root, int bound) {
            SizeFilter sf = new SizeFilter(bound);
            root.accept(sf);
            return sf.found;
        }

        public void visit(AstLetRec e) {
            if(FunctionSize.compute(e.fd.body) < bound)  {
                found.put(e.fd.decl.id, e.fd);
            }
            e.ret.accept(this);
        }
    }

}
