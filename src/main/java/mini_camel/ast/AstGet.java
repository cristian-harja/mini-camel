package mini_camel.ast;

public final class AstGet extends AstExp {
    public final AstExp e1, e2;

    public AstGet(AstExp e1, AstExp e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public <T, U> T accept(Visitor2<T, U> v, U a) {
        return v.visit(a, this);
    }

    public String toString(){
        return "(" + e1.toString() + ".(" + e2.toString() + "))";
    }
}