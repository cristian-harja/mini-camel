package mini_camel.ast;

public final class AstArray extends AstExp {
    public final AstExp e1, e2;

    public AstArray(AstExp e1, AstExp e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}