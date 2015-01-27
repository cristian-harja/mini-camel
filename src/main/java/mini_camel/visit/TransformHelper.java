package mini_camel.visit;

import mini_camel.ast.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Useful for implementing program transformations on the AST, while
 * keeping the ASTs immutable. Meant to be used as a base class for those
 * program transformations that have ASTs as their input and output.
 * </p>
 * <p>A sub-class would override some methods (depending on the node types
 * that it wants to transform) and return either the same node, or a new one.
 * </p>
 * <p>Transforming the AST is done in two steps:</p>
 * <ul>
 *     <li>Top-down: recursively visit each node of the tree</li>
 *     <li>Bottom-up: returning from each recursive call, checking whether
 *     any of the children were modified. If not, return the old node;
 *     otherwise, return a new node of the same type, but with the new
 *     children as its fields.</li>
 * </ul>
 *
 * <p>The methods that are not overridden will default to an implementation
 * that applies the transformation recursively on each node (top-down) and
 * then after returning from the recursive calls (bottom-up) return new or
 * old nodes, depending on whether the children were modified.
 * </p>
 * <p>If only some of the children were modified, the old ones will be reused
 * as part of the new tree. Since everything is immutable, this is just as
 * good as re-creating the whole tree. It's similar to how the same process
 * would work in a functional language.
 * </p>
 */
public abstract class TransformHelper implements Visitor1<AstExp> {

    /*
        Arity = 0. No children to verify.

        If the sub-class doesn't override any of these methods, the old node
        is returned. If only some methods are overridden, whatever called
        them will detect the modification and will propagate it up the call
        tree (by returning new child nodes of the same type, but with some
        new values for their fields).
     */

    @Override
    public AstExp visit(@Nonnull AstErr e) {
        return e;
    }

    @Override
    public AstExp visit(@Nonnull AstUnit e) {
        return e;
    }

    @Override
    public AstExp visit(@Nonnull AstBool e) {
        return e;
    }

    @Override
    public AstExp visit(@Nonnull AstInt e) {
        return e;
    }

    @Override
    public AstExp visit(@Nonnull AstFloat e) {
        return e;
    }

    @Override
    public AstExp visit(@Nonnull AstSymRef e) {
        return e;
    }

    /*
        Arity = 1. One child to verify.

        Here's the basic workflow for all of the following functions:

        Step 1: take note of the old nodes (in the `old_*` variables)
        Step 2: apply the transformation and store the new nodes (`new_*`)
        Step 3: check if step 2 left all nodes untouched (`new_*` = `old_*`)
        Step 4: if no modification was detected, return the original node
        Step 5: if at least one new value was detected, return a new node
                of the same type, initialized with the new values
     */

    @Override
    public AstExp visit(@Nonnull AstNot e) {
        AstExp old_e = e.e; // step 1
        AstExp new_e = old_e.accept(this); // step 2
        if (new_e == old_e) { // step 3
            return e; // step 4
        }
        return new AstNot(new_e); // step 5
    }

    @Override
    public AstExp visit(@Nonnull AstNeg e) {
        AstExp old_e = e.e;
        AstExp new_e = old_e.accept(this);
        if (new_e == old_e) return e;
        return new AstNeg(new_e);
    }

    @Override
    public AstExp visit(@Nonnull AstFNeg e) {
        AstExp old_e = e.e;
        AstExp new_e = old_e.accept(this);
        if (new_e == old_e) return e;
        return new AstFNeg(new_e);
    }

    /*
        Arity = 2. Two children to test.
     */

    @Override
    public AstExp visit(@Nonnull AstAdd e) {
        AstExp old_e1 = e.e1; // step 1 ...
        AstExp old_e2 = e.e2;
        AstExp new_e1 = old_e1.accept(this); // step 2...
        AstExp new_e2 = old_e2.accept(this);
        if (new_e1 == old_e1 && new_e2 == old_e2) return e; // step 3, 4
        return new AstAdd(new_e1, new_e2); // step 5
    }

    @Override
    public AstExp visit(@Nonnull AstSub e) {
        AstExp old_e1 = e.e1;
        AstExp old_e2 = e.e2;
        AstExp new_e1 = old_e1.accept(this);
        AstExp new_e2 = old_e2.accept(this);
        if (new_e1 == old_e1 && new_e2 == old_e2) return e;
        return new AstSub(new_e1, new_e2);
    }

    @Override
    public AstExp visit(@Nonnull AstFAdd e) {
        AstExp old_e1 = e.e1;
        AstExp old_e2 = e.e2;
        AstExp new_e1 = old_e1.accept(this);
        AstExp new_e2 = old_e2.accept(this);
        if (new_e1 == old_e1 && new_e2 == old_e2) return e;
        return new AstFAdd(new_e1, new_e2);
    }

    @Override
    public AstExp visit(@Nonnull AstFSub e) {
        AstExp old_e1 = e.e1;
        AstExp old_e2 = e.e2;
        AstExp new_e1 = old_e1.accept(this);
        AstExp new_e2 = old_e2.accept(this);
        if (new_e1 == old_e1 && new_e2 == old_e2) return e;
        return new AstFSub(new_e1, new_e2);
    }

    @Override
    public AstExp visit(@Nonnull AstFMul e) {
        AstExp old_e1 = e.e1;
        AstExp old_e2 = e.e2;
        AstExp new_e1 = old_e1.accept(this);
        AstExp new_e2 = old_e2.accept(this);
        if (new_e1 == old_e1 && new_e2 == old_e2) return e;
        return new AstFMul(new_e1, new_e2);
    }

