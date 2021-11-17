

import java.util.ArrayList;
import java.util.Stack;

public class Parse {
    public static ArrayList<Var> varList = new ArrayList<>();
    public static Stack<String> tmpStack = new Stack<>();
    public static int reg = 1;
    public static int src = 0;
    public static boolean parseAnalyse(){
       if(CompUnit()){
           return true;
       }
       else{
           return false;
       }
    }

    public static boolean match(int x){
        if(src >= Main.syms.size()){
            System.out.println("out of index");
            System.exit(1);
        }
        if(Main.syms.get(src++).getType() != x){
            src--;
            return false;
        }
        else{
            return true;
        }
    }

    public static boolean CompUnit(){
        if(FuncDef()){
            return true;
        }
        else{
            return false;
        }
    }

    public static boolean FuncType(){
        int id = src;
        if(match(1)){
            return true;
        }
        else{
            src = id;
            return false;
        }
    }

    public static boolean Ident(){
        int id = src;
        if(match(17)){
            return true;
        }
        else{
            src = id;
            return false;
        }
    }

    public static boolean BType(){
        int id = src;
        if(match(1)){
            return true;
        }
        else{
            src = id;
            return false;
        }
    }

    public static String ConstInitval(){
        int id = src;
        String tmpRegister;
        if((tmpRegister = ConstExp()) != null){
            return tmpRegister;
        }
        else{
            src = id;
            return null;
        }
    }

    public static boolean Number(){
        int id = src;
        if(match(4)){
            return true;
        }
        else{
            src = id;
            return false;
        }
    }

    public static String Exp(){
        int id = src;
        String tmpRegister;
        if((tmpRegister = AddExp()) != null){
            return tmpRegister;
        }
        else{
            return null;
        }
    }

    public static boolean FunctionIdent(){
        if(src >= Main.syms.size()){
            System.out.println("out of index");
            System.exit(1);
        }
        if(Main.syms.get(src++).getType() != 19){
            src--;
            return false;
        }
        else{
            return true;
        }
    }

    public static boolean LVal(){
        int id = src;
//        String name = Main.syms.get(src).getWord();
        if(Ident()){
            return true;
        }
        else{
            src = id;
            return false;
        }
    }

    public static boolean PrimaryExp(){
        int id = src;
        if(match(5)){
            String tmpRegister;
            if((tmpRegister = Exp()) != null){
                if(match(6)){
                    if(tmpRegister.length()>=2 && tmpRegister.substring(0,2).equals("%x")){
                        Main.out.append("\t%" + reg++ +" = load i32, i32* " + tmpRegister +"\n");
                        tmpStack.push("%" + (reg - 1));
                    }
                    else{
                        tmpStack.push(tmpRegister);
                    }
                    return true;
                }
                else{
                    src = id;
                    return false;
                }
            }
            else{
                src = id;
                return false;
            }
        }
        else if(LVal()){
            String name = Main.syms.get(src - 1).getWord();
            if(inVarList(name)){
                Var var = getVarByName(name);
                Main.out.append("\t%" + reg++ +" = load i32, i32* " + var.getRegister() +"\n");
                tmpStack.push("%" + (reg - 1));
            }
            else{
                src = id;
                return false;
            }
            return true;
        }
        else if(Number()){
            tmpStack.push(Main.syms.get(src-1).getWord());
            return true;
        }
        else{
            src = id;
            return false;
        }
    }

    public static ArrayList<String> FuncRParams(){
        int id = src;
        ArrayList<String> params = new ArrayList<>();
        String tmpRegister1;
        if((tmpRegister1 = Exp()) != null){
            params.add(tmpRegister1);
            while(true){
                if(match(16)){
                    String tmp;
                    if((tmp = Exp()) != null){
                        params.add(tmp);
                    }
                    else{
                        src = id;
                        System.exit(1);
                    }
                }
                else{
                    break;
                }
            }
        }
        return params;
    }

