package mini_camel.visit;

import mini_camel.ast.AstExp;
import mini_camel.ast.AstLetRec;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class FunNumOp extends DummyVisitor {
    private int bound = 300;
    private List<String> funNumOp = new ArrayList<String>();

    public FunNumOp(){
    }

    /**
     * This is the method that initiates the actual transformation.
     *
     * @param astNode input AST
     * @return transformed AST
     */
    public List<String> applyTransform(@Nonnull AstExp astNode) {
        astNode.accept(this);


        return funNumOp;
    }

    @Override
    public void visit(@Nonnull AstLetRec e) {
        NumberOperation no = new NumberOperation();
        int tmp = no.applyTransform(e.fd.body);
        //Ici,il faut rajouter une limite
        if(tmp < bound)
        {funNumOp.add(e.fd.decl.id);}
        e.ret.accept(this);
    }

}
