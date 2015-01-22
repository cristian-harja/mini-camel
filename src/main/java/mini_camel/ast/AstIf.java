package mini_camel.ast;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * Conditional {@code if-then-else} ({@code if (e1) then e2 else e3}).
 */
@Immutable
public final class AstIf extends AstExp {
    public final AstExp e1, e2, e3;

    public AstIf(
            @Nonnull AstExp e1,
            @Nonnull AstExp e2,
            @Nonnull AstExp e3
    ) {
        this.e1 = e1;
        this.e2 = e2;
        this.e3 = e3;
    }

    public void accept(@Nonnull Visitor v) {
        v.visit(this);
    }

    public <T, U> T accept(@Nonnull Visitor2<T, U> v, U a) {
        return v.visit(a, this);
    }

    public <T, U> T accept(@Nonnull VisitorK<T, U> v, U a) {
        if (e1 instanceof AstEq) {
            AstEq eq = (AstEq) e1;
            return v.visitIfEq(a, this,
                    ((AstVar) eq.e1).id,
                    ((AstVar) eq.e2).id,
                    e2, e3
            );
        }
        if (e1 instanceof AstLE) {
            AstLE eq = (AstLE) e1;
            return v.visitIfLE(a, this,
                    ((AstVar) eq.e1).id,
                    ((AstVar) eq.e2).id,
                    e2, e3
            );
        }

        throw new RuntimeException("Unexpected " +
                e1.getClass().getCanonicalName() +
                " (instead of `AstEq` or `AstLE`) " +
                "as the condition of an `if` statement. "+
                "Expecting K-normalized AST."
        );
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
