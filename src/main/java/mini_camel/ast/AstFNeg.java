package mini_camel.ast;

public final class AstFNeg extends AstExp {
    public final AstExp e;

    public AstFNeg(AstExp e) {
        this.e = e;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}