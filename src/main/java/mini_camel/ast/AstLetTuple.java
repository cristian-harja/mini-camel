package mini_camel.ast;

import mini_camel.ir.Couple;
import mini_camel.type.Type;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Collections;
import java.util.List;

/**
 * Let expression ({@code let (id1, id2, ...idN) = e1 in e2}).
 */
@Immutable
public final class AstLetTuple extends AstExp {
    public final List<Id> ids;
    public final List<Type> ts;
    public final AstExp e1, e2;

    public AstLetTuple(
            @Nonnull List<Id> ids,
            @Nonnull List<Type> ts,
            @Nonnull AstExp e1,
            @Nonnull AstExp e2
    ) {
        this.ids = Collections.unmodifiableList(ids);
        this.ts = Collections.unmodifiableList(ts);
        this.e1 = e1;
        this.e2 = e2;
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

        sb.append("(let ");

        boolean first = true;
        sb.append("(");
        for (Id l : ids){
            if(!first){
                sb.append(", ");
            }
            first = false;
            sb.append(l.id);
        }
        sb.append(") = ");
        sb.append(e1);
        sb.append(" in ");
        sb.append(e2);
        sb.append(")");

        return sb.toString();
    }
}