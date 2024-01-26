
/*
author: colin harding

SymbolTable handles the symbol table for the compiler

SymbolTable contains a hashmap backed by a look up table.

Method fillSymTable() preconstructs the symbol table with "chip recognized tokens"

Method addToSymbolTable() adds new tokens to the data structure//

*/
import java.util.*;

public class SymbolTable {

    private HashMap<Integer, HashMap<String, Token>> st = new HashMap<>();

    private HashMap<Integer, ArrayList<Token>> listmap = new HashMap<>();

    private ArrayList<Token> reserveds = new ArrayList<Token>();
    private ArrayList<Token> operators = new ArrayList<Token>();
    private ArrayList<Token> punctuations = new ArrayList<Token>();
    private ArrayList<Token> identifiers = new ArrayList<Token>();
    private ArrayList<Token> integers = new ArrayList<Token>();
    private ArrayList<Token> characters = new ArrayList<Token>();
    private ArrayList<Token> strings = new ArrayList<Token>();

    public SymbolTable() {

    }

    public void fillSymTable() {

        HashMap<String, Token> temp = new HashMap<>();

        temp.put("include", new Token("include", 1, 0, 0, 0));
        reserveds.add(temp.get("include"));
        temp.put("using", new Token("using", 1, 1, 0, 0));
        reserveds.add(temp.get("using"));
        temp.put("std", new Token("std", 1, 2, 0, 0));
        reserveds.add(temp.get("std"));
        temp.put("const", new Token("const", 1, 3, 0, 0));
        reserveds.add(temp.get("const"));
        temp.put("cout", new Token("cout", 1, 4, 0, 0));
        reserveds.add(temp.get("cout"));
        temp.put("cin", new Token("cin", 1, 5, 0, 0));
        reserveds.add(temp.get("cin"));
        temp.put("main", new Token("main", 1, 6, 0, 0));
        reserveds.add(temp.get("main"));
        temp.put("while", new Token("while", 1, 7, 0, 0));
        reserveds.add(temp.get("while"));
        temp.put("int", new Token("int", 1, 8, 0, 0));
        reserveds.add(temp.get("int"));
        temp.put("else", new Token("else", 1, 9, 0, 0));
        reserveds.add(temp.get("else"));
        temp.put("namespace", new Token("namespace", 1, 10, 0, 0));
        reserveds.add(temp.get("namespace"));
        temp.put("char", new Token("char", 1, 11, 0, 0));
        reserveds.add(temp.get("char"));
        temp.put("if", new Token("if", 1, 12, 0, 0));
        reserveds.add(temp.get("if"));
        temp.put("return", new Token("return", 1, 13, 0, 0));
        reserveds.add(temp.get("return"));
        temp.put("iostream", new Token("iostream", 1, 14, 0, 0));
        reserveds.add(temp.get("iostream"));
        
        st.put(1, temp);
      
       // for (Token t : reserveds) {// updates values of reserved
           
            //System.out.println(t.getLexeme()+": "+t.getValue()); // check values once added
           
       // }
        
       
        temp = new HashMap<>();
        temp.put("++", new Token("++", 2, 0, 0, 0));
        operators.add(temp.get("++"));
        temp.put("--", new Token("--", 2, 1, 0, 0));
        operators.add(temp.get("--"));
        temp.put(">>", new Token(">>", 2, 2, 0, 0));
        operators.add(temp.get(">>"));
        temp.put("<<", new Token("<<", 2, 3, 0, 0));
        operators.add(temp.get("<<"));
        temp.put("==", new Token("==", 2, 4, 0, 0));
        operators.add(temp.get("=="));
        temp.put("&&", new Token("&&", 2, 5, 0, 0));
        operators.add(temp.get("&&"));
        temp.put("||", new Token("||", 2, 6, 0, 0));
        operators.add(temp.get("||"));
        temp.put("<=", new Token("<=", 2, 7, 0, 0));
        operators.add(temp.get("<="));
        temp.put("!", new Token("!", 2, 8, 0, 0));
        operators.add(temp.get("!"));
        temp.put("*", new Token("*", 2, 9, 0, 0));
        operators.add(temp.get("*"));
        temp.put("+", new Token("+", 2, 10, 0, 0));
        operators.add(temp.get("+"));
        temp.put("-", new Token("-", 2, 11, 0, 0));
        operators.add(temp.get("-"));
        temp.put("/", new Token("/", 2, 12, 0, 0));
        operators.add(temp.get("/"));
        temp.put("<", new Token("<", 2, 13, 0, 0));
        operators.add(temp.get("<"));
        temp.put("!=", new Token("!=", 2, 14, 0, 0));
        operators.add(temp.get("!="));
        temp.put("=", new Token("=", 2, 15, 0, 0));
        operators.add(temp.get("="));
        temp.put(">", new Token(">", 2, 16, 0, 0));
        operators.add(temp.get(">"));
        temp.put(">=", new Token(">=", 2, 17, 0, 0));
        operators.add(temp.get(">="));
        temp.put("%", new Token("%",2,18,0,0));
        operators.add(temp.get("%"));

        st.put(2, temp);
        
        /*
        for (Token t : operators) {// updates operator values
           //System.out.println(t.getLexeme()+": "+t.getValue()); // check values once added
        }
        */
        

        temp = new HashMap<>();

        temp.put(";", new Token(";", 3, 0, 0, 0));
        temp.put(":", new Token(":", 3, 0, 0, 0));
        temp.put("(", new Token("(", 3, 0, 0, 0));
        temp.put(")", new Token(")", 3, 0, 0, 0));
        temp.put("[", new Token("[", 3, 0, 0, 0));
        temp.put("]", new Token("]", 3, 0, 0, 0));
        temp.put(",", new Token(",", 3, 0, 0, 0));
        temp.put(".", new Token(".", 3, 0, 0, 0));
        temp.put("{", new Token("{", 3, 0, 0, 0));
        temp.put("}", new Token("}", 3, 0, 0, 0));
        temp.put("#", new Token("#", 3, 0, 0, 0));

        temp.forEach((k, v) -> {// fills punctuation lookup table
            punctuations.add(v);
        });

        for (Token t : punctuations) {// adds and updates value field

            t.setValue(punctuations.indexOf(t));
            //System.out.println(t.getLexeme()+": "+t.getValue()); // check values once added
            temp.put(t.getLexeme(), t);
        }

        st.put(3, temp);
        temp = new HashMap<>();
        st.put(4, temp);
        temp = new HashMap<>();
        st.put(5, temp);
        temp = new HashMap<>();
        st.put(6, temp);
        temp = new HashMap<>();
        st.put(7, temp);

        listmap.put(1, reserveds);
        listmap.put(2, operators);
        listmap.put(3, punctuations);
        listmap.put(4, identifiers);
        listmap.put(5, integers);
        listmap.put(6, characters);
        listmap.put(7, strings);
    }

