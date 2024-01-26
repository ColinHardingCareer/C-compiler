/*
Author colin harding

resources used : System Software an introduction to system programming by leland l. beck

parser at currrent stage checks for declaration issues and errors accrodingly

call parse to parse the lexical output.

array 1
const 2

unassigned int -1
unassigned char -2
unassigned int[] -3
unassigned char[] -4

int 5
char 6

const int 10 
const char 12



*/

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.*;

public class Parser {
    ArrayList<int[]> tokenInputStream;
    ArrayList<int[]> idList = new ArrayList<>();
    ListIterator<int[]> it;
    int[] cToken;
    boolean dF = false;
    boolean cF = false;
    private SymbolTable symbolTable;
    Stack<ParseTree> parentStack;
    Stack<ParseTree> treeStack;
    ParseTree parseTree;
    int tc = 0;
    // int count = 0;

    public Parser() {// default constructor

    }

    public Parser(ArrayList<int[]> t, SymbolTable s) {// builder constructor
        tokenInputStream = t;
        symbolTable = s;
        it = tokenInputStream.listIterator();
        cToken = it.next();
        treeStack = new Stack<ParseTree>();
        parentStack = new Stack<ParseTree>();
        parseTree = new ParseTree();
    }

    public void parse() {// the method to make the parser work

        // check for include
        boolean iF = readInclude();
        if (iF) {
            System.out.println("iostream included");
        } else {
            System.out.println("iostream not found");
            return;
        }
        // check for using
        boolean uF = readUsing();
        if (uF) {
            System.out.println("namespace included");
        } else {
            System.out.println("namespace not found");
            return;
        }

        if (dec_list()) {
            System.out.println("declarations parsed\n");
        } else {
            System.out.println("declarations error\n");
        }

        // System.out.println(count);

    }// end parse method

    ListIterator<int[]> getIterator() {
        return it;
    }

    public boolean readUsing() {
        boolean found = false;
        if (cToken[0] == 1 && cToken[1] == 1) {// if using
            cToken = (int[]) it.next();
            if (cToken[0] == 1 && cToken[1] == 10) {// if namespace
                cToken = (int[]) it.next();
                if (cToken[0] == 1 && cToken[1] == 2) {// if std
                    cToken = (int[]) it.next();
                    if (cToken[0] == 3 && cToken[1] == 4) {// if ;
                        cToken = (int[]) it.next();

                        found = true;

                    }
                }

            }
        }
        return found;
    }

    public boolean readInclude() {
        boolean found = false;
        if (cToken[0] == 3 && cToken[1] == 0) {// if #
            cToken = (int[]) it.next();
            if (cToken[0] == 1 && cToken[1] == 0) {// if include
                cToken = (int[]) it.next();
                if (cToken[0] == 2 && cToken[1] == 13) {// if <
                    cToken = (int[]) it.next();
                    if (cToken[0] == 1 && cToken[1] == 14) {// if iostream
                        cToken = (int[]) it.next();
                        if (cToken[0] == 2 && cToken[1] == 16) {// if >
                            cToken = (int[]) it.next();
                            found = true;

                        }

                    }
                }

            }
        }
        return found;
    }

    public void printIdList() {

        for (int[] x : idList) {
            System.out.println(x[0] + " " + x[1] + " " + x[4]);
        }

    }

    public boolean factor() {

        boolean found = false;

        if (cToken[0] == 4 || cToken[0] == 5) {
            found = true;
            cToken = (int[]) it.next();
        } else {
            if (cToken[0] == 3 && cToken[1] == 1) {
                cToken = (int[]) it.next();
                if (exp()) {
                    if (cToken[0] == 3 && cToken[1] == 2) {
                        found = true;
                        cToken = (int[]) it.next();
                    }
                }
            }
        }

        return found;

    }

