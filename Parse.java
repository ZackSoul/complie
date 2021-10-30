package lab2;

public class Parse {
    public static int src = 0;
    public static void parseAnalyse(){
        CompUnit();
    }

    public static void match(int x){
        if(Main.syms.get(src++).getType() != x){
//            System.out.println(x);
//            System.out.println(Main.syms.get(src));
            System.exit(1);
        }
//        else{
//            System.out.println(x);
//        }
    }

    public static void Number(){
        match(4);
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
    }

    public static void MulExp(){
        UnaryExp();
    }

    public static void UnaryExp() {
        if (Main.syms.get(src).getType() == 5 || Main.syms.get(src).getType() == 4) {
            primaryExp();
        } else {
            UnaryOp();
            UnaryExp();
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

    public static void UnaryOp(){
        if(Main.syms.get(src).getType()==10){
            match(10);
        }
        else{
            match(11);
        }
    }


    public static void CompUnit(){
        FuncDef();
    };

}
