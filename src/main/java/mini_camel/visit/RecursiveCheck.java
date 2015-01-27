package mini_camel.visit;

import mini_camel.ast.*;
import mini_camel.util.SymRef;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class RecursiveCheck extends DummyVisitor {

    private List<String> recDef = new ArrayList<>();
    private List<String> recFunction = new ArrayList<>();

    public RecursiveCheck() {
    }

    public List<String> applyTransform(AstExp astNode) {
        astNode.accept(this);

        return recFunction;
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