    public boolean term() {

        boolean found = false;

        if (factor()) {
            found = true;

            while ((cToken[0] == 2 && (cToken[1] == 9 || cToken[1] == 12)) && found) {
                cToken = (int[]) it.next();
                if (!factor()) {
                    System.out.println("int math * / error");
                    found = false;

                }

            } // end while

        }

        return found;

    }

    public boolean exp() {
        boolean found = false;

        if (term()) {
            found = true;

            while ((cToken[0] == 2 && (cToken[1] == 10 || cToken[1] == 11)) && found) {
                cToken = (int[]) it.next();
                if (!term()) {
                    found = false;
                    System.out.println("int math + - error");
                }
            }

        }

        return found;
    }

    public boolean assign() {

        boolean found = false;
        int[] id = cToken;
        //Token aToke;
        if (cToken[0] == 4) {// checks dup ids

            if (dF) {// if this is a declaration and not a stand alone assignment
                for (int[] x : idList) {// check that if hasnt been declared before
                    if (x[0] == id[0] && x[1] == id[1]) {
                        System.out.println("duplicate id");
                        return found = false;
                    }
                }

            }

            if (cF) {// because it checks if const is assigned later just assign the eltype to const
                     // and let it error later
                symbolTable.getListMap().get(4).get(cToken[1]).setElType(2);
                //System.out.println(symbolTable.getListMap().get(4).get(cToken[1]).getElType() + " "
                       // + symbolTable.getListMap().get(4).get(cToken[1]).getLexeme());

            }
            //aToke = symbolTable.getListMap().get(cToken[0]).get(cToken[1]);
            System.out.println("Atoke: "+symbolTable.getListMap().get(cToken[0]).get(cToken[1]).getLexeme()+" "+symbolTable.getListMap().get(cToken[0]).get(cToken[1]).getValue());
            cToken = (int[]) it.next();// eat the id to move on

            // CHECKS FOR ARRAYS
            if ((cToken[0] == 3 && cToken[1] == 5)) {// if [
                cToken = (int[]) it.next();
                if (cToken[0] == 5) {// is int
                    // create int size
                    int size = Integer.parseInt(symbolTable.getListMap().get(5).get(cToken[1]).getLexeme());
                    symbolTable.getListMap().get(4).get(id[1]).setSize(size);
                    // System.out.println(symbolTable.getListMap().get(4).get(id[1]).getSize());
                    // set it equal to name of token cast as int
                    cToken = (int[]) it.next();
                    if ((cToken[0] == 3 && cToken[1] == 8)) {// is ]
                        found = true;
                        // set eltype to array
                        
                        if(tc==-1){
                            symbolTable.getListMap().get(id[0]).get(id[1]).setElType(-3);
                        }else{
                            symbolTable.getListMap().get(id[0]).get(id[1]).setElType(-4);
                        }
                        // System.out.println(symbolTable.getListMap().get(4).get(id[1]).getElType());

                        cToken = (int[]) it.next();
                    } else {
                        System.out.println("Array init error");
                        return found = false;
                    }
                } else {
                    System.out.println("Array init error");
                    return found = false;
                }
            }

            else if (cToken[0] == 2 && cToken[1] == 15) {// this checks for = after an id
                cToken = (int[]) it.next();
                

                if (cToken[0] == 4) {
                    int i = 0;
                    if (idList.size() == 0) {
                        System.out.println("assigned id not init");
                        return found = false;
                    } else {
                        for (int[] x : idList) {// check that if hasnt been declared before
                            boolean nF = false;
                            if (!(x[0] == id[0] && x[1] == id[1])) {

                                nF = true;
                                if (i >= idList.size() - 1 && nF) {
                                    System.out.println("assigned id not init");
                                    return found = false;
                                }

                            }
                        }
                    }
                }
                System.out.println("= "+symbolTable.getListMap().get(cToken[0]).get(cToken[1]).getLexeme());
                if(cToken[0] == 4){
                    //.get(cToken[0]).get(.getLexeme()).getValue()

                    // = value          symbolTable.getListMap().get(cToken[0]).get(cToken[1]).getValue()
                    // value =          symbolTable.getListMap().get(id[0]).get(id[1]).setValue()
                    // value = value 

                    if(symbolTable.getListMap().get(id[0]).get(id[1]).getElType() == 2){
                       if( (symbolTable.getListMap().get(cToken[0]).get(cToken[1]).getElType()==5 ||symbolTable.getListMap().get(cToken[0]).get(cToken[1]).getElType()==10) || tc==-1){
                        symbolTable.getListMap().get(id[0]).get(id[1]).setElType(10);
                    }else{
                        symbolTable.getListMap().get(id[0]).get(id[1]).setElType(12);
                    }
                        
                    }else{
                        if( (symbolTable.getListMap().get(cToken[0]).get(cToken[1]).getElType()==5 ||symbolTable.getListMap().get(cToken[0]).get(cToken[1]).getElType()==10) || tc==-1){
                            symbolTable.getListMap().get(id[0]).get(id[1]).setElType(5);
                        }else{
                            symbolTable.getListMap().get(id[0]).get(id[1]).setElType(6);
                        }
                    
                    }
                    
                    //symbolTable.getListMap().get(id[0]).get(id[1]).setElType(symbolTable.getListMap().get(cToken[0]).get(cToken[1]).getElType());
                    symbolTable.getListMap().get(id[0]).get(id[1]).setValue(symbolTable.getListMap().get(cToken[0]).get(cToken[1]).getValue());
                    
                    //symbolTable.getListMap().get(id[0]).get(id[1]).setElType(4);
                    System.out.println("Atoke changed: "+symbolTable.getListMap().get(id[0]).get(id[1]).getValue());

                }
                if(cToken[0] == 5){
                    symbolTable.getListMap().get(id[0]).get(id[1]).setValue(Integer.parseInt(symbolTable.getListMap().get(cToken[0]).get(cToken[1]).getLexeme()));
                   
                    if(symbolTable.getListMap().get(id[0]).get(id[1]).getElType() == 2){
                        symbolTable.getListMap().get(id[0]).get(id[1]).setElType(10);

                    }else{
                        symbolTable.getListMap().get(id[0]).get(id[1]).setElType(5);
                    }
                    
                    System.out.println("Atoke changed: "+symbolTable.getListMap().get(id[0]).get(id[1]).getValue());
                }
                
                if (exp()) {

                    cF = false;
                    found = true;
                    idList.add(new int[] { id[0], id[1] });

                    //System.out.println("p:: "+idList.get(idList.size()-1)[0]+" , "+idList.get(idList.size()-1)[1]);
                    
                } else if (cToken[0] == 6) {
                    cF = false;
                    found = true;
                    idList.add(new int[] { id[0], id[1] });// read next
                    symbolTable.getListMap().get(id[0]).get(id[1]).setValue((symbolTable.getListMap().get(cToken[0]).get(cToken[1]).getLexeme().charAt(0)));
                    if(symbolTable.getListMap().get(id[0]).get(id[1]).getElType() == 2){
                        symbolTable.getListMap().get(id[0]).get(id[1]).setElType(12);

                    }else{
                        symbolTable.getListMap().get(id[0]).get(id[1]).setElType(6);
                    }
                    System.out.println("Atoke changed: "+symbolTable.getListMap().get(id[0]).get(id[1]).getValue());
                    cToken = (int[]) it.next();
                }

            } else if ((cToken[0] == 3 && cToken[1] == 7)) {// if ,
                found = true;
                if(tc==-1){
                    symbolTable.getListMap().get(id[0]).get(id[1]).setElType(-1);
                }else{
                    symbolTable.getListMap().get(id[0]).get(id[1]).setElType(-2);
                }
                idList.add(new int[] { id[0], id[1] });
            } else if ((cToken[0] == 3 && cToken[1] == 4)) {// if ;
                found = true;
                if(tc==-1){
                    symbolTable.getListMap().get(id[0]).get(id[1]).setElType(-1);
                }else{
                    symbolTable.getListMap().get(id[0]).get(id[1]).setElType(-2);
                }
                
                idList.add(new int[] { id[0], id[1] });
                // now the list is done
            } else if (!(cToken[0] == 3 && cToken[1] == 7)) {
                System.out.println("no , assignment");
                found = false;
            }
        }

        if (cF) {
            System.out.println("const not init at id: " + symbolTable.getListMap().get(4).get(id[1]).getLexeme());
            found = false;
        }
        return found;
    }

