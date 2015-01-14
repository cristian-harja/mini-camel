package mini_camel.ast;

import mini_camel.ir.Couple;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Collections;
import java.util.List;

/**
 * Function application ({@code e(es0, es1, ..., esN)}).
 */
@Immutable
public final class AstApp extends AstExp {
    public final AstExp e;
    public final List<AstExp> es;

    public AstApp(
            @Nonnull AstExp e,
            @Nonnull List<AstExp> es
    ) {
        this.e = e;
        this.es = Collections.unmodifiableList(es);
    }

    public void accept(@Nonnull Visitor v) {
        v.visit(this);
    }

    @Override
    public Couple accept(@Nonnull Visitor3 v) {
        return v.visit(this);
    }

    public <T, U> T accept(@Nonnull Visitor2<T, U> v, U a) {
        return v.visit(a, this);
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
