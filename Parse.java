
public class Parse {
    public static int src = 0;
    public static void parseAnalyse(){
        CompUnit();
    }

    public static void match(int x){
        if(Main.syms.get(src++).getType() != x){
//            System.out.println("此时程序希望匹配符号为" + x);
//            System.out.println("此时栈中符号为" + Main.syms.get(src-1).getType());
//            System.out.println("此时栈中符号为" + Main.syms.get(src).getType());
            System.exit(1);
        }
//        else{
//            System.out.println(x);
//        }
    }

    public static void Number(){
        match(4);
        Main.stack.push(Integer.valueOf(Main.syms.get(src-1).getWord()));
    }

    public static void Stmt(){
        match(3);
        Exp();
        match(9);
    }

    public static void Block(){
        match(7);
        Stmt();
        match(8);
    }

    public static void Ident(){
        match(2);
    }

    public static void FuncType(){
        match(1);
    }

    public static void FuncDef(){
        FuncType();
        Ident();
        match(5);
        match(6);
        Block();
    }

    public static void Exp(){
        AddExp();
    }

    public static void AddExp(){
        MulExp();
        AddExp0();
    }

    public static void AddExp0(){
        if(type() == 10){
            MulExp();
            Main.stack.push(Main.stack.pop() - Main.stack.pop());
            AddExp0();
        }
        else if(type() == 11){
            MulExp();
            Main.stack.push(Main.stack.pop() + Main.stack.pop());
            AddExp0();
        }
    }

    public static void MulExp(){
        UnaryExp();
        MulExp0();
    }

    public static void MulExp0(){
        if(type() == 12){
            src++;
            UnaryExp();
            Main.stack.push(Main.stack.pop() * Main.stack.pop());
            MulExp0();
        }
        else if(type() == 13){
            src++;
            UnaryExp();
            Main.stack.push(Main.stack.pop() / Main.stack.pop());
            MulExp0();
        }
        else if(type() == 14){
            src++;
            UnaryExp();
            Main.stack.push(Main.stack.pop() % Main.stack.pop());
            MulExp0();
        }
    }

    public static void UnaryExp() {
        if (Main.syms.get(src).getType() == 5 || Main.syms.get(src).getType() == 4) {
            primaryExp();
        } else {
            int num = UnaryOp();
            UnaryExp();
            Main.stack.push(num * Main.stack.pop());
        }
    }

    public static void primaryExp(){
        if(Main.syms.get(src).getType() == 5){
            match(5);
            Exp();
            match(6);
        }
        else{
            Number();
        }
    }

    public static int UnaryOp(){
        if(Main.syms.get(src).getType()==10){
            match(10);
            return -1;
        }
        else{
            match(11);
            return 1;
        }
    }


    public static void CompUnit(){
        FuncDef();
    };

    //返回当前syms中的token类型
    public static int type(){
        return Main.syms.get(src).getType();
    }

}
