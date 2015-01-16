package mini_camel;

import mini_camel.ast.*;

import javax.annotation.Nonnull;
import java.io.PrintStream;
import java.util.*;

public final class PrintVisitor implements Visitor {

    private PrintStream out;
    
    public PrintVisitor(PrintStream out) {
        this.out = out;
    }
    
    public void visit(@Nonnull AstUnit e) {
        out.print("()");
    }

    public void visit(@Nonnull AstBool e) {
        out.print(e.b);
    }

    public void visit(@Nonnull AstInt e) {
        out.print(e.i);
    }

    public void visit(@Nonnull AstFloat e) {
        String s = String.format("%.2f", e.f);
        out.print(s);
    }

    public void visit(@Nonnull AstNot e) {
        out.print("(not ");
        e.e.accept(this);
        out.print(")");
    }

    public void visit(@Nonnull AstNeg e) {
        out.print("(- ");
        e.e.accept(this);
        out.print(")");
    }

    public void visit(@Nonnull AstAdd e) {
        out.print("(");
        e.e1.accept(this);
        out.print(" + ");
        e.e2.accept(this);
        out.print(")");
    }

    public void visit(@Nonnull AstSub e) {
        out.print("(");
        e.e1.accept(this);
        out.print(" - ");
        e.e2.accept(this);
        out.print(")");
    }

    public void visit(@Nonnull AstFNeg e){
        out.print("(-. ");
        e.e.accept(this);
        out.print(")");
    }

    public void visit(@Nonnull AstFAdd e){
        out.print("(");
        e.e1.accept(this);
        out.print(" +. ");
        e.e2.accept(this);
        out.print(")");
    }

    public void visit(@Nonnull AstFSub e){
        out.print("(");
        e.e1.accept(this);
        out.print(" -. ");
        e.e2.accept(this);
        out.print(")");
    }

    public void visit(@Nonnull AstFMul e) {
        out.print("(");
        e.e1.accept(this);
        out.print(" *. ");
        e.e2.accept(this);
        out.print(")");
    }

    public void visit(@Nonnull AstFDiv e){
        out.print("(");
        e.e1.accept(this);
        out.print(" /. ");
        e.e2.accept(this);
        out.print(")");
    }

    public void visit(@Nonnull AstEq e){
        out.print("(");
        e.e1.accept(this);
        out.print(" = ");
        e.e2.accept(this);
        out.print(")");
    }

    public void visit(@Nonnull AstLE e){
        out.print("(");
        e.e1.accept(this);
        out.print(" <= ");
        e.e2.accept(this);
        out.print(")");
    }

    public void visit(@Nonnull AstIf e){
        out.print("(if ");
        e.e1.accept(this);
        out.print(" then ");
        e.e2.accept(this);
        out.print(" else ");
        e.e3.accept(this);
        out.print(")");
    }

    public void visit(@Nonnull AstLet e) {
        out.print("(let ");
        out.print(e.id);
        out.print(" = ");
        e.e1.accept(this);
        out.print(" in ");
        e.e2.accept(this);
        out.print(")");
    }

    public void visit(@Nonnull AstVar e){
        out.print(e.id.id);
    }


    // print sequence of identifiers 
    private void printInfix(List<Id> l, String op) {
        if (l.isEmpty()) {
            return;
        }
        Iterator<Id> it = l.iterator();
        out.print(it.next().id);
        while (it.hasNext()) {
            out.print(op);
            out.print(it.next().id);
        }
    }

    // print sequence of ast.Exp
    private void printInfix2(List<AstExp> l, String op) {
        if (l.isEmpty()) {
            return;
        }
        Iterator<AstExp> it = l.iterator();
        it.next().accept(this);
        while (it.hasNext()) {
            out.print(op);
            it.next().accept(this);
        }
    }

    public void visit(@Nonnull AstLetRec e){
        out.print("(let rec ");
        out.print(e.fd.id.id);
        out.print(" ");
        printInfix(e.fd.args, " ");
        out.print(" = ");
        e.fd.e.accept(this);
        out.print(" in ");
        e.e.accept(this);
        out.print(")");
    }

    public void visit(@Nonnull AstApp e){
        out.print("(");
        e.e.accept(this);
        out.print(" ");
        printInfix2(e.es, " ");
        out.print(")");
    }

    public void visit(@Nonnull AstTuple e){
        out.print("(");
        printInfix2(e.es, ", ");
        out.print(")");
    }

    public void visit(@Nonnull AstLetTuple e){
        out.print("(let (");
        printInfix(e.ids, ", ");
        out.print(") = ");
        e.e1.accept(this);
        out.print(" in ");
        e.e2.accept(this);
        out.print(")");
    }

    public void visit(@Nonnull AstArray e){
        out.print("(Array.create ");
        e.e1.accept(this);
        out.print(" ");
        e.e2.accept(this);
        out.print(")");
    }

    public void visit(@Nonnull AstGet e){
        e.e1.accept(this);
        out.print(".(");
        e.e2.accept(this);
        out.print(")");
    }

    public void visit(@Nonnull AstPut e){
        out.print("(");
        e.e1.accept(this);
        out.print(".(");
        e.e2.accept(this);
        out.print(") <- ");
        e.e3.accept(this);
        out.print(")");
    }

    public void visit(@Nonnull AstFunDef e) {

    }

    @Override
    public void visit(@Nonnull AstErr astErr) {
        out.print("<error>");
    }
}


