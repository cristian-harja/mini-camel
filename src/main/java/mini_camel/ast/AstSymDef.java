package mini_camel.ast;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import mini_camel.type.Type;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.List;

/**
 * Denotes a symbol declaration; groups together the name and type of the
 * declared identifier.
 */
@Immutable
public final class AstSymDef extends AstNode {
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

    public AstSymDef(String id, Type type) {
        this.id = id;
        this.type = type;
    }

    public AstSymDef(Id id, Type type) {
        this(id.id, type);
        setSymbol(id.getSymbol());
    }

    public AstSymDef(String id) {
        this(id, Type.gen());
    }

    public AstSymDef(Id id) {
        this(id.id, Type.gen());
        setSymbol(id.getSymbol());
    }

    @Nonnull
    public AstSymDef rename(String newName) {
        if (newName.equals(id)) return this;
        AstSymDef sym = new AstSymDef(newName, type);
        sym.setSymbol(getSymbol());
        return sym;
    }

    @Nonnull
    public static List<String> ids(List<AstSymDef> list) {
        return Lists.transform(list, new Function<AstSymDef, String>() {
            @Override
            public String apply(@Nullable AstSymDef from) {
                return from == null ? null : from.id;
            }
        });
    }

    @Nonnull
    public static List<Type> types(List<AstSymDef> list) {
        return Lists.transform(list, new Function<AstSymDef, Type>() {
            @Override
            public Type apply(@Nullable AstSymDef from) {
                return from == null ? null : from.type;
            }
        });
    }

    @Nonnull
    public String toString() {
        return id;
    }
}
