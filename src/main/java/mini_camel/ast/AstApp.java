package mini_camel.ast;

import mini_camel.visit.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Function application ({@code e(es0, es1, ..., esN)}).
 */
@Immutable
public final class AstApp extends AstExp {
    /**
     * The expression that is being interpreted as a function.
     */
    @Nonnull
    public final AstExp e;

    /**
     * The list of arguments to the function.
     */
    @Nonnull
    public final List<AstExp> es;

    public AstApp(AstExp e, List<AstExp> es) {
        this.e = e;
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
