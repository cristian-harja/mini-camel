package mini_camel.ast;

import mini_camel.Id;
import mini_camel.type.Type;

import java.util.Collections;
import java.util.List;

public final class AstFunDef extends AstExp {
    public final Id id;
    public final Type type;
    public final List<Id> args;
    public final AstExp e;

    public AstFunDef(Id id, Type t, List<Id> args, AstExp e) {
        this.id = id;
        this.type = t;
        this.args = Collections.unmodifiableList(args);
        this.e = e;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("(let ");
        sb.append(id);

        boolean first = true;
        sb.append("(");
        for (Id l : args){
            if(!first){
                sb.append(", ");
            }
            first = false;
            sb.append(l.id);
        }
        sb.append(") = ");
        sb.append(e);
        sb.append(")");

        return sb.toString();
    }
}