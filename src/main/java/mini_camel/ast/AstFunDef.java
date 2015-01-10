package mini_camel.ast;

import mini_camel.Id;
import mini_camel.type.TFun;
import mini_camel.type.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class AstFunDef extends AstExp {
    public final Id id;
    public final Type functionType;
    public final Type returnType;
    public final List<Type> argTypes;
    public final List<Id> args;
    public final AstExp e;

    public AstFunDef(Id id, List<Id> args, AstExp e) {
        this.id = id;
        this.args = Collections.unmodifiableList(args);
        this.e = e;

        List<Type> argTypes;
        Type functionType = this.returnType = Type.gen();
        argTypes = new ArrayList<>(args.size());

        for (int i = 0, n = args.size(); i < n; ++i) {
            Type argType = Type.gen();
            argTypes.add(0, argType);
            functionType = new TFun(argType, functionType);
        }

        this.functionType = functionType;
        this.argTypes = Collections.unmodifiableList(argTypes);
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public <T, U> T accept(Visitor2<T, U> v, U a) {
        return v.visit(a, this);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("(");
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