    public static boolean UnaryExp(){
        int num = 0;
        int id = src;
        if(FunctionIdent()){
            String funcName = Main.syms.get(src-1).getWord();
            if(match(5)){
                ArrayList<String> params = FuncRParams();
                if(params.size() != 0){
                    if(match(6)){
                        if(funcName.equals("getint()")){
                            System.out.println("getint参数错误");
                            System.exit(1);
                        }
                        else if(funcName.equals("putint")){
                            if(params.size() != 1){
                                System.out.println("putint参数错误");
                            }
                            else{
                                Main.out.append("\tcall void @putint(i32 " + params.get(0) + ")\n");
                            }
                        }
                        return true;
                    }
                    else{
                        src = id;
                        return false;
                    }
                }
                else if(match(6)){
                    if(funcName.equals("getint")){
                        Main.out.append("\t%" + reg++ + "= call i32 @getint()\n");
                        tmpStack.push("%" + (reg-1));
                    }
                    else if(funcName.equals("putint")){
                        System.out.println("putint参数不能为空");
                    }
                    return true;
                }
                else{
                    src = id;
                    return false;
                }
            }
            else{
                src = id;
                return false;
            }
        }
        if(PrimaryExp()){
            return true;
        }
        else if((num = UnaryOp()) != 0){
            if(UnaryExp()){
                String tmpRegister = tmpStack.pop();
                if(tmpRegister.length() >= 2){
                    if(tmpRegister.substring(0,2).equals("%x")){
                        Main.out.append("\t%" + reg++ +" = load i32, i32* " + tmpRegister +"\n");
                        tmpStack.push("%" + (reg-1));
                    }
                    else{
                        tmpStack.push(tmpRegister);
                    }
                }
                else{
                    tmpStack.push(tmpRegister);
                }
                String tmpRegister1 = tmpStack.pop();
                if(num == -1){
                    Main.out.append("\t%" + reg++ + " = sub i32 " + 0 + ", " + tmpRegister1 +"\n");
                    tmpStack.push("%" + (reg-1));
                }
                else{
                    tmpStack.push(tmpRegister1);
                }
                return true;
            }
            else{
                src = id;
                return false;
            }
        }
        else{
            src = id;
            return false;
        }
    }

    public static int UnaryOp(){
        int id = src;
        if(match(10)){
            return -1;
        }
        else if(match(11)){
            return 1;
        }
        else{
            src = id;
            return 0;
        }
    }

    public static boolean MulExp0(){
        int id = src;
        if(match(12)){
            if(UnaryExp()){
                String b = tmpStack.pop();
                String a = tmpStack.pop();
                String tmpRegister = "%" + reg;
                tmpStack.push(tmpRegister);
                Main.out.append("\t%" + reg++ + " = mul i32 " + a + ", " + b + "\n" );
                if(MulExp0()){
                    return true;
                }
                else{
                    src = id;
                    return false;
                }
            }
            else{
                src = id;
                return false;
            }
        }
        else if(match(13)){
            if(UnaryExp()){
                String b = tmpStack.pop();
                String a = tmpStack.pop();
                String tmpRegister = "%" + reg;
                tmpStack.push(tmpRegister);
                Main.out.append("\t%" + reg++ + " = sdiv i32 " + a + ", " + b + "\n");
                if(MulExp0()){
                    return true;
                }
                else{
                    src = id;
                    return false;
                }
            }
            else{
                src = id;
                return false;
            }
        }
        else if(match(14)){
            if(UnaryExp()){
                String b = tmpStack.pop();
                String a = tmpStack.pop();
                String tmpRegister = "%" + reg;
                tmpStack.push(tmpRegister);
                Main.out.append("\t%" + reg++ + " = srem i32 " + a + ", " + b + "\n");
                if(MulExp0()){
                    return true;
                }
                else{
                    src = id;
                    return false;
                }
            }
            else{
                src = id;
                return false;
            }
        }
        else{
            return true;
        }
    }

    public static boolean MulExp(){
        int id = src;
        if(UnaryExp()){
            if(MulExp0()){
                return true;
            }
            else{
                src = id;
                return false;
            }
        }
        else{
            src = id;
            return false;
        }
    }

