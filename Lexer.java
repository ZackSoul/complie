

import java.math.BigInteger;
import java.util.ArrayList;

public class Lexer {
    public static ArrayList<String> reserve = new ArrayList<>();
    public static char[] filter = {' ','\t','\r','\n'};
    public static StringBuilder token = new StringBuilder();
    public static void lexerAnalyse(String s){
        setReserve();
        char ch;
        for(int i = 0; i < s.length();){
            ch = s.charAt(i++);
            token.setLength(0);
            if(isFilter(ch)){
                token.append(ch);
                Word word = new Word(token.toString());
                Main.words.add(word);
            }
            else if(Character.isLowerCase(ch)){
                while(Character.isLowerCase(ch)||Character.isUpperCase(ch)){
                    token.append(ch);
                    ch = s.charAt(i++);
                }
                i--;
                if(isReserve(token.toString())){
                    Main.words.add(new Word(token.toString()));
                    Main.syms.add(new Word(token.toString()));
                }
                else{
//                    System.out.println("1");
//                    System.out.println(token.toString());
//                    System.exit(1);
                }
            }
            else if(Character.isDigit(ch)){
                if(ch == '0'){
                    token.append(ch);
                    ch = s.charAt(i++);
                    if(ch=='x'||ch=='X'){
                        token.append(ch);
                        ch = s.charAt(i++);
                        if(!isHexadecimal(ch)){
                            System.out.println("2");
                            System.exit(1);
                        }
                        else{
                            while(Character.isDigit(ch)||isHexadecimal(ch)){
                                token.append(ch);
                                ch = s.charAt(i++);
                            }
                            token.delete(0,2);
                            int num = decodeHex(token.toString());
                            Main.syms.add(new Word(String.valueOf(num)));
                            Main.words.add(new Word(String.valueOf(num)));
                            i--;
                        }
                    }
                    else if(isOctal(ch)){
                        while(isOctal(ch)){
                            token.append(ch);
                            ch = s.charAt(i++);
                        }
                        int num = decodeOct(token.toString());
                        Main.syms.add(new Word(String.valueOf(num)));
                        Main.words.add(new Word(String.valueOf(num)));
                        i--;
                    }
                    else{
                        System.out.println("3");
                        System.exit(1);
                    }
                }
                else{
                    while(Character.isDigit(ch)){
                        token.append(ch);
                        ch = s.charAt(i++);
                    }
                    Main.syms.add(new Word(token.toString()));
                    Main.words.add(new Word(token.toString()));
                    i--;
                }
            }
            else if(ch=='/'){
                token.append(ch);
                ch = s.charAt(i++);
                if(ch=='/'){
                    while (ch != '\n') {
                        ch = s.charAt(i++);
                    }
                }
                else if(ch == '*'){
                    ch = s.charAt(i++);
                    while(true){
                        if(ch == '*'){
                            ch = s.charAt(i++);
                            if(ch == '/'){
                                break;
                            }
                            else if(i == s.length()){
                                System.out.println("4");
                                System.exit(1);
                            }
                            ch = s.charAt(i++);
                        }
                        else if(i == s.length()){
                            System.out.println("5");
                            System.exit(1);
                        }
                        else{
                            ch = s.charAt(i++);
                        }
                    }
                }
                else if(ch == '+' || ch == '-' || ch == '(' || ch == ' '){
                    Main.syms.add(new Word(token.toString()));
                    Main.words.add(new Word(token.toString()));
                    i--;
                }
                else{
                    System.out.println("6");
                    System.exit(1);
                }
            }
            else switch (ch){
                case '(':
                {
                    Main.syms.add(new Word("("));
                    Main.words.add(new Word("("));
                    break;
                }
                case ')':
                {
                    Main.syms.add(new Word(")"));
                    Main.words.add(new Word(")"));
                    break;
                }
                case '{':
                {
                    Main.syms.add(new Word("{"));
                    Main.words.add(new Word("{"));
                    break;
                }
                case '}':
                {
                    Main.syms.add(new Word("}"));
                    Main.words.add(new Word("}"));
                    break;
                }
                case ';':
                {
                    Main.syms.add(new Word(";"));
                    Main.words.add(new Word(";"));
                    break;
                }
                case '-':
                {
                    Main.syms.add(new Word("-"));
                    Main.words.add(new Word("-"));
                    break;
                }
                case '+':
                {
                    Main.syms.add(new Word("+"));
                    Main.words.add(new Word("+"));
                    break;
                }
                case '*':
                {
                    Main.syms.add(new Word("*"));
                    Main.words.add(new Word("*"));
                    break;
                }
                case '/':
                {
                    Main.syms.add(new Word("/"));
                    Main.words.add(new Word("/"));
                    break;
                }
                case '%':
                {
                    Main.syms.add(new Word("%"));
                    Main.words.add(new Word("%"));
                    break;
                }
                default:
                {
                    System.out.println("7");
                    System.exit(1);
                }
            }
        }
    }

    public static void setReserve(){
        reserve.add("int");
        reserve.add("main");
        reserve.add("return");
    }

    public static boolean isFilter(char ch){
        for(int i = 0; i < 4; i++){
            if(filter[i] == ch){
                return true;
            }
        }
        return false;
    }

    public static boolean isReserve(String token){
        for(int i = 0; i < reserve.size(); i++){
            if(token.equals(reserve.get(i))){
                return true;
            }
        }
        return false;
    }

    public static boolean isHexadecimal(char ch){
        if(Character.isDigit(ch)||(ch>='a'&&ch<='f')||(ch>='A'&&ch<='F')){
            return true;
        }
        return false;
    }

    public static boolean isOctal(char ch){
        if(ch >= '0' && ch <= '7'){
            return true;
        }
        return false;
    }

    public static int decodeHex(String hex){
        BigInteger bigint = new BigInteger(hex,16);
        int num = bigint.intValue();
        return num;
    }

    public static int decodeOct(String hex){
        BigInteger bigint = new BigInteger(hex,8);
        int num = bigint.intValue();
        return num;
    }
}
