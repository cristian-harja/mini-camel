package mini_camel.ast;

public final class AstFloat extends AstExp {
    public final float f;

    public AstFloat(float f) {
        this.f = f;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}