    public static String AddExp(){
        int id = src;
        if(MulExp()){
            if(AddExp0()){
                if(tmpStack.size() != 0){
                    return tmpStack.pop();
                }
                else{
                    return null;
                }
            }
            else{
                src = id;
                return null;
            }
        }
        else{
            src = id;
            return null;
        }
    }
    public static boolean AddExp0(){
        int id = src;
        if(match(10)){
            if(MulExp()){
                String b = tmpStack.pop();
//                String b1;
//                if(b.substring(0,2).equals("%x")){
//                    Main.out.append("\t%" + reg++ +" = load i32, i32* " + b +"\n");
//                    b1 = "%" + (reg - 1);
//                }
//                else{
//                    b1 = b;
//                }
                String a = tmpStack.pop();
//                String a1;
//                if(a.substring(0,2).equals("%x")){
//                    Main.out.append("\t%" + reg++ +" = load i32, i32* " + a +"\n");
//                    a1 = "%" + (reg - 1);
//                }
//                else{
//                    a1 = a;
//                }
                String tmpRegister = "%" + reg;
                tmpStack.push(tmpRegister);
                Main.out.append("\t%" + reg++ + " = sub i32 " + a + ", " + b +"\n");
                if(AddExp0()){
                    return true;
                }
                else{
                    src = id;
                    return false;
                }
            }
            else{
                src = id;
                return false;
            }
        }
        else if(match(11)){
            if(MulExp()){
                String b = tmpStack.pop();
                String a = tmpStack.pop();
                String tmpRegister = "%" + reg;
                tmpStack.push(tmpRegister);
                Main.out.append("\t%" + reg++ + " = add i32 " + a + ", " + b +"\n");
                if(AddExp0()){
                    return true;
                }
                else{
                    src = id;
                    return false;
                }
            }
            else{
                src = id;
                return false;
            }
        }
        else{
            return true;
        }
    }

    public static String ConstExp(){
        int id = src;
        String tmpRegister;
        if((tmpRegister = AddExp()) != null){
            return tmpRegister;
        }
        else{
            src = id;
            return null;
        }
    }

    public static boolean ConstDef(){
        int id = src;
        if(Ident()){
            String name = Main.syms.get(src - 1).getWord();
            if(inVarList(name)){
                System.exit(1);
            }
            if(match(18)){
                String tmpRegister;
                if((tmpRegister = ConstInitval()) != null){
                    Var var = new Var("%x" + varList.size(), name, true);
                    varList.add(var);
                    Main.out.append("\t" + var.getRegister() + " = alloca i32\n");
                    Main.out.append("\tstore i32 " + tmpRegister +", i32* " + var.getRegister() +"\n");
                    return true;
                }
                else{
                    src = id;
                    return false;
                }
            }
            else{
                src = id;
                return false;
            }
        }
        else{
            src = id;
            return false;
        }
    }

    public static boolean ConstDecl(){
        int id = src;
        if(match(15)){
            if(BType()){
                if(ConstDef()){
                    while(true){
                        if(match(16)){
                            if(ConstDef()){
                                ;
                            }
                            else{
                                src = id;
                                return false;
                            }
                        }
                        else{
                            break;
                        }
                    }
                    return true;
                }
                else{
                    src = id;
                    return false;
                }
            }
            else{
                src = id;
                return false;
            }
        }
        else{
            src = id;
            return false;
        }
    }

    public static String InitVal(){
        int id = src;
        String tmpRegister;
        if((tmpRegister = Exp()) != null){
            return tmpRegister;
        }
        else{
            src = id;
            return null;
        }
    }

    public static boolean VarDef(){
        int id = src;
        if(Ident()){
            String name = Main.syms.get(src-1).getWord();
            if(inVarList(name)){
                System.exit(1);
            }
            if(match(18)){
                String tmpRegister;
                if((tmpRegister = InitVal()) != null){
                    Var var = new Var("%x"+ varList.size(),name,false);
                    varList.add(var);
                    Main.out.append("\t" + var.getRegister() + " = alloca i32\n");
                    Main.out.append("\tstore i32 " + tmpRegister +", i32* " + var.getRegister()+"\n");

                    return true;
                }
                else{
                    src = id;
                    return false;
                }
            }
            else{
                Var var = new Var("%x"+ varList.size(),name,false);
                varList.add(var);
                Main.out.append("\t" + var.getRegister() + " = alloca i32\n");
                return true;
            }
        }
        else{
            src = id;
            return false;
        }
    }