    public boolean a_list() {
        boolean found = false;
        boolean hF = false;
        if (cF) {
            hF = true;
        }

        if (assign()) {
            found = true;
            if (!(cToken[0] == 3 && cToken[1] == 7) && !(cToken[0] == 3 && cToken[1] == 4)) {
                System.out.println("no , assignment");
            }
            while ((cToken[0] == 3 && cToken[1] == 7) && found) {
                if (hF) {
                    cF = true;
                }
                cToken = (int[]) it.next();
                if (!assign()) {
                    found = false;
                    System.out.println("assignment error");
                }

            }

        }

        return found;
    }
    
    public boolean type_id() {
        boolean found = false;

        if ((cToken[0] == 1 && cToken[1] == 8) || ((cToken[0] == 1 && cToken[1] == 11))) {
            if((cToken[0] == 1 && cToken[1] == 8)){
                tc=-1;
            }else{
                tc=-2;
            }
            found = true;
            cToken = (int[]) it.next();

        } else {
            System.out.println("invalid type declaration, must be int or char");
        }

        return found;
    }

    public boolean type() {
        boolean found = false;

        if ((cToken[0] == 1 && cToken[1] == 3)) {
            cToken = (int[]) it.next();
            cF = true;
        }
        if (type_id()) {
            found = true;
        }

        return found;
    }

