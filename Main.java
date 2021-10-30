

import java.io.*;
import java.util.ArrayList;

public class Main {
    public static String s;
    public static ArrayList<Word> words = new ArrayList<>();
    public static ArrayList<Word> syms = new ArrayList<>();
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
        int num = 1;
        for(int i = 0; i < words.size(); i++){
            while(words.get(i).getType()==10||words.get(i).getType()==11){
                if(words.get(i).getType()==10){
                    num *= -1;
                }
                else{
                    num *= 1;
                }
                i++;
            }
            if(words.get(i).getType()==4){
                num = num * Integer.valueOf(words.get(i).getOutPut());
                sout.append(String.valueOf(num));
            }
           else{
//                if(words.get(i).getType()!=5&&words.get(i).getType()!=6){
                    sout.append(words.get(i).getOutPut());
//                }
           }
        }
        String out = sout.toString();
//        System.out.println(out);
       OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(args[1]));
//        OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream("E:\\JavaFile\\compile\\src\\lab2\\out.txt"));
        fout.write(out);
        fout.close();
    }
}
