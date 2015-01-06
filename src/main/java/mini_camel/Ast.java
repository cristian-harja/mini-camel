package mini_camel;

import java.util.*;

abstract class Exp {
    abstract void accept(Visitor v);
}

class Unit extends Exp {
    void accept(Visitor v) {
        v.visit(this);
    }
}

class Bool extends Exp {
    boolean b;

    Bool(boolean b) {
        this.b = b;
    }

    void accept(Visitor v) {
        v.visit(this);
    }

}

class Int extends Exp {
    int i;

    Int(int i) {
        this.i = i;
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class FloatExp extends Exp {
    float f;

    FloatExp(float f) {
        this.f = f;
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class Not extends Exp {
    Exp e;

    Not(Exp e) {
        this.e = e;
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class Neg extends Exp {
    Exp e;

    Neg(Exp e) {
        this.e = e;
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class Add extends Exp {
    Exp e1;
    Exp e2;

    Add(Exp e1, Exp e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class Sub extends Exp {
    Exp e1;
    Exp e2;

    Sub(Exp e1, Exp e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class FNeg extends Exp {
    Exp e;

    FNeg(Exp e) {
        this.e = e;
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class FAdd extends Exp {
    Exp e1;
    Exp e2;

    FAdd(Exp e1, Exp e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class FSub extends Exp {
    Exp e1;
    Exp e2;

    FSub(Exp e1, Exp e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class FMul extends Exp {
    Exp e1;
    Exp e2;

    FMul(Exp e1, Exp e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class FDiv extends Exp {
    Exp e1;
    Exp e2;

    FDiv(Exp e1, Exp e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class Eq extends Exp {
    Exp e1;
    Exp e2;

    Eq(Exp e1, Exp e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class LE extends Exp {
    Exp e1;
    Exp e2;

    LE(Exp e1, Exp e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class If extends Exp {
    Exp e1;
    Exp e2;
    Exp e3;

    If(Exp e1, Exp e2, Exp e3) {
        this.e1 = e1;
        this.e2 = e2;
        this.e3 = e3;
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class Let extends Exp {
    Id id;
    Type t;
    Exp e1;
    Exp e2;

    Let(Id id, Type t, Exp e1, Exp e2) {
        this.id = id;
        this.t = t;
        this.e1 = e1;
        this.e2 = e2;
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class Var extends Exp {
    Id id;

    Var(Id id) {
        this.id = id;
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class LetRec extends Exp {
    FunDef fd;
    Exp e;

    LetRec(FunDef fd, Exp e) {
        this.fd = fd;
        this.e = e;
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class App extends Exp {
    Exp e;
    List<Exp> es;

    App(Exp e, List<Exp> es) {
        this.e = e;
        this.es = es;
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class Tuple extends Exp {
    List<Exp> es;

    Tuple(List<Exp> es) {
        this.es = es;
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class LetTuple extends Exp {
    List<Id> ids;
    List<Type> ts;
    Exp e1;
    Exp e2;

    LetTuple(List<Id> ids, List<Type> ts, Exp e1, Exp e2) {
        this.ids = ids;
        this.ts = ts;
        this.e1 = e1;
        this.e2 = e2;
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class Array extends Exp {
    Exp e1;
    Exp e2;

    Array(Exp e1, Exp e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class Get extends Exp {
    Exp e1;
    Exp e2;

    Get(Exp e1, Exp e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class Put extends Exp {
    Exp e1;
    Exp e2;
    Exp e3;

    Put(Exp e1, Exp e2, Exp e3) {
        this.e1 = e1;
        this.e2 = e2;
        this.e3 = e3;
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class FunDef {
    Id id;
    Type type;
    List<Id> args;
    Exp e;

    FunDef(Id id, Type t, List<Id> args, Exp e) {
        this.id = id;
        this.type = type;
        this.args = args;
        this.e = e;
    }

}

