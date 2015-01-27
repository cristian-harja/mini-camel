package mini_camel.ast;

import mini_camel.visit.*;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * Conditional {@code if-then-else} ({@code if (e1) then e2 else e3}).
 */
@Immutable
public final class AstIf extends AstExp {
    /**
     * Expression representing the {@code if}'s condition. Its data type is
     * expected to be {@link mini_camel.type.TBool}.
     */
    @Nonnull
    public final AstExp eCond;

    /**
     * The {@code then} branch. Its data type is expected to be identical to
     * that of {@link #eElse}.
     */
    @Nonnull
    public final AstExp eThen;

    /**
     * The {@code else} branch. Its data type is expected to be identical to
     * that of {@link #eThen}.
     */
    @Nonnull
    public final AstExp eElse;

    public AstIf(AstExp eCond, AstExp eThen, AstExp eElse) {
        this.eCond = eCond;
        this.eThen = eThen;
        this.eElse = eElse;
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

        sb.append("(if(");
        sb.append(eCond);
        sb.append(") then ");
        sb.append(eThen);
        sb.append(" else ");
        sb.append(eElse);
        sb.append(")");

        return sb.toString();
    }
}
