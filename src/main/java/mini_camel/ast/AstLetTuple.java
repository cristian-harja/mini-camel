package mini_camel.ast;

import mini_camel.Id;
import mini_camel.type.Type;

import java.util.Collections;
import java.util.List;

public final class AstLetTuple extends AstExp {
    public final List<Id> ids;
    public final List<Type> ts;
    public final AstExp e1, e2;

    public AstLetTuple(List<Id> ids, List<Type> ts, AstExp e1, AstExp e2) {
        this.ids = Collections.unmodifiableList(ids);
        this.ts = Collections.unmodifiableList(ts);
        this.e1 = e1;
        this.e2 = e2;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}