package lab1;

public class Parse {
    public static int src = 0;
    public static void parseAnalyse(){
        CompUnit();
    }

    public static void match(int x){
        if(Main.syms.get(src++).getType() != x){
            System.exit(1);
        }
    }

    public static void Number(){
        match(4);
    }

    public static void Stmt(){
        match(3);
        Number();
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

    public static void CompUnit(){
        FuncDef();
    };

}
