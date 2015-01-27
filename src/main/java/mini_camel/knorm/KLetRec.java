package mini_camel.knorm;

import mini_camel.util.KVisitor;
import mini_camel.util.KVisitor1;
import mini_camel.util.KVisitor2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class KLetRec extends KNode {
    @Nonnull
    public final KFunDef fd;

    @Nonnull
    public final KNode ret;

    public KLetRec(KFunDef fd, KNode ret) {
        this.fd = fd;
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
    public String toString() {
        return String.format(
                "let rec %s %s = %s in %s",
                fd.name.id, fd.args,
                fd.body, ret.toString()
        );
    }

}