    public boolean dec() {
        boolean found = false;
        dF = false;
        if (type()) {
            dF = true;
            if (a_list()) {
                found = true;
            }

        }
        dF = false;
        return found;
    }

    public boolean dec_list() {// returns whether a declist exists or not

        boolean found = false;

        if (dec()) {// to be a dec list atleast 1 dec must exist
            // System.out.println();

            found = true;
            while ((cToken[0] == 3 && cToken[1] == 4) && found) {// loop until the end of a declaration list
                // count++;
                cToken = (int[]) it.next();
                if (dec()) {// keep looping else is declist done or error
                    // cToken = (int[]) it.next();

                } else {// is declist done or error

                    if (_main()) {// if the dec_list is done and main follows else delcist error thrown
                        // System.out.println("main");
                        return found;
                    } else {// throw declist error
                        found = false;
                        System.out.println("Error: Declaration list");
                    } // end if(_main())
                } // end if(dec())
            } // end while ; && found
        } // end first if(dec())
        if ((!(cToken[0] == 3 && cToken[1] == 4))) {
            System.out.println("no ; for dec list");
            found = false;
        }
        return found;
    }// end dec_list()

    public boolean _main() {

        boolean found = false;

        if (cToken[0] == 1 && cToken[1] == 6) {// if cToken is main reserved
            cToken = (int[]) it.next();
            if (cToken[0] == 3 && cToken[1] == 1) {// if cToken is (
                cToken = (int[]) it.next();
                if (cToken[0] == 3 && cToken[1] == 2) {// if cToken is )
                    cToken = (int[]) it.next();
                    return true;
                    /*
                    if (cToken[0] == 3 && cToken[1] == 6) {// if cToken is {
                        while (it.hasNext()) {// read and ignore body of program just find the end
                            cToken = (int[]) it.next();
                            if (cToken[0] == 3 && cToken[1] == 9) {// if cToken is }
                                return found = true;
                            }
                        }
                    }
                    */
                }
            }
        }

        return found;
    }// end _main()

}
