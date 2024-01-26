
/*
author: colin harding

Token is an obj that handles the tokens created for the symbol table

methods: set and gets//

*/
public class Token {

    private String lexeme;
    private int tokenType;
    private int value;
    private int size;// later
    private int elType;// later

    // default
    public Token() {

    }

    // lexeme constructor
    public Token(String l) {
        lexeme = l;

    }

    // full constructor
    public Token(String l, int t, int v, int s, int e) {
        lexeme = l;
        tokenType = t;
        value = v;
        size = s;
        elType = e;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String l) {
        lexeme = l;
    }

    public int getTokenType() {
        return tokenType;
    }

    public void setTokenType(int t) {
        tokenType = t;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int v) {
        value = v;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int s) {
        size = s;
    }

    public int getElType() {
        return elType;
    }

    public void setElType(int e) {
        elType = e;
    }

}
