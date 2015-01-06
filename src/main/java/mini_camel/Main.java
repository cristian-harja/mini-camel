package mini_camel;

import java.io.*;

public class Main {
  static public void main(String argv[]) {    
    try {
      Parser p = new Parser(new Lexer(new FileReader(argv[0])));
      Exp result = (Exp) p.parse().value;
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

