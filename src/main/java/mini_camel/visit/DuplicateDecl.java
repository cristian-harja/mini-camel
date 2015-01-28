package mini_camel.visit;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import mini_camel.ast.AstExp;
import mini_camel.ast.AstFunDef;
import mini_camel.ast.AstLetTuple;
import mini_camel.util.SymDef;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
public final class DuplicateDecl extends DummyVisitor {

    private List<Collection<SymDef>> findings = new LinkedList<>();

    private Multimap<String, SymDef> mm = HashMultimap.create();

    private DuplicateDecl() {
    }

    public static List<Collection<SymDef>> compute(AstExp root) {
        DuplicateDecl dd = new DuplicateDecl();
        root.accept(dd);
        return dd.findings;
    }

    private void checkDefs(List<SymDef> defs) {
        for (SymDef def : defs) {
            mm.put(def.id, def);
        }
        for (String entry : mm.keySet()) {
            Collection<SymDef> occurrences = mm.get(entry);
            if (occurrences.size() > 1) {
                findings.add(new ArrayList<>(occurrences));
            }
        }
        mm.clear();
    }

    public void visit(AstLetTuple e) {
        checkDefs(e.ids);
    }

    public void visit(AstFunDef e) {
        checkDefs(e.args);
    }

}
