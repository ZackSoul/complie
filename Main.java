

import java.io.*;
import java.util.ArrayList;
import java.util.Stack;

public class Main {
    public static String s;
    public static ArrayList<Word> words = new ArrayList<>();
    public static ArrayList<Word> syms = new ArrayList<>();
    public static Stack<Register> stack = new Stack<>();
    public static ArrayList<String> functionList = new ArrayList<>();
    public static int regIndex = 0;
    public static StringBuilder out = new StringBuilder();
    public static void main(String[] args) throws IOException {
//        InputStreamReader fpin = new InputStreamReader(new FileInputStream(args[0]));
        InputStreamReader fpin = new InputStreamReader(new FileInputStream("E:\\JavaFile\\compile\\src\\lab3\\in.txt"));
        StringBuilder str = new StringBuilder();
        int flag;
        while((flag = fpin.read()) != -1){
            str.append((char) flag);
        }
        fpin.close();
        s = str.toString();
        Lexer.lexerAnalyse(s);
        out.append("define dso_local i32 @main(){\n");
        if(Parse.parseAnalyse()){
            out.append("\tret i32 ");
            out.append("%" + stack.peek().getId());
            out.append("\n}");
            String sout = out.toString();
            OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream("E:\\JavaFile\\compile\\src\\lab3\\out.txt"));
//            OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(args[1]));
            fout.write(sout);
            fout.close();
//            System.out.println(out);
        }
        else{
            System.exit(1);
        }
    }
}
