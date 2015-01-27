package mini_camel.knorm;

import mini_camel.util.KVisitor;
import mini_camel.util.KVisitor1;
import mini_camel.util.KVisitor2;
import mini_camel.util.SymRef;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.List;

/**
 * Wraps a function together with its free variables into a function object,
 * which is callable by a {@link ApplyClosure}.
 */
@Immutable
public final class ClosureMake extends KNode {
    @Nonnull
    public final SymRef functionName;

    @Nonnull
    public final List<SymRef> freeArguments;

    public ClosureMake(SymRef functionName, List<SymRef> freeArguments) {
        this.functionName = functionName;
        this.freeArguments = freeArguments;
    }

    public void accept(KVisitor v) {
        v.visit(this);
    }

    public <T> T accept(KVisitor1<T> v) {
        return v.visit(this);
    }

    public <T, U> T accept(KVisitor2<T, U> v, @Nullable U a) {
        return v.visit(a, this);
    }

    @Nonnull
    public String toString() {
        return String.format(
                "MakeClosure(%s, %s)",
                functionName.id,
                freeArguments.toString()
        );
    }
}
