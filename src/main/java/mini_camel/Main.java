package mini_camel;

import mini_camel.ir.*;

import java.util.ArrayList;
import java.util.List;

public class Main {
  static public void main(String argv[]) {    
    /*
    try {
      Reader r;
      if (argv.length > 0) {
        r = new FileReader(argv[0]);
      } else {
        r = new StringReader("let x = 3 + y in z + x"); //EXAMPLE 1
        r = new StringReader("let x = 3 in z + x");     //EXAMPLE 2
        r = new StringReader("let rec f x y z = x - y + z in f 1 2 3 ");     //EXAMPLE 3
      }
      Parser p = new Parser(new Lexer(r));
      AstExp result = (AstExp) p.parse().value;
      if (result == null) {
          return;
      }
      result.accept(new PrintVisitor(System.out));
      System.out.println();
    } catch (Exception e) {
      e.printStackTrace();
    }
    */

    // example 1
    /*
    Block b = new Block();
    Var x = new Var("x");
    Op op1 = new Const(3);
    Op op2 = new Var("y");
    Operation tmp1 = new Add(x, op1, op2);

    Var result = new Var("result");
    Op op3 = new Var("z");
    Operation tmp2 = new Add(result,x,op3);

    b.addInstr(tmp1);
    b.addInstr(tmp2);
    b.printInfo();
    */

    //example 2
    /*
    Block b = new Block();
    Var x = new Var("x");
    Op op1 = new Const(3);
    Operation tmp1 = new Equal(x,op1);

    Var result = new Var("result");
    Op op3 = new Var("z");
    Operation tmp2 = new Add(result,x,op3);

    b.addInstr(tmp1);
    b.addInstr(tmp2);
    b.printInfo();*/

    //example 3
    Block b = new Block();
    Var f = new Var("f");

    List<Var> args = new ArrayList<Var>();
    Var x = new Var("x");
    Var y = new Var("y");
    Var z = new Var("z");
    args.add(x);
    args.add(y);
    args.add(z);

    List<Operation> inst = new ArrayList<Operation>();
    Var result1 = new Var("result1");
    Var result = new Var("result");
    Operation tmp1 = new Add(result1,x,y);
    inst.add(tmp1);
    Operation tmp2 = new Add(result,result1,z);

    FunDef func = new FunDef(f,args,inst);

    b.addInstr(func);
    b.printInfo();

  }
}

