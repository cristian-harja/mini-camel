package mini_camel.visit;

import mini_camel.ast.*;
import mini_camel.util.SymDef;
import mini_camel.util.SymRef;
import mini_camel.util.Visitor;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;

@ParametersAreNonnullByDefault
public final class PrettyPrinter implements Visitor {

    private PrintStream out;
    private int indent;

    public PrettyPrinter(PrintStream out) {
        this.out = out;
    }

    private void nl(int indentChange) {
        indent += indentChange;
        nl();
    }

    private void nl() {
        out.print('\n');
        for (int i = 0; i < indent; ++i) {
            out.print('\t');
        }
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

    public void visit(AstFNeg e) {
        out.print("(-. ");
        e.e.accept(this);
        out.print(")");
    }

    public void visit(AstFAdd e) {
        out.print("(");
        e.e1.accept(this);
        out.print(" +. ");
        e.e2.accept(this);
        out.print(")");
    }

    public void visit(AstFSub e) {
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

    public void visit(AstFDiv e) {
        out.print("(");
        e.e1.accept(this);
        out.print(" /. ");
        e.e2.accept(this);
        out.print(")");
    }

    public void visit(AstEq e) {
        out.print("(");
        e.e1.accept(this);
        out.print(" = ");
        e.e2.accept(this);
        out.print(")");
    }

    public void visit(AstLE e) {
        out.print("(");
        e.e1.accept(this);
        out.print(" <= ");
        e.e2.accept(this);
        out.print(")");
    }

    public void visit(AstIf e) {
        out.print("(\n");
        nl();
        out.print("if ");
        indent++;
        e.eCond.accept(this);
        indent--;
        nl();

        out.print("then");
        nl(+1);
        e.eThen.accept(this);
        nl(-1);

        out.print("else");
        nl(+1);
        e.eElse.accept(this);
        nl(-1);
        out.print(")");
    }

    public void visit(AstLet e) {
        out.print("let ");
        out.print(e.decl.id);
        out.print(" = ");
        e.initializer.accept(this);
        out.print(" in");
        nl();
        e.ret.accept(this);
    }


    public void visit(SymRef e) {
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

    public void visit(AstLetRec e) {
        out.print("let rec ");
        out.print(e.fd.decl.id);
        out.print(" ");
        printInfix(e.fd.args, " ");
        out.print(" =");
        nl(+1);
        e.fd.body.accept(this);
        nl(-1);
        out.print("in");
        nl(+1);
        e.ret.accept(this);
        indent--;
    }

    public void visit(AstApp e) {
        out.print("(");
        e.e.accept(this);
        out.print(" ");
        printInfix2(e.es, " ");
        out.print(")");
    }

    public void visit(AstTuple e) {
        out.print("(");
        printInfix2(e.es, ", ");
        out.print(")");
    }

    public void visit(AstLetTuple e) {
        out.print("let (");
        printInfix(e.ids, ", ");
        out.print(") = ");
        e.initializer.accept(this);
        out.print(" in");
        nl(+1);
        e.ret.accept(this);
        nl(-1);
    }

    public void visit(AstArray e) {
        out.print("(Array.create ");
        e.size.accept(this);
        out.print(" ");
        e.initializer.accept(this);
        out.print(")");
    }

    public void visit(AstGet e) {
        e.array.accept(this);
        out.print(".(");
        e.index.accept(this);
        out.print(")");
    }

    public void visit(AstPut e) {
        out.print("(");
        e.array.accept(this);
        out.print(".(");
        e.index.accept(this);
        out.print(") <- ");
        e.value.accept(this);
        out.print(")");
    }

    public void visit(AstFunDef e) {

    }

    public void visit(AstErr astErr) {
        out.print("<error>");
    }
}


