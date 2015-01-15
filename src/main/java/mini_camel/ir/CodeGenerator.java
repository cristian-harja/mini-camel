package mini_camel.ir;

import mini_camel.ast.*;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * CodeGenerator
 */
public class CodeGenerator implements Visitor3 {

    private int va;

    Queue<AstFunDef> functions = new LinkedList<>();

    Couple result = null;

    public CodeGenerator() {
        va = 0;
    }

    public void generateCode(AstExp e) {
        result = e.accept(this);
    }

    public static Queue<FunDef> generateIR(AstExp root, String mainFunName) {
        Queue<FunDef> functions = new LinkedList<>();
        Queue<AstFunDef> pending = new LinkedList<>();
        List<Var> args = Collections.emptyList();
        CodeGenerator cg;
        AstExp node = root;
        String name = mainFunName;

        while (true) {
            cg = new CodeGenerator();
            cg.generateCode(node);
            functions.add(new FunDef(new Var(name), args, cg.getCode()));

            pending.addAll(cg.functions);

            if (pending.isEmpty()) break;
            AstFunDef fd = pending.remove();

            node = fd.e;
            name = fd.id.id;
            args = new ArrayList<>(fd.args.size());
            for (Id id : fd.args) {
                args.add(new Var(id.id));
            }
        }

        return functions;

    }

    public List<Instr> getCode() {
        return result.getInstr();
    }

    public Couple visit(AstUnit e) {
        return new Couple(Collections.<Instr>emptyList(), null);
    }

    @Override
    public Couple visit(AstBool e) {
        return null; // todo
    }

    public Couple visit(AstInt e) {
        va++;
        Var v = new Var("V" + va);
        List<Instr> l = new ArrayList<Instr>();
        Op o = new Const(e.i);
        Instr i = new Assign(v, o);
        l.add(i);
        return new Couple(l, v);
    }


    public Couple visit(AstFloat e) {
        va++;
        Var v = new Var("V" + va);
        List<Instr> l = new ArrayList<Instr>();
        Op o = new Const(e.f);
        Instr i = new Assign(v, o);
        l.add(i);
        return new Couple(l, v);
    }

    @Override
    public Couple visit(AstNot e) {
        return null; // TODO
    }

    public Couple visit(AstNeg e) {
        Op zero = new Const(0);
        Couple c = visit((AstInt) e.e);
        Var v = new Var("V" + ++va);
        List<Instr> l = new ArrayList<Instr>();
        l.addAll(c.getInstr());
        l.add(new Sub(v, zero, c.getVar()));
        return new Couple(l, v);
    }

    public Couple visit(AstAdd e) {
        Couple cou1 = e.e1.accept(this);
        Couple cou2 = e.e2.accept(this);
        va++;
        Var v = new Var("V" + va);
        List<Instr> l = new ArrayList<>();
        Instr i = new Add(v, cou1.getVar(), cou2.getVar());
        l.addAll(cou1.getInstr());
        l.addAll(cou2.getInstr());
        l.add(i);
        return new Couple(l, v);
    }

    public Couple visit(AstSub e) {
        Couple cou1 = e.e1.accept(this);
        Couple cou2 = e.e2.accept(this);
        va++;
        Var v = new Var("V" + va);
        List<Instr> l = new ArrayList<Instr>();
        Instr i = new Sub(v, cou1.getVar(), cou2.getVar());
        l.addAll(cou1.getInstr());
        l.addAll(cou2.getInstr());
        l.add(i);
        return new Couple(l, v);
    }

    public Couple visit(AstFNeg e) {
        Op zero = new Const(0.0);
        Couple c = visit((AstInt) e.e);
        Var v = new Var("V" + ++va);
        List<Instr> l = new ArrayList<Instr>();
        l.addAll(c.getInstr());
        l.add(new SubF(v, zero, c.getVar()));
        return new Couple(l, v);
    }


    public Couple visit(AstFAdd e) {
        // todo: fix this
        Couple cou1 = e.e1.accept(this);
        Couple cou2 = e.e2.accept(this);
        va++;
        Var v = new Var("V" + va);
        List<Instr> l = new ArrayList<Instr>();
        Instr i = new AddF(v, cou1.getVar(), cou2.getVar());
        l.addAll(cou1.getInstr());
        l.addAll(cou2.getInstr());
        l.add(i);
        return new Couple(l, v);
    }

    public Couple visit(AstFSub e) {
        Couple cou1 = e.e1.accept(this);
        Couple cou2 = e.e2.accept(this);
        va++;
        Var v = new Var("V" + va);
        List<Instr> l = new ArrayList<Instr>();
        Instr i = new SubF(v, cou1.getVar(), cou2.getVar());
        l.addAll(cou1.getInstr());
        l.addAll(cou2.getInstr());
        l.add(i);
        return new Couple(l, v);
    }

    public Couple visit(AstFMul e) {
        Couple cou1 = e.e1.accept(this);
        Couple cou2 = e.e2.accept(this);
        va++;
        Var v = new Var("V" + va);
        List<Instr> l = new ArrayList<Instr>();
        Instr i = new MultF(v, cou1.getVar(), cou2.getVar());
        l.addAll(cou1.getInstr());
        l.addAll(cou2.getInstr());
        l.add(i);
        return new Couple(l, v);
    }

    public Couple visit(AstFDiv e) {
        Couple cou1 = e.e1.accept(this);
        Couple cou2 = e.e2.accept(this);
        va++;
        Var v = new Var("V" + va);
        List<Instr> l = new ArrayList<Instr>();
        Instr i = new DivF(v, cou1.getVar(), cou2.getVar());
        l.addAll(cou1.getInstr());
        l.addAll(cou2.getInstr());
        l.add(i);
        return new Couple(l, v);
    }

    @Override
    public Couple visit(AstEq e) {
        return null; // todo
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
        Var v = new Var(e.id.id);
        Instr i = new Assign(v, cou1.getVar());

        List<Instr> l = new ArrayList<>();
        l.addAll(cou1.getInstr());
        l.add(i);
        l.addAll(cou2.getInstr());

        return new Couple(l, cou2.getVar());
    }

    public Couple visit(AstVar e) {
        Var v = new Var(e.id.id);
        return new Couple(Collections.<Instr>emptyList(), v);
    }

    public Couple visit(AstLetRec e) {
        functions.add(e.fd);
        return e.e.accept(this);
    }

    public Couple visit(AstApp e) {
        List<Instr> code = new LinkedList<>();
        List<Op> args = new LinkedList<>();

        for (AstExp arg : e.es) {
            Couple argCode = arg.accept(this);
            code.addAll(argCode.getInstr());
            if (argCode.getVar() == null) continue;
            args.add(argCode.getVar());
        }

        Couple fCode = e.e.accept(this);

        code.addAll(fCode.getInstr());
        code.add(new Call(fCode.getVar().varName, args));

        return new Couple(code, new Var("ret"));
    }

    public Couple visit(AstTuple e) {
        // TODO: throw exception
        return null;
    }

    public Couple visit(AstLetTuple e) {
        // TODO: throw exception
        return null;
    }

    public Couple visit(AstArray e) {
        // TODO: throw exception
        return null;
    }

    public Couple visit(AstGet e) {
        // TODO: throw exception
        return null;
    }

    public Couple visit(AstPut e) {
        // TODO: throw exception
        return null;
    }

    public Couple visit(AstFunDef e) {
        // handled somewhere else
        return null;
    }

}
