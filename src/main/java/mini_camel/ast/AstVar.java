package mini_camel.ast;

import mini_camel.Id;

public final class AstVar extends AstExp {
    public final Id id;

    public AstVar(Id id) {
        this.id = id;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}