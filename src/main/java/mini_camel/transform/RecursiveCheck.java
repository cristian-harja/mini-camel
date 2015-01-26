package mini_camel.transform;

import mini_camel.ast.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by lina on 1/20/15.
 */
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
        recDef.add(e.fd.id.id);

        e.fd.accept(this);
    }

    public void visit(@Nonnull AstVar e) {
        if (recDef.contains(e.toString())) {
            recFunction.add(e.toString());
        }
    }

}
