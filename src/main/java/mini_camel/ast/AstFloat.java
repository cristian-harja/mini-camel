package mini_camel.ast;

public final class AstFloat extends AstExp {
    public final float f;

    public AstFloat(float f) {
        this.f = f;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public <T, U> T accept(Visitor2<T, U> v, U a) {
        return v.visit(a, this);
    }

    public String toString(){
        return Float.toString(f);
    }
}
