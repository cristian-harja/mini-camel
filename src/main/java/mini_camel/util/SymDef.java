package mini_camel.util;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import mini_camel.ast.AstNode;
import mini_camel.type.Type;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import java.util.List;
import java.util.Map;

/**
 * Denotes a symbol declaration; groups together the name and type of the
 * declared identifier.
 */
@Immutable
@ParametersAreNonnullByDefault
public final class SymDef extends AstNode implements Map.Entry<String, Type> {
    /**
     * The name of the symbol.
     */
    @Nonnull
    public final String id;

    /**
     * Type variable representing the type of the declared symbol. This field
     * is most likely going to be initialized with an instance of the {@link
     * mini_camel.type.TVar} class.
     */
    @Nonnull
    public final Type type;

    public SymDef(String id, Type type) {
        this.id = id;
        this.type = type;
    }

    public SymDef(Id id, Type type) {
        this(id.id, type);
        setSymbol(id.getSymbol());
    }

    public SymDef(String id) {
        this(id, Type.gen());
    }

    public SymDef(Id id) {
        this(id.id, Type.gen());
        setSymbol(id.getSymbol());
    }

    @Nonnull
    public SymDef rename(String newName) {
        if (newName.equals(id)) return this;
        SymDef sym = new SymDef(newName, type);
        sym.setSymbol(getSymbol());
        return sym;
    }

    @Nonnull
    public SymRef makeRef() {
        return new SymRef(id);
    }

    @Nonnull
    public static List<String> ids(List<SymDef> list) {
        return Lists.transform(list, new Function<SymDef, String>() {
            @Override
            public String apply(@Nullable SymDef from) {
                return from == null ? null : from.id;
            }
        });
    }

    @Nonnull
    public static List<Type> types(List<SymDef> list) {
        return Lists.transform(list, new Function<SymDef, Type>() {
            @Override
            public Type apply(@Nullable SymDef from) {
                return from == null ? null : from.type;
            }
        });
    }

    @Nonnull
    public String toString() {
        return id;
    }

    @Nonnull
    public String getKey() {
        return id;
    }

    @Nonnull
    public Type getValue() {
        return type;
    }

    public Type setValue(Type value) {
        throw new UnsupportedOperationException();
    }
}
