package mini_camel.type;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * Represents the type of a function (which takes one argument and returns
 * one value). Functions with ore than one argument are curried.
 */
@Immutable
public final class TFun extends Type {
    public final Type arg;
    public final Type ret;

    public TFun(@Nonnull Type arg, @Nonnull Type ret) {
        this.arg = arg;
        this.ret = ret;
    }

    public TFun() {
        this(Type.gen(), Type.gen());
    }

    @Override
    public String toString() {
        return "(" + arg + " -> " + ret + ")";
    }
}
