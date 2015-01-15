package mini_camel.ir;

import java.util.List;

/**
 * Created by lina on 1/14/15.
 */
public class Call extends Instr {
    private String name;
    private List<Op> args;

    public Call(String s, List<Op> l) {
        name = s;
        args = l;
    }

    public Call(String s) {
        name = s;
    }

    public Call() {
    }

    @Override
    public Type getType() {
        return Type.CALL;
    }

    @Override
    public String toString() {
        return "call " + name + args;
    }

    public String getName() {
        return name;
    }


    public List<Op> getArgs() {
        return args;
    }
}
