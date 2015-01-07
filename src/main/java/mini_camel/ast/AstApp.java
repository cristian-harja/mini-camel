package mini_camel.ast;

import java.util.Collections;
import java.util.List;

public final class AstApp extends AstExp {
    public final AstExp e;
    public final List<AstExp> es;

    public AstApp(AstExp e, List<AstExp> es) {
        this.e = e;
        this.es = Collections.unmodifiableList(es);
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(e);
        boolean first = true;
        sb.append("(");
        for (AstExp l : es){
            if(!first){
                sb.append(", ");
            }
            first = false;
            sb.append(l);
        }
        sb.append("))");
        return sb.toString();
    }
}
