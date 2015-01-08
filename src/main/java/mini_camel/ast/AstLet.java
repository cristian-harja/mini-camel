package mini_camel.ast;

import mini_camel.Id;
import mini_camel.type.Type;

public final class AstLet extends AstExp {
    public final Id id;
    public final Type t;
    public final AstExp e1;
    public final AstExp e2;

    public AstLet(Id id, Type t, AstExp e1, AstExp e2) {
        this.id = id;
        this.t = t;
        this.e1 = e1;
        this.e2 = e2;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public String toString(){
        return "(let " + id.id + " = " + e1 + " in " + e2 + ")";
    }
}