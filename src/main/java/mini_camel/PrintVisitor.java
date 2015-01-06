package mini_camel;

import mini_camel.ast.*;

import java.util.*;

public final class PrintVisitor implements Visitor {
    public void visit(AstUnit e) {
        System.out.print("()");
    }

    public void visit(AstBool e) {
        System.out.print(e.b);
    }

    public void visit(AstInt e) {
        System.out.print(e.i);
    }

    public void visit(AstFloat e) {
        String s = String.format("%.2f", e.f);
        System.out.print(s);
    }

    public void visit(AstNot e) {
        System.out.print("(not ");
        e.e.accept(this);
        System.out.print(")");
    }

    public void visit(AstNeg e) {
        System.out.print("(- ");
        e.e.accept(this);
        System.out.print(")");
    }

    public void visit(AstAdd e) {
        System.out.print("(");
        e.e1.accept(this);
        System.out.print(" + ");
        e.e2.accept(this);
        System.out.print(")");
    }

    public void visit(AstSub e) {
        System.out.print("(");
        e.e1.accept(this);
        System.out.print(" - ");
        e.e2.accept(this);
        System.out.print(")");
    }

    public void visit(AstFNeg e){
        System.out.print("(-. ");
        e.e.accept(this);
        System.out.print(")");
    }

    public void visit(AstFAdd e){
        System.out.print("(");
        e.e1.accept(this);
        System.out.print(" +. ");
        e.e2.accept(this);
        System.out.print(")");
    }

    public void visit(AstFSub e){
        System.out.print("(");
        e.e1.accept(this);
        System.out.print(" -. ");
        e.e2.accept(this);
        System.out.print(")");
    }

    public void visit(AstFMul e) {
        System.out.print("(");
        e.e1.accept(this);
        System.out.print(" *. ");
        e.e2.accept(this);
        System.out.print(")");
    }

    public void visit(AstFDiv e){
        System.out.print("(");
        e.e1.accept(this);
        System.out.print(" /. ");
        e.e2.accept(this);
        System.out.print(")");
    }

    public void visit(AstEq e){
        System.out.print("(");
        e.e1.accept(this);
        System.out.print(" = ");
        e.e2.accept(this);
        System.out.print(")");
    }

    public void visit(AstLE e){
        System.out.print("(");
        e.e1.accept(this);
        System.out.print(" <= ");
        e.e2.accept(this);
        System.out.print(")");
    }

    public void visit(AstIf e){
        System.out.print("(if ");
        e.e1.accept(this);
        System.out.print(" then ");
        e.e2.accept(this);
        System.out.print(" else ");
        e.e3.accept(this);
        System.out.print(")");
    }

    public void visit(AstLet e) {
        System.out.print("(let ");
        System.out.print(e.id);
        System.out.print(" = ");
        e.e1.accept(this);
        System.out.print(" in ");
        e.e2.accept(this);
        System.out.print(")");
    }

    public void visit(AstVar e){
        System.out.print(e.id);
    }


    // print sequence of identifiers 
    static <E> void printInfix(List<E> l, String op) {
        if (l.isEmpty()) {
            return;
        }
        Iterator<E> it = l.iterator();
        System.out.print(it.next());
        while (it.hasNext()) {
            System.out.print(op + it.next());
        }
    }

    // print sequence of ast.Exp
    void printInfix2(List<AstExp> l, String op) {
        if (l.isEmpty()) {
            return;
        }
        Iterator<AstExp> it = l.iterator();
        it.next().accept(this);
        while (it.hasNext()) {
            System.out.print(op);
            it.next().accept(this);
        }
    }

    public void visit(AstLetRec e){
        System.out.print("(let rec " + e.fd.id + " ");
        printInfix(e.fd.args, " ");
        System.out.print(" = ");
        e.fd.e.accept(this);
        System.out.print(" in ");
        e.e.accept(this);
        System.out.print(")");
    }

    public void visit(AstApp e){
        System.out.print("(");
        e.e.accept(this);
        System.out.print(" ");
        printInfix2(e.es, " ");
        System.out.print(")");
    }

    public void visit(AstTuple e){
        System.out.print("(");
        printInfix2(e.es, ", ");
        System.out.print(")");
    }

    public void visit(AstLetTuple e){
        System.out.print("(let (");
        printInfix(e.ids, ", ");
        System.out.print(") = ");
        e.e1.accept(this);
        System.out.print(" in ");
        e.e2.accept(this);
        System.out.print(")");
    }

    public void visit(AstArray e){
        System.out.print("(Array.create ");
        e.e1.accept(this);
        System.out.print(" ");
        e.e2.accept(this);
        System.out.print(")");
    }

    public void visit(AstGet e){
        e.e1.accept(this);
        System.out.print(".(");
        e.e2.accept(this);
        System.out.print(")");
    }

    public void visit(AstPut e){
        System.out.print("(");
        e.e1.accept(this);
        System.out.print(".(");
        e.e2.accept(this);
        System.out.print(") <- ");
        e.e3.accept(this);
        System.out.print(")");
    }

    public void visit(AstFunDef e) {

    }
}


