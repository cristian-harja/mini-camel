package mini_camel.visit;

import mini_camel.ast.*;
import mini_camel.util.SymDef;
import mini_camel.util.SymRef;
import mini_camel.util.Visitor;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.PrintStream;
import java.util.*;

@ParametersAreNonnullByDefault
public final class PrintVisitor implements Visitor {

    private PrintStream out;

    public PrintVisitor(PrintStream out) {
        this.out = out;
    }

    public void visit(AstUnit e) {
        out.print("()");
    }

    public void visit(AstBool e) {
        out.print(e.b);
    }

    public void visit(AstInt e) {
        out.print(e.i);
    }

    public void visit(AstFloat e) {
        String s = String.format("%.2f", e.f);
        out.print(s);
    }

    public void visit(AstNot e) {
        out.print("(not ");
        e.e.accept(this);
        out.print(")");
    }

    public void visit(AstNeg e) {
        out.print("(- ");
        e.e.accept(this);
        out.print(")");
    }

    public void visit(AstAdd e) {
        out.print("(");
        e.e1.accept(this);
        out.print(" + ");
        e.e2.accept(this);
        out.print(")");
    }

    public void visit(AstSub e) {
        out.print("(");
        e.e1.accept(this);
        out.print(" - ");
        e.e2.accept(this);
        out.print(")");
    }

    public void visit(AstFNeg e){
        out.print("(-. ");
        e.e.accept(this);
        out.print(")");
    }

    public void visit(AstFAdd e){
        out.print("(");
        e.e1.accept(this);
        out.print(" +. ");
        e.e2.accept(this);
        out.print(")");
    }

    public void visit(AstFSub e){
        out.print("(");
        e.e1.accept(this);
        out.print(" -. ");
        e.e2.accept(this);
        out.print(")");
    }

    public void visit(AstFMul e) {
        out.print("(");
        e.e1.accept(this);
        out.print(" *. ");
        e.e2.accept(this);
        out.print(")");
    }

    public void visit(AstFDiv e){
        out.print("(");
        e.e1.accept(this);
        out.print(" /. ");
        e.e2.accept(this);
        out.print(")");
    }

    public void visit(AstEq e){
        out.print("(");
        e.e1.accept(this);
        out.print(" = ");
        e.e2.accept(this);
        out.print(")");
    }

    public void visit(AstLE e){
        out.print("(");
        e.e1.accept(this);
        out.print(" <= ");
        e.e2.accept(this);
        out.print(")");
    }

    public void visit(AstIf e){
        out.print("(if ");
        e.eCond.accept(this);
        out.print(" then ");
        e.eThen.accept(this);
        out.print(" else ");
        e.eElse.accept(this);
        out.print(")");
    }

    public void visit(AstLet e) {
        out.print("(let ");
        out.print(e.decl.id);
        out.print(" = ");
        e.initializer.accept(this);
        out.print(" in ");
        e.ret.accept(this);
        out.print(")");
    }

    public void visit(SymRef e){
        out.print(e.id);
    }


    // print sequence of identifiers 
    private void printInfix(List<SymDef> l, String op) {
        if (l.isEmpty()) {
            return;
        }
        Iterator<SymDef> it = l.iterator();
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

    public void visit(AstLetRec e){
        out.print("(let rec ");
        out.print(e.fd.decl.id);
        out.print(" ");
        printInfix(e.fd.args, " ");
        out.print(" = ");
        e.fd.body.accept(this);
        out.print(" in ");
        e.ret.accept(this);
        out.print(")");
    }

    public void visit(AstApp e){
        out.print("(");
        e.e.accept(this);
        out.print(" ");
        printInfix2(e.es, " ");
        out.print(")");
    }

    public void visit(AstTuple e){
        out.print("(");
        printInfix2(e.es, ", ");
        out.print(")");
    }

    public void visit(AstLetTuple e){
        out.print("(let (");
        printInfix(e.ids, ", ");
        out.print(") = ");
        e.initializer.accept(this);
        out.print(" in ");
        e.ret.accept(this);
        out.print(")");
    }

    public void visit(AstArray e){
        out.print("(Array.create ");
        e.size.accept(this);
        out.print(" ");
        e.initializer.accept(this);
        out.print(")");
    }

    public void visit(AstGet e){
        e.array.accept(this);
        out.print(".(");
        e.index.accept(this);
        out.print(")");
    }

    public void visit(AstPut e){
        out.print("(");
        e.array.accept(this);
        out.print(".(");
        e.index.accept(this);
        out.print(") <- ");
        e.value.accept(this);
        out.print(")");
    }

    public void visit(AstFunDef e) {
        // nothing to do
    }

    public void visit(AstErr e) {
        out.print("<error>");
    }
}


