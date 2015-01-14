package mini_camel.ir;

import mini_camel.Id;
import mini_camel.ast.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CodeGenerator
 */
public class CodeGenerator implements Visitor2<AstExp, T>{

    private int va;

    public CodeGenerator () {
        va = 0;
    }

    public Couple visit(AstUnit e) {
        Couple c = new Couple(Collections.EMPTY_LIST, null);
        return c;
    }

    /*public Couple visit(AstBool e) {
        va++;
        Var v = new Var("V"+va);
        Couple c = new Couple(Collections.EMPTY_LIST, v);
        return c;
    }*/

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

   /* public Couple visit(AstNot e) {
        va++;
        Var v = new Var("V"+va);
        Couple c = new Couple(Collections.EMPTY_LIST, v);
        return c;
    }*/

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
    
    
    
    
    public void visit(AstLE e) {

    }

    public void visit(AstIf e) {

    }

    public void visit(AstLet e) {
        Couple cou1 = visit(e.e1.accept(this));
    }

    public void visit(AstVar e) {

    }

    public void visit(AstLetRec e) {

    }

    public void visit(AstApp e) {

    }

    public void visit(AstTuple e) {

    }

    public void visit(AstLetTuple e) {

    }

    public void visit(AstArray e) {

    }

    public void visit(AstGet e) {

    }

    public void visit(AstPut e) {

    }

    public void visit(AstFunDef e) {

    }
}
