package mini_camel.ast;

import mini_camel.util.*;
import mini_camel.type.TFun;
import mini_camel.type.Type;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.Collections;
import java.util.List;

/**
 * Ast node corresponding to a function definition. Can only occur in a
 * "let rec" expression ({@code let rec func arg1 arg2 ... argN = e in ...}).
 */
@Immutable
public final class AstFunDef extends AstExp {
    /**
     * Information about the declared symbol (function name and type). Note
     * that this not the returned type; if arguments have type {@code T1} ..
     * {@code Tn} and the return type is {@code Tr} (the {@link #returnType}
     * field in this class), then the type of the function is
     * {@code T1 -> (T2 -> (T3 -> ... (Tn -> Tr)...)). This is because from
     * the point of view of the type system, functions are "Curried".
     */
    @Nonnull
    public final SymDef decl;

    /**
     * Information about the formal arguments of this function (name and type).
     */
    @Nonnull
    public final List<SymDef> args;

    /**
     * <p>The AST node representing the body of the function; it can contain
     * references to the declared symbol ({@link #decl}), in which case the
     * function would be recursive.
     * </p>
     * <p>The type of this expression is expected to be (after type checking)
     * equal to {@link #returnType}.
     * </p>
     */
    @Nonnull
    public final AstExp body;

    /**
     * Return type; note that the return type (as well as many others types)
     * is not necessarily known while parsing (and thus, it is not known while
     * building the AST), so this field will be an instance of {@link
     * mini_camel.type.TVar}. A {@link mini_camel.type.Checker type checker}
     * will later create a mapping from type variables to concrete types.
     */
    @Nonnull
    public final Type returnType;

    @CheckForNull
    private List<String> ids_;

    @CheckForNull
    private List<Type> types_;

    public AstFunDef(Id id, List<SymDef> args, AstExp body) {

        this.returnType = Type.gen();

        Type functionType = returnType;
        for (int i = args.size() - 1; i >= 0; --i) {
            SymDef arg = args.get(i);
            functionType = new TFun(arg.type, functionType);
        }

        this.args = Collections.unmodifiableList(args);
        this.decl = new SymDef(id, functionType);
        this.body = body;
    }

    public AstFunDef(SymDef decl, List<SymDef> args, AstExp body) {
        Type ret = decl.type; // function type
        for (SymDef arg : args) {
            TFun tFun = (ret instanceof TFun) ? ((TFun) ret) : null;
            if (tFun == null || arg.type != tFun.arg) {
                throw new IllegalArgumentException("Type mismatch");
            }
            ret = tFun.ret;
        }
        this.decl = decl;
        this.args = Collections.unmodifiableList(args);
        this.body = body;
        this.returnType = ret;
    }

    @Nonnull
    public List<String> getArgumentNames() {
        if (ids_ != null) return ids_;
        synchronized (this) {
            if (ids_ != null) return ids_;
            ids_ = SymDef.ids(args);
        }
        return ids_;
    }

    @Nonnull
    public List<Type> getArgumentTypes() {
        if (types_ != null) return types_;
        synchronized (this) {
            if (types_!= null) return types_;
            types_ = SymDef.types(args);
        }
        return types_;
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

        sb.append("(");
        sb.append(decl.id);

        boolean first = true;
        sb.append("(");
        for (SymDef l : args){
            if(!first){
                sb.append(", ");
            }
            first = false;
            sb.append(l.id);
        }
        sb.append(") = ");
        sb.append(body);
        sb.append(")");

        return sb.toString();
    }
}