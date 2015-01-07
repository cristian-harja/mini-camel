package mini_camel.ast;

public final class AstIf extends AstExp {
    public final AstExp e1, e2, e3;

    public AstIf(AstExp e1, AstExp e2, AstExp e3) {
        this.e1 = e1;
        this.e2 = e2;
        this.e3 = e3;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("(if(");
        sb.append(e1);
        sb.append(") then ");
        sb.append(e2);
        sb.append(" else ");
        sb.append(e3);
        sb.append(")");

        return sb.toString();
    }
}
