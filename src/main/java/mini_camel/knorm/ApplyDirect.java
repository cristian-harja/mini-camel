package mini_camel.knorm;

import mini_camel.type.Type;
import mini_camel.util.KVisitor;
import mini_camel.util.KVisitor1;
import mini_camel.util.KVisitor2;
import mini_camel.util.SymRef;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.Collections;
import java.util.List;

/**
 * Calls a global (known) function by name (not by reference).
 */
@Immutable
public final class ApplyDirect extends KNode {
    @Nonnull
    public final SymRef functionName;

    @Nonnull
    public final List<SymRef> args;

    public ApplyDirect(SymRef functionName, List<SymRef> args) {
        this.functionName = functionName;
        this.args = Collections.unmodifiableList(args);
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
        return null; // fixme after closure conversion
    }

    @Nonnull
    public String toString() {
        return String.format(
                "ApplyDirect(%s, %s)",
                functionName.id,
                args.toString()
        );
    }
}
