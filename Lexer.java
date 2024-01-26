/*
author: colin harding

Lexer is the lexical analizer 

scans the source code and creates tokens adding them to the symbol table

//Lexer prints the found tokens to terminal (does not currently do that)
lexer now sends value and type to an output "stream"

Lexer has method scanCode() that does the bulk of lexical analysis//


*/

import java.io.*;
import java.text.*;
import java.util.ArrayList;

public class Lexer {
    private SymbolTable symTableObj;
    private ArrayList<int[]> lexerOutput = new ArrayList<>();

    public Lexer() {

    }

    public Lexer(SymbolTable s) {
        symTableObj = s;
    }

    public void scanCode(File source) throws IOException {

        FileReader fr = new FileReader(source);
        BufferedReader br = new BufferedReader(fr);
        CharacterIterator it;

        String line;
        StringBuilder str;

        SymbolTable st = symTableObj;
        //System.out.format("%-15s %-5s %-5s \n", "TYPE", "VALUE", "LEXEME");
        //System.out.println("===============================================");
        while (br.ready()) {// read in new line loop
            line = br.readLine();
            it = new StringCharacterIterator(line);
            str = new StringBuilder();

            while (it.current() != CharacterIterator.DONE) {// read each line by char loop

                char c = it.current();

                str.delete(0, str.length());

                if (c == '/') {// comment checker
                    str.append(c);
                    c = it.next();

                    if ("//".equals(str.toString() + c)) {// eat line for single comment
                        line = br.readLine();
                        it = new StringCharacterIterator(line);
                    } else if ("/*".equals(str.toString() + c)) {// starts reading multi comment
                        str.delete(0, str.length());

                        c = it.next();

                        while ((it.current() != CharacterIterator.DONE && !("*/".equals(str.toString())))) {// reads
                                                                                                            // until
                                                                                                            // multi
                                                                                                            // comment
                                                                                                            // is done

                            str.delete(0, str.length());
                            if (c == '*') {// "*/"" check
                                str.append(c);
                                c = it.next();
                                str.append(c);
                            }
                            c = it.next();

                            if (it.current() == CharacterIterator.DONE) {// get new line to read
                                br.readLine();
                                line = br.readLine();

                                if (line == null) {// eof checker for error or get new line
                                    if (!"*/".equals(str.toString())) {// non terminating comment error
                                        System.out.println("non-terminating comment error");
                                        br.close();
                                        return;
                                    }
                                } else {

                                    it = new StringCharacterIterator(line);

                                    c = it.current();
                                } // end if eof or new line

                            } // end get new line to read

                        } // end while

                    } else {// end reading multi comment || start printing division operator

                        int type = st.getSymbolTable().get(2).get(str.toString()).getTokenType();
                        int value = st.getSymbolTable().get(2).get(str.toString()).getValue();

                        //System.out.format("%-15s %-5d  \n", type, value);
                        lexerOutput.add(new int[] { type, value });
                        str.delete(0, str.length());
                    } // end multi || print

                } else if (c == '&' || c == '|') {// end comment checker /// start && and || checker
                    str.append(c);
                    c = it.next();
                    if ("&&".equals(str.toString() + c) || "||".equals(str.toString() + c)) {// checks for double char
                        str.append(c);
                        int type = st.getSymbolTable().get(2).get(str.toString()).getTokenType();
                        int value = st.getSymbolTable().get(2).get(str.toString()).getValue();
                        // System.out.format("%-15s %-5d %-5s \n", type, value, lexeme);
                       // System.out.format("%-15s %-5d  \n", type, value);
                        lexerOutput.add(new int[] { type, value });

                    }

                } else if (st.getSymbolTable().get(2).containsKey(c + "")// end && and || checker
                        || st.getSymbolTable().get(3).containsKey(c + "")) {// operator and punctuation
                                                                            // checker

                    str.append(c + "");

                    c = it.next();

                    if (c == '$' || c == '@') {// illegal char check
                        System.out.println("Illegal char: " + c + " error");
                        br.close();
                        return;
                    }

                    if (st.getSymbolTable().get(2).containsKey(str.toString() + c)) {// read next char if
                                                                                     // double operator

                        str.append(c + "");

                        it.next();

                    }

                    if (st.getSymbolTable().get(2).containsKey(str.toString())) {// print operator
                        int type = st.getSymbolTable().get(2).get(str.toString()).getTokenType();
                        int value = st.getSymbolTable().get(2).get(str.toString()).getValue();
                        // System.out.format("%-15s %-5d %-5s \n", type, value, lexeme);
                        lexerOutput.add(new int[] { type, value });
                        //System.out.format("%-15s %-5d  \n", type, value);
                    }
                    if (st.getSymbolTable().get(3).containsKey(str.toString())) {// print punctuation

                        int type = st.getSymbolTable().get(3).get(str.toString()).getTokenType();
                        int value = st.getSymbolTable().get(3).get(str.toString()).getValue();
                        // System.out.format("%-15s %-5d %-5s \n", type, value, lexeme);
                        lexerOutput.add(new int[] { type, value });
                        //System.out.format("%-15s %-5d  \n", type, value);
                    }

                } else if (Character.isDigit(c)) {// end operator and punctuation checker || start int checker

                    while (Character.isDigit(c)) {// read digits
                        if (c == '$' || c == '@') {// illegal char checker
                            System.out.println("Illegal char: " + c + " error");// check how this works
                            br.close();
                            return;
                        }
                        if (Character.isLetter(c)) {// illegal int checker
                            System.out.println("Incorrect value " + str.toString() + c + " is not a correct int");
                            br.close();
                            return;
                        }
                        str.append(c);
                        c = it.next();
                    }

                    if (st.getSymbolTable().get(5).containsKey(str.toString())) {// print int
                        Token temp = st.getSymbolTable().get(5).get(str.toString());
                        int type = temp.getTokenType();
                        int value = temp.getValue();
                        // System.out.format("%-15s %-5d %-5s \n", type, value, lexeme);
                        lexerOutput.add(new int[] { type, value });
                        //System.out.format("%-15s %-5d  \n", type, value);

                    } else {

                        Token temp = new Token(str.toString(), 5, Integer.parseInt(str.toString()), 0, 0);
                        st.addToSymbolTable(temp);
                        // tester vvvvv
                        // temp = st.getSymbolTable().get(5).get(str.toString());
                        int type = temp.getTokenType();
                        int value = temp.getValue();
                        // System.out.format("%-15s %-5d %-5s \n", type, value, lexeme);
                        lexerOutput.add(new int[] { type, value });
                        //System.out.format("%-15s %-5d  \n", type, value);
                    }
                    str.delete(0, str.length());

                } else if (Character.isLetter(c) || c == '_') { // end int checker || identifier checker

                    while (Character.isLetter(c) || c == '_' || Character.isDigit(c)) {// read valid chars
                        str.append(c);
                        c = it.next();
                        if (c == '$' || c == '@') {// illegal char checker
                            System.out.println("Illegal char: " + c + " error");
                            br.close();
                            return;
                        }

                    }

                    if (st.getSymbolTable().get(1).containsKey(str.toString())) {// print reserved
                        Token temp = st.getSymbolTable().get(1).get(str.toString());
                        int type = temp.getTokenType();
                        int value = temp.getValue();

                        // System.out.format("%-15s %-5d %-5s \n", type, value, lexeme);
                        lexerOutput.add(new int[] { type, value });
                        //System.out.format("%-15s %-5d  \n", type, value);
                    } else {
                        if (st.getSymbolTable().get(4).containsKey(str.toString())) {// print identifier
                            Token temp = st.getSymbolTable().get(4).get(str.toString());
                            int type = temp.getTokenType();
                            int value = temp.getValue();

                            // System.out.format("%-15s %-5d %-5s \n", type, value, lexeme);
                            lexerOutput.add(new int[] { type, value });
                            //System.out.format("%-15s %-5d  \n", type, value);
                        } else {
                            Token temp = new Token(str.toString(), 4, 0, 0, 0);
                            st.addToSymbolTable(temp);
                            // tester vvvvv
                            // temp = st.getSymbolTable().get(4).get(str.toString());
                            int type = temp.getTokenType();
                            int value = temp.getValue();

                            // System.out.format("%-15s %-5d %-5s \n", type, value, lexeme);
                            lexerOutput.add(new int[] { type, value });
                            //System.out.format("%-15s %-5d  \n", type, value);
                        }
                    }

                    str.delete(0, str.length());

                } else if (c == '\'') {// end identifier checker || start char checker
                    c = it.next();
                    while (c != '\'') {// read character
                        str.append(c);
                        c = it.next();
                        if (it.current() == CharacterIterator.DONE) {// non terminating char checker
                            System.out.println("non-terminating char error");
                            br.close();
                            return;
                        }
                    }

                    c = it.next();

                    Token temp = new Token(str.toString(), 6, 0, 0, 0);
                    if (!st.getSymbolTable().get(6).containsKey(str.toString())) {
                        st.addToSymbolTable(temp);
                    // tester vvvvv
                    // temp = st.getSymbolTable().get(6).get(str.toString());
                   
                    int type = temp.getTokenType();
                    int value = temp.getValue();
                    // System.out.format("%-15s %-5d %-5s \n", type, value, lexeme);
                    lexerOutput.add(new int[] { type, value });
                    //System.out.format("%-15s %-5d  \n", type, value);

                    }else{
                        temp = st.getSymbolTable().get(6).get(str.toString());

                        int type = temp.getTokenType();
                        int value = temp.getValue();
                        // System.out.format("%-15s %-5d %-5s \n", type, value, lexeme);
                        lexerOutput.add(new int[] { type, value });
                    }
                    

                    str.delete(0, str.length());

                } else if (c == '"') {// end char checker || start string checker
                    c = it.next();
                    while (c != '"') {// read string
                        str.append(c);
                        c = it.next();

                        if (it.current() == CharacterIterator.DONE) {// non-terminating string checker
                            System.out.println("non-terminating string error");
                            br.close();
                            return;
                        }
                    }

                    c = it.next();
                    Token temp = new Token(str.toString(), 7, 0, 0, 0);


                    if (!st.getSymbolTable().get(7).containsKey(str.toString())) {
                        st.addToSymbolTable(temp);
                    // tester vvvvv
                    // temp = st.getSymbolTable().get(6).get(str.toString());
                   
                    int type = temp.getTokenType();
                    int value = temp.getValue();
                    // System.out.format("%-15s %-5d %-5s \n", type, value, lexeme);
                    lexerOutput.add(new int[] { type, value });
                    //System.out.format("%-15s %-5d  \n", type, value);

                    }else{
                        temp = st.getSymbolTable().get(7).get(str.toString());

                        int type = temp.getTokenType();
                        int value = temp.getValue();
                        // System.out.format("%-15s %-5d %-5s \n", type, value, lexeme);
                        lexerOutput.add(new int[] { type, value });
                    }


                  

                    str.delete(0, str.length());

                } else {// end string checker || start eater

                    it.next();
                }

            } // end read each line by char loop

        } // end read in new line loop
        br.close();

    }// end scan code

    public ArrayList<int[]> getLexerOutput() {

        return lexerOutput;

    }

}// end lexer

/*
 * GOAL SCAN 1 CHAR AT A TIME BUILD STRINGS IDENTIFY TOKENS INSERT TOKENS INTO
 * SYMBOL TABLE FIND LEXICAL ERRORS
 * 
 */
