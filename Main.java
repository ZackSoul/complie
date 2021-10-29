

import java.io.*;
import java.util.ArrayList;

public class Main {
    public static String s;
    public static ArrayList<Word> words = new ArrayList<>();
    public static ArrayList<Word> syms = new ArrayList<>();
    public static void main(String[] args) throws IOException {
//        InputStreamReader fpin = new InputStreamReader(new FileInputStream(args[0]));
        InputStreamReader fpin = new InputStreamReader(new FileInputStream("E:\\JavaFile\\compile\\src\\lab1\\in.txt"));
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
        for(int i = 0; i < words.size(); i++){
            sout.append(words.get(i).getOutPut());
        }
        String out = sout.toString();
//        System.out.println(out);
        OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(args[1]));
//        OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream("E:\\JavaFile\\compile\\src\\lab1\\out.txt"));
        fout.write(out);
        fout.close();
    }
}