    @Override
    public AstExp visit(@Nonnull AstFDiv e) {
        AstExp old_e1 = e.e1;
        AstExp old_e2 = e.e2;
        AstExp new_e1 = old_e1.accept(this);
        AstExp new_e2 = old_e2.accept(this);
        if (new_e1 == old_e1 && new_e2 == old_e2) return e;
        return new AstFDiv(new_e1, new_e2);
    }

    @Override
    public AstExp visit(@Nonnull AstEq e) {
        AstExp old_e1 = e.e1;
        AstExp old_e2 = e.e2;
        AstExp new_e1 = old_e1.accept(this);
        AstExp new_e2 = old_e2.accept(this);
        if (new_e1 == old_e1 && new_e2 == old_e2) return e;
        return new AstEq(new_e1, new_e2);
    }

    @Override
    public AstExp visit(@Nonnull AstLE e) {
        AstExp old_e1 = e.e1;
        AstExp old_e2 = e.e2;
        AstExp new_e1 = old_e1.accept(this);
        AstExp new_e2 = old_e2.accept(this);
        if (new_e1 == old_e1 && new_e2 == old_e2) return e;
        return new AstLE(new_e1, new_e2);
    }

    @Override
    public AstExp visit(@Nonnull AstLet e) {
        AstExp old_e1 = e.initializer;
        AstExp old_e2 = e.ret;
        AstExp new_e1 = old_e1.accept(this);
        AstExp new_e2 = old_e2.accept(this);
        if (new_e1 == e.initializer && new_e2 == e.ret) return e;
        return new AstLet(e.decl, new_e1, new_e2);
    }

    @Override
    public AstExp visit(@Nonnull AstLetTuple e) {
        AstExp old_e1 = e.initializer;
        AstExp old_e2 = e.ret;
        AstExp new_e1 = old_e1.accept(this);
        AstExp new_e2 = old_e2.accept(this);
        if (new_e1 == e.initializer && new_e2 == e.ret) return e;
        return new AstLetTuple(e.ids, new_e1, new_e2);
    }

    @Override
    public AstExp visit(@Nonnull AstArray e) {
        AstExp old_e1 = e.size;
        AstExp old_e2 = e.initializer;
        AstExp new_e1 = old_e1.accept(this);
        AstExp new_e2 = old_e2.accept(this);
        if (new_e1 == e.size && new_e2 == e.initializer) return e;
        return new AstArray(new_e1, new_e2);
    }

    @Override
    public AstExp visit(@Nonnull AstGet e) {
        AstExp old_e1 = e.array;
        AstExp old_e2 = e.index;
        AstExp new_e1 = old_e1.accept(this);
        AstExp new_e2 = old_e2.accept(this);
        if (new_e1 == e.array && new_e2 == e.index) return e;
        return new AstGet(new_e1, new_e2);
    }

    /*
        Arity = 3. Three children to verify.
     */

    @Override
    public AstExp visit(@Nonnull AstIf e) {
        AstExp old_e1 = e.eCond;
        AstExp old_e2 = e.eThen;
        AstExp old_e3 = e.eElse;
        AstExp new_e1 = old_e1.accept(this);
        AstExp new_e2 = old_e2.accept(this);
        AstExp new_e3 = old_e3.accept(this);
        if (new_e1 == old_e1 && new_e2 == old_e2 && new_e3 == old_e3) return e;
        return new AstIf(new_e1, new_e2, new_e3);
    }

    @Override
    public AstExp visit(@Nonnull AstPut e) {
        AstExp old_e1 = e.array;
        AstExp old_e2 = e.index;
        AstExp old_e3 = e.value;
        AstExp new_e1 = old_e1.accept(this);
        AstExp new_e2 = old_e2.accept(this);
        AstExp new_e3 = old_e3.accept(this);
        if (new_e1 == e.array && new_e2 == e.index && new_e3 == e.value) return e;
        return new AstPut(new_e1, new_e2, new_e3);
    }

    /*
        Arity = n.

        Same old story, only this time we are applying the transformation
        to each item in a list and then comparing the new and old lists.
     */
    @Override
    public AstExp visit(@Nonnull AstApp e) {
        AstExp old_e = e.e;
        AstExp new_e = old_e.accept(this);
        List<AstExp> new_args = new ArrayList<>(e.es.size());
        for (AstExp old_arg : e.es) {
            new_args.add(old_arg.accept(this));
        }
        if (new_e.equals(e.e) && new_args.equals(e.es)) return e;
        return new AstApp(new_e, new_args);
    }

    @Override
    public AstExp visit(@Nonnull AstTuple e) {
        List<AstExp> new_items = new ArrayList<>(e.es.size());
        for (AstExp old_item : e.es) {
            new_items.add(old_item.accept(this));
        }
        if (new_items.equals(e.es)) return e;
        return new AstTuple(new_items);
    }

    /*
        The following vary slightly from the regular pattern we've been
        seeing so far.
    */

    @Override
    public AstExp visit(@Nonnull AstFunDef e) {
        AstExp old_e = e.body;
        AstExp new_e = old_e.accept(this);
        if (new_e == old_e) return e;
        return new AstFunDef(e.decl, e.args, new_e);
    }

    @Override
    public AstExp visit(@Nonnull AstLetRec e) {
        AstExp old_e = e.ret;
        AstExp new_e = old_e.accept(this);
        AstFunDef old_fd = e.fd;
        AstFunDef new_fd = (AstFunDef) old_fd.accept(this);
        if (new_fd == e.fd && new_e == e.ret) return e;
        return new AstLetRec(new_fd, new_e);
    }

}
