package mini_camel.visit;

import mini_camel.ast.AstSymDef;
import mini_camel.util.SymTable;
import mini_camel.ast.*;

import javax.annotation.Nonnull;

public final class ConstantFold extends TransformHelper {

    private SymTable<AstExp> reMapping = new SymTable<>();

    private ConstantFold () {
    }

    public static AstExp compute(AstExp astNode) {
        return astNode.accept(new ConstantFold());
    }


    //Example : if x = 5 is in the symbol table, it will return an AstInt of value 5
    // otherwise it does nothing
    public AstExp visit(@Nonnull AstSymRef e) {
        AstExp new_e = reMapping.get(e.id);
        return (new_e == null) ? e : new_e;
    }


    public AstExp visit(@Nonnull AstLet e){
        AstSymDef old_id = e.decl;
        AstExp new_e1 = e.initializer.accept(this);
        AstExp new_e2;


        reMapping.push();
        {
            // Puts the mapping e.id -> e.e1 (its value) in the stack and transforms the expression e.e2
            if(new_e1 instanceof AstUnit || new_e1 instanceof AstSymRef || new_e1 instanceof AstInt || new_e1 instanceof AstBool || new_e1 instanceof AstFloat || new_e1 instanceof AstArray || new_e1 instanceof AstTuple){
             //if(new_e1 instanceof AstVar){
                reMapping.put(old_id.id, new_e1);
            }

            new_e2 = e.ret.accept(this);
        }
        reMapping.pop();

        return new AstLet(old_id, new_e1, new_e2);
    }



    public AstExp visit(@Nonnull AstAdd e){
        AstExp new_e1 = e.e1.accept(this);
        AstExp new_e2 = e.e2.accept(this);

        if(new_e1 instanceof AstInt && new_e2 instanceof  AstInt){
            int val = ((AstInt)new_e1).i + ((AstInt)new_e2).i;
            return new AstInt(val);
        }

        return new AstAdd(new_e1, new_e2);
    }


    public AstExp visit(@Nonnull AstSub e){
        AstExp new_e1 = e.e1.accept(this);
        AstExp new_e2 = e.e2.accept(this);


        if(new_e1 instanceof AstInt && new_e2 instanceof  AstInt){
            int val = ((AstInt)new_e1).i - ((AstInt)new_e2).i;
            return new AstInt(val);
        }

        return new AstSub(new_e1, new_e2);
    }


    public AstExp visit(@Nonnull AstNeg e){
        AstExp new_e = e.e.accept(this);

        if(new_e instanceof AstInt){
            int val = - ((AstInt)new_e).i;
            return new AstInt(val);
        }

        return new AstNeg(new_e);
    }


    public AstExp visit(@Nonnull AstFNeg e){
        AstExp new_e = e.e.accept(this);

        if(new_e instanceof AstFloat){
            float val = - ((AstFloat)new_e).f;
            return new AstFloat(val);
        }

        return new AstNeg(new_e);
    }


    public AstExp visit(@Nonnull AstFAdd e){
        AstExp new_e1 = e.e1.accept(this);
        AstExp new_e2 = e.e2.accept(this);

        if(new_e1 instanceof AstFloat && new_e2 instanceof  AstFloat){
            float val = ((AstFloat)new_e1).f + ((AstFloat)new_e2).f;
            return new AstFloat(val);
        }

        return new AstFAdd(new_e1, new_e2);
    }


    public AstExp visit(@Nonnull AstFSub e){
        AstExp new_e1 = e.e1.accept(this);
        AstExp new_e2 = e.e2.accept(this);

        if(new_e1 instanceof AstFloat && new_e2 instanceof  AstFloat){
            float val = ((AstFloat)new_e1).f - ((AstFloat)new_e2).f;
            return new AstFloat(val);
        }

        return new AstFAdd(new_e1, new_e2);
    }


    public AstExp visit(@Nonnull AstFMul e){
        AstExp new_e1 = e.e1.accept(this);
        AstExp new_e2 = e.e2.accept(this);

        if(new_e1 instanceof AstFloat && new_e2 instanceof  AstFloat){
            float val = ((AstFloat)new_e1).f * ((AstFloat)new_e2).f;
            return new AstFloat(val);
        }

        return new AstFAdd(new_e1, new_e2);
    }


    public AstExp visit(@Nonnull AstFDiv e){
        AstExp new_e1 = e.e1.accept(this);
        AstExp new_e2 = e.e2.accept(this);

        if(new_e1 instanceof AstFloat && new_e2 instanceof  AstFloat){
            float val = ((AstFloat)new_e1).f / ((AstFloat)new_e2).f;
            return new AstFloat(val);
        }

        return new AstFAdd(new_e1, new_e2);
    }

    public AstExp visit(@Nonnull AstNot e){
        AstExp new_e = e.e.accept(this);

        if(new_e instanceof AstBool){
            return new AstBool(!((AstBool)new_e).b);
        }
        return new AstNot(new_e);
    }

    public AstExp visit(@Nonnull AstIf e){
        AstExp new_e1 = e.eCond.accept(this);
        if(new_e1 instanceof AstBool){
            if(((AstBool)new_e1).b){
                return e.eThen.accept(this);
            }
            else {
                return e.eElse.accept(this);
            }
        }

        AstExp new_e2 = e.eThen.accept(this);
        AstExp new_e3 = e.eElse.accept(this);
        return new AstIf(new_e1, new_e2, new_e3);

    }

    public AstExp visit(@Nonnull AstEq e){
        AstExp new_e1 = e.e1.accept(this);
        AstExp new_e2 = e.e2.accept(this);

        if(new_e1 instanceof AstInt && new_e2 instanceof AstInt){
            if(((AstInt)new_e1).i == ((AstInt)new_e2).i){
                return new AstBool(true);
            }
            else {
                return new AstBool(false);
            }
        }
        else if (new_e1 instanceof AstBool && new_e2 instanceof AstBool){
            if(((AstBool)new_e1).b == ((AstBool)new_e2).b){
                return new AstBool(true);
            }
            else {
                return new AstBool(false);
            }
        }

        // TODO float


        return new AstEq(new_e1, new_e2);
    }

    public AstExp visit(@Nonnull AstLE e){
        AstExp new_e1 = e.e1.accept(this);
        AstExp new_e2 = e.e2.accept(this);

        if(new_e1 instanceof AstInt && new_e2 instanceof AstInt){
            if(((AstInt)new_e1).i <= ((AstInt)new_e2).i){
                return new AstBool(true);
            }
            else {
                return new AstBool(false);
            }
        }

        // TODO float


        return new AstLE(new_e1, new_e2);
    }

}
