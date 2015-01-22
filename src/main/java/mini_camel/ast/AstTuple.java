package mini_camel.ast;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An ordered pair of expressions ({@code (es1, es2, ..., esN)}.
 */
@Immutable
public final class AstTuple extends AstExp {
    public final List<AstExp> es;

    public AstTuple(@Nonnull List<AstExp> es) {
        this.es = Collections.unmodifiableList(es);
    }

    public void accept(@Nonnull Visitor v) {
        v.visit(this);
    }

    public <T, U> T accept(@Nonnull Visitor2<T, U> v, U a) {
        return v.visit(a, this);
    }

    public <T, U> T accept(@Nonnull VisitorK<T, U> v, U a) {
        List<Id> ids = new ArrayList<>(es.size());
        for (AstExp e : es) {
            ids.add(((AstVar) e).id);
        }
        return v.visit(a, this, ids);
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