    public static boolean VarDecl(){
        int id = src;
        if(BType()){
            if(VarDef()){
                while(true){
                    if(match(16)){
                        if(VarDef()){
                            ;
                        }
                        else{
                            return true;
                        }
                    }
                    else{
                        break;
                    }
                }
                if(match(9)){
                    return true;
                }
                else{
                    src = id;
                    return false;
                }
            }
            else{
                src = id;
                return false;
            }
        }
        else{
            src = id;
            return false;
        }
    }

    public static boolean Decl(){
        int id = src;
        if(ConstDecl()){
            return true;
        }
        else if(VarDecl()){
            return true;
        }
        else{
            src = id;
            return false;
        }
    }

    public static boolean Stmt(){
        int id = src;
        if(LVal()){
            String name = Main.syms.get(src-1).getWord();
            if(getVarByName(name).isConst){
                System.exit(1);
            }
            if(match(18)){
                String tmpRegister;
                if((tmpRegister = Exp()) != null){
                    if(match(9)){
                        Main.out.append("\tstore i32 " + tmpRegister +", i32* " + getVarByName(name).getRegister() +"\n");
                        return true;
                    }
                    else{
                        src = id;
                        return false;
                    }
                }
                else{
                    src = id;
                    return false;
                }
            }
            else{
                src = id;
                return false;
            }
        }
        else if(match(3)){
            String tmpRegister;
            if((tmpRegister = Exp()) != null){
                if(match(9)){
                    if(tmpRegister.length()>= 2 && tmpRegister.substring(0,2).equals("%x")){
                        Main.out.append("\t%" + reg++ +" = load i32, i32* " + tmpRegister +"\n");
                        Main.out.append("\tret i32 %" + (reg-1));
                    }
                    else{
                        Main.out.append("\tret i32 %" + tmpRegister);
                    }
                    return true;
                }
                else{
                    src = id;
                    return false;
                }
            }
            else{
                src = id;
                return false;
            }
        }
        else if(Exp() != null){
            if(match(9)){
                return true;
            }
            else{
                src = id;
                return false;
            }
        }
        else if(match(9)){
            return true;
        }
        else{
            src = id;
            return false;
        }
    }

    public static boolean BlockItem(){
        int id = src;
        if(Decl()){
            return true;
        }
        else if(Stmt()){
            return true;
        }
        else{
            src = id;
            return false;
        }
    }

    public static boolean Block(){
        int id = src;
        if(match(7)){
            while(true){
                if(BlockItem()){
                    ;
                }
                else{
                    break;
                }
            }
            if(match(8)){
                return true;
            }
            else{
                src = id;
                return false;
            }
        }
        else{
            src = id;
            return false;
        }
    }

    public static boolean Main(){
        int id = src;
        if(match(2)){
            return true;
        }
        else{
            src = id;
            return false;
        }
    }


    public static boolean FuncDef(){
        int id = src;
        if(FuncType()){
            if(Main()){
                if(match(5)){
                    if(match(6)){
                        if(Block()){
                            return true;
                        }
                        else{
                            src = id;
                            return false;
                        }
                    }
                    else{
                        src = id;
                        return false;
                    }
                }
                else{
                    src = id;
                    return false;
                }
            }
            else{
                src = id;
                return false;
            }
        }
        else{
            src = id;
            return false;
        }
    }

    public static boolean inVarList(String name){
        for (int i = 0; i < varList.size(); i++){
            if(varList.get(i).getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public static Var getVarByName(String name){
        for(int i = 0; i < varList.size(); i++){
            if(varList.get(i).getName().equals(name)){
                return varList.get(i);
            }
        }
        return null;
    }

}
