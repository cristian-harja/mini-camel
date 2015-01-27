package mini_camel.ast;

import mini_camel.type.TUnit;
import mini_camel.visit.*;import mini_camel.type.Type;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * Let-expression ({@code let id = e1 in e2}).
 */
@Immutable
public final class AstLet extends AstExp {
    /**
     * Information about the declared identifier (name and type).
     */
    @Nonnull
    public final AstSymDef decl;

    /**
     * The expression used to initialize the declared symbol. Its type must
     * be compatible to the type contained in {@code decl.type}.
     */
    @Nonnull
    public final AstExp initializer;

    /**
     * The expression returned by this {@code let}; it can (and it should)
     * contain references to the symbol declared in {@link #decl}.
     */
    @Nonnull
    public final AstExp ret;

    public AstLet(AstSymDef decl, AstExp initializer, AstExp ret) {
        this.decl = decl;
        this.initializer = initializer;
        this.ret = ret;
    }

    public AstLet(Id id, AstExp initializer, AstExp ret) {
        this(id, Type.gen(), initializer, ret);
    }

    private AstLet(Id id, Type t, AstExp initializer, AstExp ret) {
        this.decl = new AstSymDef(id, t);
        this.initializer = initializer;
        this.ret = ret;
    }

    public static AstLet semicolonSyntacticSugar(AstExp s1, AstExp s2) {
        return new AstLet(Id.gen(), TUnit.INSTANCE, s1, s2);
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
        return "(let " + decl.id + " = " + initializer + " in " + ret + ")";
    }
}