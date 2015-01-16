package mini_camel.gen;
import ldf.java_cup.runtime.TokenFactory;
import ldf.java_cup.runtime.Symbol;

import mini_camel.ast.*;

import java.io.Reader;

%%
   
/* -----------------Options and Declarations Section----------------- */
   
%public
%class Lexer
%implements sym
%implements ldf.java_cup.runtime.Scanner
%unicode
%line
%column
%char
%type ldf.java_cup.runtime.Symbol
%function next_token

%eofval{
    return eof();
%eofval}

%{
    private TokenFactory tokenFactory;

    public Lexer(Reader isr, TokenFactory tf){
        this(isr);
        tokenFactory = tf;
    }

    public Symbol symbol(String name, int code){
        return symbol(name, code, null);
    }

    public Symbol symbol(String name, int code, Object value){

        Symbol tok = tokenFactory.newToken(
            name, code,
            yyline + 1,
            yycolumn + 1,
            yychar
        );

        if (value instanceof AstNode) {
            ((AstNode)value).setSymbol(tok);
        }

        tok.value = value;
        return tok;

    }

    public Symbol eof() {
        return tokenFactory.newEOF(
            "EOF",sym.EOF,
            yyline + 1,
            yycolumn + 1,
            yychar
        );
    }

    public void comment() {
        tokenFactory.newComment(
            yyline + 1,
            yycolumn + 1,
            yychar
        );
    }
    public void whitespace() {
        tokenFactory.signalWhitespace(
            yyline + 1,
            yycolumn + 1,
            yychar
        );
    }

    public Symbol symbol(int symCode) {
        return symbol(ParserHelper.getSymName(symCode), symCode);
    }

    public Symbol symbol(int symCode, Object value) {
        return symbol(ParserHelper.getSymName(symCode), symCode, value);
    }

    private Symbol symbolInteger(int radix) {
        return symbolInteger(yytext(), radix);
    }

    private Symbol symbolInteger(String lexeme, int radix) {
        try {
            return symbol(INT, Integer.parseInt(lexeme, radix));
        } catch (NumberFormatException e) {
            return symbol(error, "Invalid number format");
        }
    }

    private Symbol symbolFloat() {
        return symbolFloat(yytext());
    }

    private Symbol symbolFloat(String lexeme) {
        try {
            return symbol(FLOAT, Float.parseFloat(lexeme));
        } catch (NumberFormatException e) {
            return symbol(error, "Invalid number format");
        }
    }
    private Symbol symbolBoolean(boolean value) {
        return symbol(BOOL, value);
    }

%}

/*
  The current line number can be accessed with the variable yyline
  and the current column number with the variable yycolumn.
*/
%line
%column
   

/*
  Macro Declarations
  
  These declarations are regular expressions that will be used latter
  in the Lexical Rules Section.  
*/
   
space = [ \t\n\r]
digit = [0-9]
lower = [a-z]
upper = [A-Z]
comment =  "(*" [^*] ~"*)" 
%%
/* ------------------------Lexical Rules Section---------------------- */
   
/*
   This section contains regular expressions and actions, i.e. Java
   code, that will be executed when the scanner matches the associated
   regular expression. */
   
   /* YYINITIAL is the state at which the lexer begins scanning.  So
   these regular expressions will only be matched if the scanner is in
   the start state YYINITIAL. */
   
<YYINITIAL> {

{space}+    { whitespace(); }
{comment}   { comment(); }
"("     { return symbol(sym.LPAREN); }
")"     { return symbol(sym.RPAREN); }
"true"  { return symbolBoolean(true); }
"false" { return symbolBoolean(false); }
"not"   { return symbol(sym.NOT); }

{digit}+  { return symbolInteger(10); }
{digit}+ ("." {digit}*)? (["e" "E"] ["+" "-"]? digit+)?  
        { return symbolFloat(); }

"-"     { return symbol(sym.MINUS); }
"+"     { return symbol(sym.PLUS); }
"-."    { return symbol(sym.MINUS_DOT); }
"+."    { return symbol(sym.PLUS_DOT); }
"*."    { return symbol(sym.AST_DOT); }
"/."    { return symbol(sym.SLASH_DOT); }
"="     { return symbol(sym.EQUAL); }
"<>"    { return symbol(sym.LESS_GREATER); }
"<="    { return symbol(sym.LESS_EQUAL); }
">="    { return symbol(sym.GREATER_EQUAL); }
"<"     { return symbol(sym.LESS); }
">"     { return symbol(sym.GREATER); }
"if"    { return symbol(sym.IF); }
"then"  { return symbol(sym.THEN); }
"else"  { return symbol(sym.ELSE); }
"let"   { return symbol(sym.LET); }
"in"    { return symbol(sym.IN); }
"rec"   { return symbol(sym.REC); }
","     { return symbol(sym.COMMA); }
"_"     { return symbol(sym.IDENT, Id.gen()); }
"Array.create" { return symbol(sym.ARRAY_CREATE); }
"."     { return symbol(sym.DOT); }
"<-"    { return symbol(sym.LESS_MINUS); }
";"     { return symbol(sym.SEMICOLON); }
eof     { return symbol(sym.EOF); }

{lower} ({digit}|{lower}|{upper}|"_")*   { return symbol(sym.IDENT, new Id(yytext())); }
}
[^]                    { throw new Error("Illegal character <"+yytext()+">"); }



