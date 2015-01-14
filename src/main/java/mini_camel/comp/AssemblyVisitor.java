package mini_camel.comp;

import mini_camel.ast.*;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

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
    public void visit(@Nonnull AstUnit e) {

    }

    @Override
    public void visit(@Nonnull AstBool e) {

    }

    @Override
    public void visit(@Nonnull AstInt e) {

    }

    @Override
    public void visit(@Nonnull AstFloat e) {

    }

    @Override
    public void visit(@Nonnull AstNot e) {

    }

    @Override
    public void visit(@Nonnull AstNeg e) {

    }

    @Override
    public void visit(@Nonnull AstAdd e) {

    }

    @Override
    public void visit(@Nonnull AstSub e) {

    }

    @Override
    public void visit(@Nonnull AstFNeg e) {

    }

    @Override
    public void visit(@Nonnull AstFAdd e) {

    }

    @Override
    public void visit(@Nonnull AstFSub e) {

    }

    @Override
    public void visit(@Nonnull AstFMul e) {

    }

    @Override
    public void visit(@Nonnull AstFDiv e) {

    }

    @Override
    public void visit(@Nonnull AstEq e) {

    }

    @Override
    public void visit(@Nonnull AstLE e) {

    }

    @Override
    public void visit(@Nonnull AstIf e) {

    }

    @Override
    public void visit(@Nonnull AstLet e) {

    }


    @Override
    public void visit(@Nonnull AstVar e) {

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
    public void visit(@Nonnull AstLetRec e) {

    }

    @Override
    public void visit(@Nonnull AstApp e) {

    }

    @Override
    public void visit(@Nonnull AstTuple e) {

    }

    @Override
    public void visit(@Nonnull AstLetTuple e) {

    }

    @Override
    public void visit(@Nonnull AstArray e) {

    }

    @Override
    public void visit(@Nonnull AstGet e) {

    }

    @Override
    public void visit(@Nonnull AstPut e) {

    }

    @Override
    public void visit(@Nonnull AstFunDef e) {

    }
}
