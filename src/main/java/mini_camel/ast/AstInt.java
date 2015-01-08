package mini_camel.ast;

public final class AstInt extends AstExp {
    public final int i;

    public AstInt(int i) {
        this.i = i;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public <T, U> T accept(Visitor2<T, U> v, U a) {
        return v.visit(a, this);
    }

    public String toString(){
        return Integer.toString(i);
    }
}