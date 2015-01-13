package mini_camel.comp;

import mini_camel.ast.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by mommess on 08/01/15.
 */
public class AssemblyVisitor implements Visitor {
    StringBuilder data;
    StringBuilder text;static final Set<String> globals;

    static {
        globals = new HashSet<>();
        globals.add("print_newline");
        //globals.add("hello_world");
    }

    public AssemblyVisitor(StringBuilder text, StringBuilder data){
        this.text = text;
        this.data = data;
    }

    @Override
    public void visit(AstUnit e) {

    }

    @Override
    public void visit(AstBool e) {

    }

    @Override
    public void visit(AstInt e) {

    }

    @Override
    public void visit(AstFloat e) {

    }

    @Override
    public void visit(AstNot e) {

    }

    @Override
    public void visit(AstNeg e) {

    }

    @Override
    public void visit(AstAdd e) {

    }

    @Override
    public void visit(AstSub e) {

    }

    @Override
    public void visit(AstFNeg e) {

    }

    @Override
    public void visit(AstFAdd e) {

    }

    @Override
    public void visit(AstFSub e) {

    }

    @Override
    public void visit(AstFMul e) {

    }

    @Override
    public void visit(AstFDiv e) {

    }

    @Override
    public void visit(AstEq e) {

    }

    @Override
    public void visit(AstLE e) {

    }

    @Override
    public void visit(AstIf e) {

    }

    @Override
    public void visit(AstLet e) {

    }


    @Override
    public void visit(AstVar e) {

        if (globals.contains(e.id.id)) {
            data.append("\tbl min_caml_");
            data.append(e.id.id);
            data.append("\n");
        }

        /*if(e.id.id.equals("print_int")){

        }

        if(e.id.id.equals("print_char")){

        }

        if(e.id.id.equals("print_string")){

        }*/

    }

    @Override
    public void visit(AstLetRec e) {

    }

    @Override
    public void visit(AstApp e) {

    }

    @Override
    public void visit(AstTuple e) {

    }

    @Override
    public void visit(AstLetTuple e) {

    }

    @Override
    public void visit(AstArray e) {

    }

    @Override
    public void visit(AstGet e) {

    }

    @Override
    public void visit(AstPut e) {

    }

    @Override
    public void visit(AstFunDef e) {

    }
}
