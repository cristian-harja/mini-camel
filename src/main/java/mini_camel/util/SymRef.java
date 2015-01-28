package mini_camel.util;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import mini_camel.ast.AstExp;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import java.util.List;

/**
 * Denotes a symbol reference.
 */
@Immutable
@ParametersAreNonnullByDefault
public final class SymRef extends AstExp {
    /**
     * Name of the identifier being referenced.
     */
    public final String id;

    private static final Function<SymRef, String>
            GET_ID = new Function<SymRef, String>() {
        @Override
        public String apply(@Nullable SymRef input) {
            if (input == null) return null;
            return input.id;
        }
    };

    public SymRef(String id) {
        this.id = id;
    }

    public SymRef(Id id) {
        this(id.id);
        setSymbol(id.getSymbol());
    }

    public static List<String> idList(List<SymRef> l) {
        return Lists.transform(l, GET_ID);
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
        return id;
    }

}
