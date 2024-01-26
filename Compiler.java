
/*
author: colin harding

contains main method

Compiler runs each piece of the compiler

Compiler gets source file

to read file name it "SourceCode.txt"//
*/

import java.io.File;
import java.io.IOException;
import java.util.ListIterator;

public class Compiler {

    public static void main(String[] args) throws IOException {
        File sourceCode = new File("SourceCode.txt");

        SymbolTable symbolTable = new SymbolTable();
        symbolTable.fillSymTable();

        Lexer lex = new Lexer(symbolTable);
        lex.scanCode(sourceCode);

        symbolTable.printList();
        


        Parser parser = new Parser(lex.getLexerOutput(), symbolTable);
        parser.parse();
        //parser.printIdList();
        //System.out.println("\nSymbol Table");
        //symbolTable.printSymbolTable();

        ListIterator<int[]> it = parser.getIterator();
        //this starts at { for main

        IntermediateGenerator ig = new IntermediateGenerator(symbolTable,it);
        ig.generate();

       // System.out.println("=====================\n========================");
       // System.out.println(ig.code);

       Assembler abler = new Assembler(ig.symbolTable, ig.code,ig.tempMax,ig.addrMax);
       abler.generate();


    }// end main

}// end class

/*
 * Why does the computer keep coughing? It has a virus.
 * 
 */