    public void addToSymbolTable(Token t) {
        int type = t.getTokenType();
        ArrayList<Token> tl = getListMap().get(type);
        
        if (!st.get(type).containsKey(t.getLexeme())) {// if it is already existing no need to add
            /*
             * might change int value to be int unsure yet based on data structure set up if
             * (type.equals("integer")) {
             * 
             * }
             */
            tl.add(t);
            t.setValue(tl.indexOf(t));
           
            st.get(type).put(t.getLexeme(), t);

        }

    }

    public void printSymbolTable() {
        System.out.format("Symbol Table\n%-15s %-5s %-15s %-15s %-7s \n", "Lexeme", "Value", "Type", "Size", "ElType");
        listmap.forEach((k, v) -> {
            v.forEach((t) -> {
                System.out.format("%-15s %-5d %-15s %-15d %-7d \n", t.getLexeme(), t.getValue(), t.getTokenType(),
                        t.getSize(), t.getElType());
            });
        });
    }

    public void printList(){
        for(int i = 1;i<8;i++){
            for(Token a : listmap.get(i)){
                System.out.println("Lex: "+a.getLexeme()+" || type: "+a.getTokenType()+" || value: "+a.getValue());
            }
            
        }
    }

    public HashMap<Integer, ArrayList<Token>> getListMap() {
        return listmap;
    }

    public void setListMap(HashMap<Integer, ArrayList<Token>> h) {
        listmap = h;
    }

    public HashMap<Integer, HashMap<String, Token>> getSymbolTable() {
        return st;
    }

    public void setSymbolTable(HashMap<Integer, HashMap<String, Token>> h) {
        st = h;
    }

    public void setOperatorsList(ArrayList<Token> t) {
        operators = t;
    }

    public ArrayList<Token> getOperatorsList() {
        return operators;
    }

    public void setReservedsList(ArrayList<Token> t) {
        reserveds = t;
    }

    public ArrayList<Token> getReservedsList() {
        return reserveds;
    }

    public void setPunctuationsList(ArrayList<Token> t) {
        punctuations = t;
    }

    public ArrayList<Token> getPunctuationsList() {
        return punctuations;
    }

    public void setIdentifiersList(ArrayList<Token> t) {
        identifiers = t;
    }

    public ArrayList<Token> getIdentifiersList() {
        return identifiers;
    }

    public void setIntegersList(ArrayList<Token> t) {
        integers = t;
    }

    public ArrayList<Token> getIntegersList() {
        return integers;
    }

    public void setCharactersList(ArrayList<Token> t) {
        characters = t;
    }

    public ArrayList<Token> getCharactersList() {
        return characters;
    }

    public void setStringsList(ArrayList<Token> t) {
        strings = t;
    }

    public ArrayList<Token> getStringsList() {
        return strings;
    }

}// end class
