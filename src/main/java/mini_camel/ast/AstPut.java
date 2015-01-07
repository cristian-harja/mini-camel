package mini_camel.ast;

public final class AstPut extends AstExp {
    public final AstExp e1, e2, e3;

    public AstPut(AstExp e1, AstExp e2, AstExp e3) {
        this.e1 = e1;
        this.e2 = e2;
        this.e3 = e3;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public String toString(){
        return "(" + e1.toString() + ".(" + e2.toString() + ") <- " + e3.toString() + ")";
    }
}