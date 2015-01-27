package mini_camel.ast;

import mini_camel.util.SymDef;
import mini_camel.util.Visitor;
import mini_camel.util.Visitor1;
import mini_camel.util.Visitor2;
import mini_camel.type.Type;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.Collections;
import java.util.List;

/**
 * Let expression with a tuple ({@code let (id1, id2, ...idN) = e1 in e2}).
 */
@Immutable
public final class AstLetTuple extends AstExp {
    /**
     * Contains the declared identifiers (name and type), which are bound in
     * the body of the {@code let} expression.
     */
    @Nonnull
    public final List<SymDef> ids;

    /**
     * Initializer of the {@code let} expression. Its type is expected to be
     * {@link mini_camel.type.TTuple}.
     */
    @Nonnull
    public final AstExp initializer;

    /**
     * The expression returned by this {@code let}.
     */
    @Nonnull
    public final AstExp ret;

    @CheckForNull
    private List<String> ids_;

    @CheckForNull
    private List<Type> types_;

    public AstLetTuple(List<SymDef> ids, AstExp initializer, AstExp ret) {
        this.ids = Collections.unmodifiableList(ids);
        this.initializer = initializer;
        this.ret = ret;
    }

    @Nonnull
    public List<String> getIdentifierList() {
        if (ids_ != null) return ids_;
        synchronized (this) {
            if (ids_ != null) return ids_;
            ids_ = SymDef.ids(ids);
        }
        return ids_;
    }

    @Nonnull
    public List<Type> getIdentifierTypes() {
        if (types_ != null) return types_;
        synchronized (this) {
            if (types_!= null) return types_;
            types_ = SymDef.types(ids);
        }
        return types_;
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

        sb.append("(let ");

        boolean first = true;
        sb.append("(");
        for (SymDef l : ids){
            if(!first){
                sb.append(", ");
            }
            first = false;
            sb.append(l.id);
        }
        sb.append(") = ");
        sb.append(initializer);
        sb.append(" in ");
        sb.append(ret);
        sb.append(")");

        return sb.toString();
    }
}