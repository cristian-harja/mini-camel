package mini_camel.ast;

import mini_camel.util.SymDef;
import mini_camel.util.Visitor;
import mini_camel.util.Visitor1;
import mini_camel.util.Visitor2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * A "let rec" expression, allowing the definition of a function.
 */
@Immutable
public final class AstLetRec extends AstExp {
    /**
     * Function declared by this {@code let rec} expression.
     */
    public final AstFunDef fd;

    /**
     * Value returned by this {@code let rec}; it can (and it should) contain
     * references to the declared function.
     */
    public final AstExp ret;

    public AstLetRec(AstFunDef fd, AstExp ret) {
        this.fd = fd;
        this.ret = ret;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public <T> T accept(Visitor1<T> v) {
        return v.visit(this);
    }

    public <T, U> T accept(Visitor2<T, U> v, @Nullable U a) {
        return v.visit(a, this);
    }

    @Nonnull
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("(let rec ");
        sb.append(fd.decl.id);

        boolean first = true;
        sb.append("(");
        for (SymDef l : fd.args){
            if(!first){
                sb.append(", ");
            }
            first = false;
            sb.append(l.id);
        }
        sb.append(") = ");
        sb.append(fd.body);
        sb.append(" in ");
        sb.append(ret);
        sb.append(")");

        return sb.toString();
    }
}