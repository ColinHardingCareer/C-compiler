public class ParseTree {

    String lexeme;
    ParseTree left;
    ParseTree right;
    ParseTree parent;

    ParseTree() {
        lexeme = "";
        left = null;
        right = null;
    }

    ParseTree(String lexeme) {
        this.lexeme = lexeme;
        left = null;
        right = null;
    }
    ParseTree(String lexeme,ParseTree left, ParseTree right){
        this.lexeme = lexeme;
        this.left = left;
        this.right = right;
    }
}
