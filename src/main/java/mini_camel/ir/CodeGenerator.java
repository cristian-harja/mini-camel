package mini_camel.ir;

import mini_camel.ast.*;
import mini_camel.ir.instr.*;
import mini_camel.ir.op.ConstFloat;
import mini_camel.ir.op.ConstInt;
import mini_camel.ir.op.Operand;
import mini_camel.ir.op.Var;
import mini_camel.util.SymDef;
import mini_camel.util.SymRef;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * CodeGenerator
 */
public class CodeGenerator extends CodeGeneratorHelper {

    private static int va = 0;

    private Queue<AstFunDef> functions = new LinkedList<>();
    private List<Var> locals = new ArrayList<>();

    private Couple result = null;

    private CodeGenerator() {
    }

    public static List<Function> generateIR(AstExp root, String mainFunName) {
        HashSet<String> compiled = new LinkedHashSet<>();
        List<Function> functions = new LinkedList<>();
        Queue<AstFunDef> pending = new LinkedList<>();
        List<Var> args = Collections.emptyList();
        CodeGenerator cg;
        AstExp node = root;
        String name = mainFunName;

        while (true) {
            cg = new CodeGenerator();
            cg.result = node.accept(cg, null);

            List<Instr> body = cg.result.getInstr();
            body.add(new Ret(cg.result.getVar())); // fixme

            functions.add(new Function(
                    new Label(name),
                    args,
                    cg.locals,
                    body
            ));

            pending.addAll(cg.functions);

            if (pending.isEmpty()) break;
            AstFunDef fd = pending.remove();

            name = fd.decl.id;

            if (compiled.contains(name)) break;
            compiled.add(name);

            node = fd.body;
            args = new ArrayList<>(fd.args.size());
            for (SymDef id : fd.args) {
                args.add(new Var(id.id));
            }
        }

        return functions;

    }

    private Var genVar() {
        Var v = new Var("V" + ++va);
        locals.add(v);
        return v;
    }

    public Couple visit(Branches b, @Nonnull AstUnit e) {
        return new Couple(Collections.<Instr>emptyList(), null);
    }

    public Couple visit(Branches b, @Nonnull AstBool e) {//TODO if non null
        ArrayList<Instr> l = new ArrayList<>(1);
        if (b != null) {
            l.add(new Jump(e.b ? b.ifTrue() : b.ifFalse()));
            return new Couple(l, null);
        } else {
            Var v = genVar();
            l.add(new Assign(v, new ConstInt(e.b ? 1 : 0)));
            return new Couple(l, v);
        }
    }

    public Couple visit(Branches b, @Nonnull AstInt e) {
        Var v = genVar();
        List<Instr> l = new ArrayList<>();
        l.add(new Assign(v, new ConstInt(e.i)));
        return new Couple(l, v);
    }


    public Couple visit(Branches b, @Nonnull AstFloat e) {
        Var v = genVar();
        List<Instr> l = new ArrayList<>();
        l.add(new Assign(v, new ConstFloat(e.f)));
        return new Couple(l, v);
    }


    public Couple visit(Branches b, @Nonnull AstNot e) {
        return e.e.accept(this, new Branches(b.ifFalse(), b.ifTrue()));
    }

    public Couple visit(Branches b, @Nonnull AstNeg e) {
        Var v = genVar();
        List<Instr> l = new ArrayList<>();
        Couple c = e.e.accept(this, null);
        l.addAll(c.getInstr());
        l.add(new SubI(v, new ConstInt(0), c.getVar()));
        return new Couple(l, v);
    }

    public Couple visit(Branches b, @Nonnull AstAdd e) {
        Var v = genVar();
        List<Instr> l = new ArrayList<>();
        Couple cou1 = e.e1.accept(this, null);
        Couple cou2 = e.e2.accept(this, null);
        l.addAll(cou1.getInstr());
        l.addAll(cou2.getInstr());
        l.add(new AddI(v, cou1.getVar(), cou2.getVar()));
        return new Couple(l, v);
    }

    public Couple visit(Branches b, @Nonnull AstSub e) {
        Var v = genVar();
        List<Instr> l = new ArrayList<>();
        Couple cou1 = e.e1.accept(this, null);
        Couple cou2 = e.e2.accept(this, null);
        l.addAll(cou1.getInstr());
        l.addAll(cou2.getInstr());
        l.add(new SubI(v, cou1.getVar(), cou2.getVar()));
        return new Couple(l, v);
    }

    public Couple visit(Branches b, @Nonnull AstFNeg e) {
        Var v = genVar();
        List<Instr> l = new ArrayList<>();
        Couple c = e.e.accept(this, null);
        l.addAll(c.getInstr());
        l.add(new SubF(v, new ConstFloat(0.0f), c.getVar()));
        return new Couple(l, v);
    }


    public Couple visit(Branches b, @Nonnull AstFAdd e) {
        Var v = genVar();
        List<Instr> l = new ArrayList<>();
        Couple cou1 = e.e1.accept(this, null);
        Couple cou2 = e.e2.accept(this, null);
        l.addAll(cou1.getInstr());
        l.addAll(cou2.getInstr());
        l.add(new AddF(v, cou1.getVar(), cou2.getVar()));
        return new Couple(l, v);
    }

    public Couple visit(Branches b, @Nonnull AstFSub e) {
        Var v = genVar();
        List<Instr> l = new ArrayList<>();
        Couple cou1 = e.e1.accept(this, null);
        Couple cou2 = e.e2.accept(this, null);
        l.addAll(cou1.getInstr());
        l.addAll(cou2.getInstr());
        l.add(new SubF(v, cou1.getVar(), cou2.getVar()));
        return new Couple(l, v);
    }

