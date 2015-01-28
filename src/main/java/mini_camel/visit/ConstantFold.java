package mini_camel.visit;

import mini_camel.ast.*;
import mini_camel.util.SymDef;
import mini_camel.util.SymRef;
import mini_camel.util.SymTable;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public final class ConstantFold extends TransformHelper {

    private SymTable<AstExp> reMapping = new SymTable<>();

    private ConstantFold() {
    }

    public static AstExp compute(AstExp astNode) {
        return astNode.accept(new ConstantFold());
    }


    //Example : if x = 5 is in the symbol table, it will return an AstInt of value 5
    // otherwise it does nothing
    @Nonnull
    public AstExp visit(SymRef e) {
        AstExp new_e = reMapping.get(e.id);
        return (new_e == null) ? e : new_e;
    }


    @Nonnull
    public AstExp visit(AstLet e) {
        SymDef old_id = e.decl;
        AstExp new_e1 = e.initializer.accept(this);
        AstExp new_e2;


        reMapping.push();
        {
            // Puts the mapping e.id -> e.e1 (its value) in the stack and transforms the expression e.e2
            if (new_e1 instanceof AstUnit || new_e1 instanceof SymRef || new_e1 instanceof AstInt || new_e1 instanceof AstBool || new_e1 instanceof AstFloat || new_e1 instanceof AstArray || new_e1 instanceof AstTuple) {
                //if(new_e1 instanceof AstVar){
                reMapping.put(old_id.id, new_e1);
            }

            new_e2 = e.ret.accept(this);
        }
        reMapping.pop();

        return new AstLet(old_id, new_e1, new_e2);
    }


    @Nonnull
    public AstExp visit(AstAdd e) {
        AstExp new_e1 = e.e1.accept(this);
        AstExp new_e2 = e.e2.accept(this);

        if (new_e1 instanceof AstInt && new_e2 instanceof AstInt) {
            return new AstInt(
                    ((AstInt) new_e1).i + ((AstInt) new_e2).i
            );
        }

        return new AstAdd(new_e1, new_e2);
    }

    @Nonnull
    public AstExp visit(AstSub e) {
        AstExp new_e1 = e.e1.accept(this);
        AstExp new_e2 = e.e2.accept(this);


        if (new_e1 instanceof AstInt && new_e2 instanceof AstInt) {
            int val = ((AstInt) new_e1).i - ((AstInt) new_e2).i;
            return new AstInt(val);
        }

        return new AstSub(new_e1, new_e2);
    }

    @Nonnull
    public AstExp visit(AstNeg e) {
        AstExp new_e = e.e.accept(this);

        if (new_e instanceof AstInt) {
            return new AstInt(-((AstInt) new_e).i);
        }

        return new AstNeg(new_e);
    }

    @Nonnull
    public AstExp visit(AstFNeg e) {
        AstExp new_e = e.e.accept(this);

        if (new_e instanceof AstFloat) {
            return new AstFloat(-((AstFloat) new_e).f);
        }

        return new AstNeg(new_e);
    }

    @Nonnull
    public AstExp visit(AstFAdd e) {
        AstExp new_e1 = e.e1.accept(this);
        AstExp new_e2 = e.e2.accept(this);

        if (new_e1 instanceof AstFloat && new_e2 instanceof AstFloat) {
            return new AstFloat(
                    ((AstFloat) new_e1).f + ((AstFloat) new_e2).f
            );
        }

        return new AstFAdd(new_e1, new_e2);
    }

    @Nonnull
    public AstExp visit(AstFSub e) {
        AstExp new_e1 = e.e1.accept(this);
        AstExp new_e2 = e.e2.accept(this);

        if (new_e1 instanceof AstFloat && new_e2 instanceof AstFloat) {
            return new AstFloat(
                    ((AstFloat) new_e1).f - ((AstFloat) new_e2).f
            );
        }

        return new AstFAdd(new_e1, new_e2);
    }

    @Nonnull
    public AstExp visit(AstFMul e) {
        AstExp new_e1 = e.e1.accept(this);
        AstExp new_e2 = e.e2.accept(this);

        if (new_e1 instanceof AstFloat && new_e2 instanceof AstFloat) {
            return new AstFloat(
                    ((AstFloat) new_e1).f * ((AstFloat) new_e2).f
            );
        }

        return new AstFAdd(new_e1, new_e2);
    }

    @Nonnull
    public AstExp visit(AstFDiv e) {
        AstExp new_e1 = e.e1.accept(this);
        AstExp new_e2 = e.e2.accept(this);

        if (new_e1 instanceof AstFloat && new_e2 instanceof AstFloat) {
            return new AstFloat(
                    ((AstFloat) new_e1).f / ((AstFloat) new_e2).f
            );
        }

        return new AstFAdd(new_e1, new_e2);
    }

    @Nonnull
    public AstExp visit(AstNot e) {
        AstExp new_e = e.e.accept(this);

        if (new_e instanceof AstBool) {
            return new AstBool(!((AstBool) new_e).b);
        }
        return new AstNot(new_e);
    }

    @Nonnull
    public AstExp visit(AstIf e) {
        AstExp new_e1 = e.eCond.accept(this);
        if (new_e1 instanceof AstBool) {
            if (((AstBool) new_e1).b) {
                return e.eThen.accept(this);
            } else {
                return e.eElse.accept(this);
            }
        }

        AstExp new_e2 = e.eThen.accept(this);
        AstExp new_e3 = e.eElse.accept(this);
        return new AstIf(new_e1, new_e2, new_e3);

    }

    @Nonnull
    public AstExp visit(AstEq e) {
        boolean b;
        AstExp new_e1 = e.e1.accept(this);
        AstExp new_e2 = e.e2.accept(this);
        if (new_e1 instanceof AstInt && new_e2 instanceof AstInt) {
            b = ((AstInt) new_e1).i == ((AstInt) new_e2).i;
        } else if (new_e1 instanceof AstBool && new_e2 instanceof AstBool) {
            b = ((AstBool) new_e1).b == ((AstBool) new_e2).b;
        } else if (new_e1 instanceof AstFloat && new_e2 instanceof AstFloat) {
            b = ((AstFloat) new_e1).f == ((AstFloat) new_e2).f;
        } else {
            return new AstEq(new_e1, new_e2);
        }

        return AstBool.staticInstance(b);

    }

    @Nonnull
    public AstExp visit(AstLE e) {
        boolean b;
        AstExp new_e1 = e.e1.accept(this);
        AstExp new_e2 = e.e2.accept(this);

        if (new_e1 instanceof AstInt && new_e2 instanceof AstInt) {
            b = ((AstInt) new_e1).i <= ((AstInt) new_e2).i;
        } else if (new_e1 instanceof AstFloat && new_e2 instanceof AstFloat) {
            b = ((AstFloat) new_e1).f <= ((AstFloat) new_e2).f;
        } else {
            return new AstLE(new_e1, new_e2);
        }

        return AstBool.staticInstance(b);

    }

}
