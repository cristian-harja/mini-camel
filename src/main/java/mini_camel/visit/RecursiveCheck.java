package mini_camel.visit;

import mini_camel.ast.*;
import mini_camel.util.SymRef;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public final class RecursiveCheck extends DummyVisitor {
    private List<String> recDef = new ArrayList<>();
    private List<String> recFunction = new ArrayList<>();

    private RecursiveCheck() {
    }

    public static List<String> compute(AstExp root) {
        RecursiveCheck rc = new RecursiveCheck();
        root.accept(rc);
        return rc.recFunction;
    }

    public void visit(@Nonnull AstLetRec e) {
        recDef.add(e.fd.decl.id);
        e.fd.accept(this);
    }

    public void visit(@Nonnull SymRef e) {
        if (recDef.contains(e.toString())) {
            recFunction.add(e.toString());
        }
    }

}
