package mini_camel.ir;

import mini_camel.Id;
import mini_camel.ast.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CodeGenerator
 */
public class CodeGenerator implements Visitor3{

    private int va;

    public CodeGenerator () {
        va = 0;
    }

    public Couple visit(AstUnit e) {
        Couple c = new Couple(Collections.EMPTY_LIST, null);
        return c;
    }

    @Override
    public Couple visit(AstBool e) {
        return null;
    }

    public Couple visit(AstInt e) {
        va++;
        Var v = new Var("V"+va);
        List<Instr> l = new ArrayList<Instr>();
        Op o = new Const(e.i);
        Instr i = new Equal(v,o);
        l.add(i);
        Couple c = new Couple(l, v);
        return c;
    }


    public Couple visit(AstFloat e) {
        va++;
        Var v = new Var("V"+va);
        List<Instr> l = new ArrayList<Instr>();
        Op o = new Const(e.f);
        Instr i = new Equal(v,o);
        l.add(i);
        Couple c = new Couple(l, v);
        return c;
    }

    @Override
    public Couple visit(AstNot e) {
        return null;
    }

    public Couple visit(AstNeg e) {
        Couple cou1 = new Couple();
        Op zero = new Const(0);
        Couple cou2 = visit((AstInt) e.e);
        va++;
        Var v = new Var("V"+va);
        List<Instr> l = new ArrayList<Instr>();
        Instr i = new Sub(v,cou1.getVar(),cou2.getVar());
        cou1.addListInstr(l);
        cou2.addListInstr(l);
        l.add(i);
        Couple c = new Couple(l,v);
        return c;
    }

    public Couple visit(AstAdd e) {
        Couple cou1 = visit((AstInt) e.e1);
        Couple cou2 = visit((AstInt) e.e2);
        va++;
        Var v = new Var("V"+va);
        List<Instr> l = new ArrayList<Instr>();
        Instr i = new Add(v,cou1.getVar(),cou2.getVar());
        cou1.addListInstr(l);
        cou2.addListInstr(l);
        l.add(i);
        Couple c = new Couple(l,v);
        return c;
    }


    public Couple visit(AstSub e) {
        Couple cou1 = visit((AstInt) e.e1);
        Couple cou2 = visit((AstInt) e.e2);
        va++;
        Var v = new Var("V"+va);
        List<Instr> l = new ArrayList<Instr>();
        Instr i = new Sub(v,cou1.getVar(),cou2.getVar());
        cou1.addListInstr(l);
        cou2.addListInstr(l);
        l.add(i);
        Couple c = new Couple(l,v);
        return c;
    }


    public Couple visit(AstFNeg e) {
        Couple cou1 = new Couple();
        Op zero = new Const((float)0.0);
        Couple cou2 = visit((AstInt) e.e);
        va++;
        Var v = new Var("V"+va);
        List<Instr> l = new ArrayList<Instr>();
        Instr i = new SubF(v,cou1.getVar(),cou2.getVar());
        cou1.addListInstr(l);
        cou2.addListInstr(l);
        l.add(i);
        Couple c = new Couple(l,v);
        return c;
    }


    public Couple visit(AstFAdd e) {
        AstInt tmp1 = (AstInt)e.e1;
        AstInt tmp2 = (AstInt)e.e2;
        Couple cou1 = visit(tmp1);
        Couple cou2 = visit(tmp2);
        va++;
        Var v = new Var("V"+va);
        List<Instr> l = new ArrayList<Instr>();
        Instr i = new AddF(v,cou1.getVar(),cou2.getVar());
        cou1.addListInstr(l);
        cou2.addListInstr(l);
        l.add(i);
        Couple c = new Couple(l,v);
        return c;
    }

    public Couple visit(AstFSub e) {
        AstInt tmp1 = (AstInt)e.e1;
        AstInt tmp2 = (AstInt)e.e2;
        Couple cou1 = visit(tmp1);
        Couple cou2 = visit(tmp2);
        va++;
        Var v = new Var("V"+va);
        List<Instr> l = new ArrayList<Instr>();
        Instr i = new SubF(v,cou1.getVar(),cou2.getVar());
        cou1.addListInstr(l);
        cou2.addListInstr(l);
        l.add(i);
        Couple c = new Couple(l,v);
        return c;
    }

    public Couple visit(AstFMul e) {
        AstInt tmp1 = (AstInt)e.e1;
        AstInt tmp2 = (AstInt)e.e2;
        Couple cou1 = visit(tmp1);
        Couple cou2 = visit(tmp2);
        va++;
        Var v = new Var("V"+va);
        List<Instr> l = new ArrayList<Instr>();
        Instr i = new MultF(v,cou1.getVar(),cou2.getVar());
        cou1.addListInstr(l);
        cou2.addListInstr(l);
        l.add(i);
        Couple c = new Couple(l,v);
        return c;
    }

    public Couple visit(AstFDiv e) {
        AstInt tmp1 = (AstInt)e.e1;
        AstInt tmp2 = (AstInt)e.e2;
        Couple cou1 = visit(tmp1);
        Couple cou2 = visit(tmp2);
        va++;
        Var v = new Var("V"+va);
        List<Instr> l = new ArrayList<Instr>();
        Instr i = new DivF(v,cou1.getVar(),cou2.getVar());
        cou1.addListInstr(l);
        cou2.addListInstr(l);
        l.add(i);
        Couple c = new Couple(l,v);
        return c;
    }

    @Override
    public Couple visit(AstEq e) {
        return null;
    }

    /*public Couple visit(AstEq e) {
        AstInt tmp1 = (AstInt)e.e1;
        AstInt tmp2 = (AstInt)e.e2;
        Couple cou1 = visit(tmp1);
        Couple cou2 = visit(tmp2);
        va++;
        Var v = new Var("V"+va);
        List<Instr> l = new ArrayList<Instr>();
        Instr i = new Equal(v,cou1.getVar(),cou2.getVar());
        l.add(i);
        Couple c = new Couple(l,v);
        return c;
    }*/


    protected Couple recursiveVisit(@Nonnull AstExp e) {
        return e.accept(this);
    }
    
    public Couple visit(AstLE e) {

        return null;
    }

    public Couple visit(AstIf e) {
        return null;
    }

    public Couple visit(AstLet e) {
        Couple cou1 = recursiveVisit(e.e1);
        Couple cou2 = recursiveVisit(e.e2);
        va++;
        Var v = new Var("V"+va);
        Instr i = new Let(v,cou1.getVar());

        List<Instr> l = new ArrayList<Instr>();
        cou1.addListInstr(l);
        l.add(i);
        cou2.addListInstr(l);

        return new Couple(l,cou2.getVar());
    }

    public Couple visit(AstVar e) {
        Instr i;
        List<Instr> l = new ArrayList<Instr>();
        if(e.id.id.equals("print_newline"))
        {
            i = new Routine(e.id.id);
            l.add(i);
            return new Couple(l,null);
        }
        va++;
        Var v = new Var("V"+va);
        return new Couple(Collections.EMPTY_LIST, v);
    }

    public Couple visit(AstLetRec e) {
        return null;
    }

    public Couple visit(AstApp e) {
        return null;
    }

    public Couple visit(AstTuple e) {
        return null;
    }

    public Couple visit(AstLetTuple e) {
        return null;
    }

    public Couple visit(AstArray e) {
        return null;
    }

    public Couple visit(AstGet e) {
        return null;
    }

    public Couple visit(AstPut e) {
        return null;
    }

    public Couple visit(AstFunDef e) {
        return null;
    }
    
}
