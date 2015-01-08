package mini_camel.ast;

import mini_camel.Id;

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

    public <T, U> T accept(Visitor2<T, U> v, U a) {
        return v.visit(a, this);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("(let rec ");
        sb.append(fd.id);

        boolean first = true;
        sb.append("(");
        for (Id l : fd.args){
            if(!first){
                sb.append(", ");
            }
            first = false;
            sb.append(l.id);
        }
        sb.append(") = ");
        sb.append(fd.e);
        sb.append(" in ");
        sb.append(e);
        sb.append(")");

        return sb.toString();
    }
}