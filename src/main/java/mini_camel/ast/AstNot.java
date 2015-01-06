package mini_camel.ast;

public final class AstNot extends AstExp {
    public final AstExp e;

    public AstNot(AstExp e) {
        this.e = e;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}