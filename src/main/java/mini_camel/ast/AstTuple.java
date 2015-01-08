package mini_camel.ast;

import java.util.Collections;
import java.util.List;

public final class AstTuple extends AstExp {
    public final List<AstExp> es;

    public AstTuple(List<AstExp> es) {
        this.es = Collections.unmodifiableList(es);
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public <T, U> T accept(Visitor2<T, U> v, U a) {
        return v.visit(a, this);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();

        boolean first = true;
        sb.append("(");
        for (AstExp l : es){
            if(!first){
                sb.append(", ");
            }
            first = false;
            sb.append(l);
        }
        sb.append(")");

        return sb.toString();
    }
}