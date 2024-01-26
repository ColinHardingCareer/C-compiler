import java.util.*;

public class Assembler {
    SymbolTable symbolTable;
    Queue<String> code;
    int tempMax;
    int addrMax;

    public Assembler() {

    }

    public Assembler(SymbolTable st, Queue<String> code,int tm,int am) {
        symbolTable = st;
        this.code = code;
        tempMax = tm;
        addrMax = am;
    }

    public ArrayList<String> strConvert(String s) {
        return new ArrayList<String>(Arrays.asList(s.split(" ")));
    }

    public void generate() {
            System.out.println("#include <iostream>\nusing namespace std;");
        for(int c = 0; c<tempMax; c++){//make temps
            System.out.println("int _t_"+c+";");
        }
        System.out.println();
        for(int c = 0; c<addrMax; c++){//make temps
            System.out.println("int* _a_"+c+";");
        }
        System.out.println();
        for(Token t : symbolTable.getIdentifiersList() ){
            
            switch(t.getElType()){
                case -4:
                System.out.println("char "+t.getLexeme()+"["+t.getSize()+"];");
                break;

                case -3:
                    System.out.println("int "+t.getLexeme()+"["+t.getSize()+"];");
                break;

                case -2:
                    System.out.println("char "+t.getLexeme()+";");
                break;

                case-1:
                    System.out.println("int "+t.getLexeme()+";");
                break;

                case 5:
                    System.out.println("int "+t.getLexeme()+" = " + t.getValue()+";");
                break;

                case 6:
                    System.out.println("char "+t.getLexeme()+" = \'" + (char)t.getValue()+"\';");
                break;

                case 10:
                System.out.println("const int "+t.getLexeme()+" = " + t.getValue()+";");
                break;

                case 12:
                System.out.println("const char "+t.getLexeme()+" = \'" + (char)t.getValue()+"\';");
                break;

                default:
                    System.out.println("missing assignment case");
                break;
            }
        }

        System.out.println();

        System.out.println("int main() \n{");
        while (!code.isEmpty()) {

            String line = code.remove();
            ArrayList<String> bits = strConvert(line);

            switch (bits.size()) {
            case 4:
                switch (bits.get(0)) {
                case "[]+":
                    System.out.println(bits.get(3) + " = " + bits.get(1) + " + " + bits.get(2) + ";");
                    break;

                case "+":
                case "-":
                case "*":
                case "/":
                case "%":
                    System.out.println(bits.get(3) + " = " + bits.get(1) + " " + bits.get(0) + " " + bits.get(2) + ";");
                    break;

                case "==":
                case "<":
                case "<=":
                case ">":
                case ">=":
                case "^":
                case "&&":
                case "||":
                    if (bits.get(3).split("")[0].equals("_")) {// if temp
                        System.out.println(
                                bits.get(3) + " = " + bits.get(1) + " " + bits.get(0) + " " + bits.get(2) + ";");
                    } else {// if label
                        System.out.println("if( !(" + bits.get(1) + " " + bits.get(0) + " " + bits.get(2) + ") ) goto "
                                + bits.get(3) + ";");
                    }
                    break;

                default:
                    System.out.println("missing 4 condition");
                    break;
                }

                break;

            case 3:
                switch (bits.get(0)) {
                case "=":
                    System.out.println(bits.get(1) + " " + bits.get(0) + " " + bits.get(2) + ";");
                    break;
                case "=[]":// add pointers
                    System.out.println(bits.get(2) + " = *" + bits.get(1) + ";");
                    break;
                case "[]=":// add pointers
                    System.out.println("*"+bits.get(1) + " = " + bits.get(2) + ";");
                    break;

                default:
                    System.out.println("missing 3 condition");
                    break;
                }

                break;

            case 2:// goto or out or in

                switch (bits.get(0)) {
                case "goto":
                    System.out.println(bits.get(0) + " " + bits.get(1) + ";");
                    break;

                case "cout":
                    System.out.println(bits.get(0) + " << " + bits.get(1) + ";");
                    break;

                case "cin":
                    System.out.println(bits.get(0) + " >> " + bits.get(1) + ";");
                    break;
                
                default:
                    System.out.println("missing 2 condition");
                    break;

                }

                break;

            case 1:// label
                System.out.println(bits.get(0) + ":");
                break;

            default:
                System.out.println("missing condition");
                break;

            }

        } // end while code is not empty
        System.out.println("return 0;\n}");
    }

}
