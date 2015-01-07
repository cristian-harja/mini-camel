package mini_camel.ast;

public final class AstUnit extends AstExp {
    public void accept(Visitor v) {
        v.visit(this);
    }

    public String toString(){
        return "()";
    }
}