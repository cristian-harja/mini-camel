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
}
