package mini_camel;

import mini_camel.ast.AstExp;

import java.io.*;

public class Main {
  static public void main(String argv[]) {    
    try {
      // Parser p = new Parser(new Lexer(new FileReader(argv[0])));
      Parser p = new Parser(new Lexer(new StringReader(
              "let x = 3 + y in z + x"
      )));
      AstExp result = (AstExp) p.parse().value;
      if (result == null) {
          return;
      }
      result.accept(new PrintVisitor());
      System.out.println();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

