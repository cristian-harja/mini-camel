package mini_camel.ast;

import mini_camel.visit.*;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An ordered pair of expressions ({@code (es1, es2, ..., esN)}.
 */
@Immutable
public final class AstTuple extends AstExp {
    /**
     * The elements of the tuple being created.
     */
    public final List<AstExp> es;

    public AstTuple(List<AstExp> es) {
        this.es = Collections.unmodifiableList(es);
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public <T> T accept(Visitor1<T> v) {
        return v.visit(this);
    }

    public <T, U> T accept(Visitor2<T, U> v, @Nullable U a) {
        return v.visit(a, this);
    }

    @Nonnull
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