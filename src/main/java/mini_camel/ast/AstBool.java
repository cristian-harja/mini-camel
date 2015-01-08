package mini_camel.ast;

public final class AstBool extends AstExp {
    public final boolean b;

    public AstBool(boolean b) {
        this.b = b;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public <T, U> T accept(Visitor2<T, U> v, U a) {
        return v.visit(a, this);
    }

    public String toString(){
        return b ? "True" : "False";
    }

}
