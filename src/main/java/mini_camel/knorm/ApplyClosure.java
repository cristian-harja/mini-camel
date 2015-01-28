package mini_camel.knorm;

import mini_camel.type.Type;
import mini_camel.util.KVisitor;
import mini_camel.util.KVisitor1;
import mini_camel.util.KVisitor2;
import mini_camel.util.SymRef;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.List;

/**
 * Calls a function object (previously created by a {@link ClosureMake}).
 */
@Immutable
public final class ApplyClosure extends KNode {
    @Nonnull
    public final SymRef functionObj;

    @Nonnull
    public final List<SymRef> boundArguments;

    public ApplyClosure(SymRef functionObj, List<SymRef> boundArguments) {
        this.functionObj = functionObj;
        this.boundArguments = boundArguments;
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
                "ApplyClosure(%s, %s)",
                functionObj.id,
                boundArguments.toString()
        );
    }
}
