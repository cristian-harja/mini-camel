package mini_camel.ast;

public final class AstNeg extends AstExp {
    public final AstExp e;

    public AstNeg(AstExp e) {
        this.e = e;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}