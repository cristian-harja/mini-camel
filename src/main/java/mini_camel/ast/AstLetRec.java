package mini_camel.ast;

import mini_camel.ir.instr.Couple;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * A "let rec" expression, allowing the definition of a function.
 */
@Immutable
public final class AstLetRec extends AstExp {
    public final AstFunDef fd;
    public final AstExp e;

    public AstLetRec(
            @Nonnull AstFunDef fd,
            @Nonnull AstExp e
    ) {
        this.fd = fd;
        this.e = e;
    }

    public void accept(@Nonnull Visitor v) {
        v.visit(this);
    }

    @Override
    public Couple accept(@Nonnull Visitor3 v) {
        return v.visit(this);
    }

    public <T, U> T accept(@Nonnull Visitor2<T, U> v, U a) {
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