    public Couple visit(Branches b, @Nonnull AstFMul e) {
        Var v = genVar();
        List<Instr> l = new ArrayList<>();
        Couple cou1 = e.e1.accept(this, null);
        Couple cou2 = e.e2.accept(this, null);
        l.addAll(cou1.getInstr());
        l.addAll(cou2.getInstr());
        l.add(new MultF(v, cou1.getVar(), cou2.getVar()));
        return new Couple(l, v);
    }

    public Couple visit(Branches b, @Nonnull AstFDiv e) {
        Var v = genVar();
        List<Instr> l = new ArrayList<>();
        Couple cou1 = e.e1.accept(this, null);
        Couple cou2 = e.e2.accept(this, null);
        l.addAll(cou1.getInstr());
        l.addAll(cou2.getInstr());
        l.add(new DivF(v, cou1.getVar(), cou2.getVar()));
        return new Couple(l, v);
    }


    @Override
    public Couple visit(Branches b, @Nonnull AstEq e) {
        List<Instr> l = new ArrayList<>();
        Couple cou1 = e.e1.accept(this, null);
        Couple cou2 = e.e2.accept(this, null);
        l.addAll(cou1.getInstr());
        l.addAll(cou2.getInstr());
        l.add(new Branch(
                // false -> strict equality
                false, cou1.getVar(), cou2.getVar(), // operand1 = operand2
                b.ifTrue(), b.ifFalse()
        ));
        return new Couple(l, null);
    }

    public Couple visit(Branches b, @Nonnull AstLE e) {
        List<Instr> l = new ArrayList<>();
        Couple cou1 = e.e1.accept(this, null);
        Couple cou2 = e.e2.accept(this, null);
        l.addAll(cou1.getInstr());
        l.addAll(cou2.getInstr());
        l.add(new Branch(
                // true -> less or equal
                true, cou1.getVar(), cou2.getVar(), // operand1 <= operand2
                b.ifTrue(), b.ifFalse()
        ));
        return new Couple(l, null);
    }

    public Couple visit(Branches b, @Nonnull AstIf e) {
        Var result = genVar();
        List<Instr> l = new ArrayList<>();
        Label lThen = Label.gen();
        Label lElse = Label.gen();
        Label lEndIf = Label.gen();
        Couple cou1 = e.eCond.accept(this, new Branches(lThen, lElse));
        Couple cou2 = e.eThen.accept(this, null);
        Couple cou3 = e.eElse.accept(this, null);
        l.addAll(cou1.getInstr());
        l.add(lThen);
        l.addAll(cou2.getInstr());
        l.add(new Assign(result, cou2.getVar()));
        l.add(new Jump(lEndIf));
        l.add(lElse);
        l.addAll(cou3.getInstr());
        l.add(new Assign(result, cou3.getVar()));
        l.add(lEndIf);

        return new Couple(l, result);
    }

    public Couple visit(Branches b, @Nonnull AstLet e) {
        List<Instr> l = new ArrayList<>();
        Couple cou1 = e.initializer.accept(this, null);
        Couple cou2 = e.ret.accept(this, null);

        l.addAll(cou1.getInstr());
        Var v = new Var(e.decl.id);
        l.add(new Assign(v, cou1.getVar()));
        locals.add(v);
        l.addAll(cou2.getInstr());

        return new Couple(l, cou2.getVar());
    }

    public Couple visit(Branches b, @Nonnull SymRef e) {
        if (b == null) {
            return new Couple(Collections.<Instr>emptyList(), new Var(e.id));
        } else {
            List<Instr> l = new ArrayList<>();
            Var v = new Var(e.id);
            Branch newb = new Branch(false, v, new ConstInt(1), b.ifTrue(), b.ifFalse());
            l.add(newb);
            return new Couple(l, null);

        }

    }

    public Couple visit(Branches b, @Nonnull AstLetRec e) {
        functions.add(e.fd);
        return e.ret.accept(this, null);
    }

    public Couple visit(Branches b, @Nonnull AstApp e) {
        List<Instr> code = new LinkedList<>();
        List<Operand> args = new LinkedList<>();
        Var ret = genVar();

        for (AstExp arg : e.es) {
            Couple argCode = arg.accept(this, null);
            code.addAll(argCode.getInstr());
            if (argCode.getVar() == null) continue;
            args.add(argCode.getVar());
        }

        Couple fCode = e.e.accept(this, null);

        code.addAll(fCode.getInstr());
        code.add(new DirApply(ret, fCode.getVar().name, args));

        return new Couple(code, ret);
    }

    public Couple visit(Branches b, @Nonnull AstTuple e) {
        return throwNoTuple();
    }

    public Couple visit(Branches b, @Nonnull AstLetTuple e) {
        return throwNoTuple();
    }

    public Couple visit(Branches b, @Nonnull AstArray e) {
        return throwNoArrays();
    }

    public Couple visit(Branches b, @Nonnull AstGet e) {
        return throwNoArrays();
    }

    public Couple visit(Branches b, @Nonnull AstPut e) {
        return throwNoArrays();
    }

    private Couple throwNoTuple() {
        throw new RuntimeException("Sorry, tuples are not implemented yet");
    }

    private Couple throwNoArrays() {
        throw new RuntimeException("Sorry, arrays are not implemented yet");
    }

    public Couple visit(Branches b, @Nonnull AstFunDef e) {
        // handled somewhere else
        return null;
    }

    public Couple visit(Branches b, @Nonnull AstErr e) {
        throw new RuntimeException("Compiling an `AstErr`??");
    }

}
