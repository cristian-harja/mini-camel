package mini_camel.ast;

public final class AstFSub extends AstExp {
    public final AstExp e1, e2;

    public AstFSub(AstExp e1, AstExp e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}