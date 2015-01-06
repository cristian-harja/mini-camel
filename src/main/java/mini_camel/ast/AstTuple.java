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
}