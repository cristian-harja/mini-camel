package mini_camel.ast;

public final class AstLetRec extends AstExp {
    public final AstFunDef fd;
    public final AstExp e;

    public AstLetRec(AstFunDef fd, AstExp e) {
        this.fd = fd;
        this.e = e;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}