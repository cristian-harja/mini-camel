package mini_camel;

import mini_camel.ast.AstExp;

import java.io.*;

public class Main {
  static public void main(String argv[]) {    
    try {
      Reader r;
      if (argv.length > 0) {
        r = new FileReader(argv[0]);
      } else {
        r = new StringReader("let x = 3 + y in z + x");
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
  }
}

