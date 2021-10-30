

import java.io.*;
import java.util.ArrayList;
import java.util.Stack;

public class Main {
    public static String s;
    public static ArrayList<Word> words = new ArrayList<>();
    public static ArrayList<Word> syms = new ArrayList<>();
    public static Stack<Integer> stack = new Stack<>();
    public static void main(String[] args) throws IOException {
       InputStreamReader fpin = new InputStreamReader(new FileInputStream(args[0]));
//         InputStreamReader fpin = new InputStreamReader(new FileInputStream("E:\\JavaFile\\compile\\src\\lab2\\in.txt"));
        StringBuilder str = new StringBuilder();
        int flag;
        while((flag = fpin.read()) != -1){
            str.append((char) flag);
        }
        fpin.close();
        s = str.toString();
        Lexer.lexerAnalyse(s);
//        for(int i = 0; i < words.size(); i++){
//            System.out.print(words.get(i).getWord());
//        }
        Parse.parseAnalyse();
        StringBuilder sout = new StringBuilder();
        sout.append("define dso_local i32 @main(){\n");
        sout.append("\tret i32 ");
        sout.append(stack.peek());
        sout.append("\n}");
        String out = sout.toString();
//        System.out.println(out);
       OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(args[1]));
//         OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream("E:\\JavaFile\\compile\\src\\lab2\\out.txt"));
        fout.write(out);
        fout.close();
    }
}
