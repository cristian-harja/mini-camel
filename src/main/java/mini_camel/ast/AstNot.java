package mini_camel.ast;

public final class AstNot extends AstExp {
    public final AstExp e;

    public AstNot(AstExp e) {
        this.e = e;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public <T, U> T accept(Visitor2<T, U> v, U a) {
        return v.visit(a, this);
    }

    public String toString(){
        return "(Not " + e.toString() + ")";
    }
}