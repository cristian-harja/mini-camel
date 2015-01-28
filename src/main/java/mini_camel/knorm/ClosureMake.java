package mini_camel.knorm;

import mini_camel.type.Type;
import mini_camel.util.*;

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
    public final SymDef target;

    @Nonnull
    public final SymRef functionName;

    @Nonnull
    public final List<SymRef> freeArguments;

    @Nonnull
    public final KNode ret;

    public ClosureMake(
            SymDef target,
            SymRef functionName,
            List<SymRef> freeArguments,
            KNode ret
    ) {
        this.target = target;
        this.functionName = functionName;
        this.freeArguments = freeArguments;
        this.ret = ret;
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
    public Type getDataType() {
        return ret.getDataType(); // fixme: verify correctness
    }

    @Nonnull
    public String toString() {
        return String.format(
                "let %s = MakeClosure(%s, %s) in (%s)",
                target.id,
                functionName.id,
                freeArguments.toString(),
                ret.toString()
        );
    }
}
