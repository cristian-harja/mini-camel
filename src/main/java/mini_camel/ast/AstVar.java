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

    public <T, U> T accept(Visitor2<T, U> v, U a) {
        return v.visit(a, this);
    }

    public String toString(){
        return id.toString();
    }
}