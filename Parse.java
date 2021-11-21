

import java.util.ArrayList;
import java.util.Stack;

public class Parse {
    public static ArrayList<Var> varList = new ArrayList<>();
    public static Stack<String> tmpStack = new Stack<>();
    public static Stack<String> blockStack = new Stack<>();
    public static Stack<String> condStack = new Stack<>();
    public static Stack<String> elseJump = new Stack<>();
    public static Stack<Integer> endJump = new Stack<>();
    public static int reg = 1;
    public static int src = 0;
    public static int bNum = 1;
    public static int condNum = 1;
    public static boolean exist_var = false;
    public static void parseAnalyse(){
       CompUnit();
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

    public static void CompUnit(){
        FuncDef();
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
        return ConstExp();
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
        exist_var = false;
        return AddExp();
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
                if(!var.isConst){
                    exist_var = true;
                }
                Main.out.append("\t%" + reg++ +" = load i32, i32* " + var.getRegister() +"\n");
                tmpStack.push("%" + (reg - 1));
            }
            else{
                System.out.println("10000");
                System.exit(1);
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
                        System.out.println("2000");
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
                        if(funcName.equals("getint") || funcName.equals("getch")){
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
                        else if(funcName.equals("putch")){
                            if(params.size() != 1){
                                System.out.println("putch参数错误");
                            }
                            else{
                                Main.out.append("\tcall void @putch(i32 " + params.get(0) + ")\n");
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
                        tmpStack.push("%" + (reg - 1));
                    }
                    else if(funcName.equals("getch")){
                        Main.out.append("\t%" + reg++ + "= call i32 @getch()\n");
                        tmpStack.push("%" + (reg - 1));
                    }
                    else if(funcName.equals("putint") || funcName.equals("putch")){
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
                if(tmpStack.size() == 0){
                    System.out.println("此处栈为空1");
                    System.exit(100);
                }
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
                if(tmpStack.size() == 0){
                    System.out.println("此处栈为空2");
                    System.exit(200);
                }
                String tmpRegister1 = tmpStack.pop();
                if(num == -1){
                    if(tmpRegister1.length()<=5 || !tmpRegister1.substring(0,5).equals("%cond")){
                        Main.out.append("\t%" + reg++ + " = sub i32 " + 0 + ", " + tmpRegister1 +"\n");
                        tmpStack.push("%" + (reg-1));
                    }
                    else{
                        Main.out.append("\t%" + reg++ + "= zext i1 " + tmpRegister1 + " to i32\n");
                        String tmp = "%" + (reg - 1);
                        Main.out.append("\t%" + reg++ + " = sub i32 " + 0 + ", " + tmp +"\n");
                        tmpStack.push("%"+(reg-1));
                        Main.out.append("\t%cond" + condNum++ + " = icmp ne i32 " + tmpStack.peek() + ", 0\n");
                        condStack.push("%cond"+(condNum-1));
                    }
                }
                else if(num == 2){
                    if(tmpRegister1.length()<=5 || !tmpRegister1.substring(0,5).equals("%cond")){
                        Main.out.append("\t%cond" + condNum++ +" = icmp eq i32 " + tmpRegister1 + ", 0\n");
                        tmpStack.push("%cond"+(condNum-1));
                    }
                    else{
                        Main.out.append("\t%cond" + condNum++ +" = icmp eq i1 " + tmpRegister1 + ", 0\n");
                        tmpStack.push("%cond"+(condNum-1));
                    }
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
        else if(match(22)){
            return 2;
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
                if(tmpStack.size() == 0){
                    System.out.println("此处栈为空3");
                    System.exit(300);
                }
                String b = tmpStack.pop();
                if(tmpStack.size() == 0){
                    System.out.println("此处栈为空4");
                    System.exit(400);
                }
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
                if(tmpStack.size() == 0){
                    System.out.println("此处栈为空5");
                    System.exit(500);
                }
                String b = tmpStack.pop();
                if(tmpStack.size() == 0){
                    System.out.println("此处栈为空6");
                    System.exit(600);
                }
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
                if(tmpStack.size() == 0){
                    System.out.println("此处栈为空7");
                    System.exit(700);
                }
                String b = tmpStack.pop();
                if(tmpStack.size() == 0){
                    System.out.println("此处栈为空8");
                    System.exit(800);
                }
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
                if(tmpStack.size() == 0){
                    System.out.println("此处栈为空9");
                    System.exit(900);
                }
                String b = tmpStack.pop();
                if(tmpStack.size() == 0){
                    System.out.println("此处栈为空10");
                    System.exit(1000);
                }
                String a = tmpStack.pop();
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
                if(tmpStack.size() == 0){
                    System.out.println("此处栈为空11");
                    System.exit(1100);
                }
                String b = tmpStack.pop();
                if(tmpStack.size() == 0){
                    System.out.println("此处栈为空12");
                    System.exit(1200);
                }
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
        return AddExp();
    }

    public static boolean ConstDef(){
        int id = src;
        if(Ident()){
            String name = Main.syms.get(src - 1).getWord();
            if(inVarList(name)){
                System.out.println("3000");
                System.exit(1);
            }
            if(match(18)){
                String tmpRegister;
                if((tmpRegister = ConstInitval()) != null && !exist_var){
                    Var var = new Var("%x" + varList.size(), name, true);
                    varList.add(var);
                    Main.out.append("\t" + var.getRegister() + " = alloca i32\n");
                    Main.out.append("\tstore i32 " + tmpRegister +", i32* " + var.getRegister() +"\n");
                    return true;
                }
                else{
                    src = id;
                    System.out.println("4000");
                    System.exit(1);
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
        return Exp();
    }

    public static boolean VarDef(){
        int id = src;
        if(Ident()){
            String name = Main.syms.get(src-1).getWord();
            if(inVarList(name)){
                System.out.println("5000");
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
            if(getVarByName(name) == null){
                System.out.println("6000");
                System.exit(1);
            }
            if(getVarByName(name).isConst){
                System.out.println("7000");
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
        else if(match(7)){
            src--;
            Block();
            return true;
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
                        Main.out.append("\tret i32 " + tmpRegister);
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
        else if(match(20)){
            if(match(5)){
                String tmpCond = Cond();
                if(tmpCond != null){
                    if(match(6)){
                        Main.out.append("\tbr i1 " + tmpCond + ", label %block" + bNum++ + ", label %block" + bNum++ +"\n\n");
                        Main.out.append("block" + (bNum-2) + ":\n");
                        blockStack.push("%block" + (bNum - 2));
                        blockStack.push("%block" + (bNum - 1));
                        elseJump.push("block" + (bNum - 1));
                        if(Stmt()){
                            int tmpSize = Main.out.length();
                            endJump.push(tmpSize);
                            if(match(21)){
                                Main.out.append(elseJump.pop() + ":\n");
                                if(Stmt()){
                                    endJump.push(Main.out.length());
                                    Main.out.append("block" + bNum++ + ":\n");
                                    blockStack.push("%block" + (bNum - 1));
                                    Main.out.insert(endJump.pop(),"\tbr label %block" + (bNum - 1) + "\n\n");
                                    Main.out.insert(endJump.pop(),"\tbr label %block" + (bNum - 1) + "\n\n");
                                    return true;
                                }
                                else{
                                    System.out.println("循环出错");
                                    System.exit(1);
                                    return false;
                                }
                            }
                            else{
                                Main.out.append(elseJump.peek()+":\n\n");
                                Main.out.insert(endJump.pop(),"\tbr label %block" + (bNum - 1) + "\n\n");
                                return true;
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
        if(Main.syms.get(id).getWord().equals("const") || Main.syms.get(id).getWord().equals("int")){
            Decl();
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

    public static void Block(){
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
                ;
            }
            else{
                System.out.println("8000");
                System.exit(1);
            }
        }
        else{
            System.out.println("9000");
            System.exit(1);
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


    public static void FuncDef(){
        int id = src;
        if(FuncType()){
            if(Main()){
                if(match(5)){
                    if(match(6)){
                        Block();
                    }
                    else{
                        System.out.println("100001");
                        System.exit(1);
                    }
                }
                else{
                    System.out.println("100002");
                    System.exit(1);
                }
            }
            else{
                System.out.println("100003");
                System.exit(1);
            }
        }
        else{
            System.out.println("100004");
            System.exit(1);
        }
    }

    public static String Cond(){
        if(LOrExp()){
            return condStack.pop();
        }
        else{
            return null;
        }
    }

    public static boolean LOrExp(){
        int id = src;
        if(LAndExp()){
            if(LOrExp0()){
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

    public static boolean LOrExp0(){
        int id = src;
        if(match(29)){
            if(EqExp()){
                String b = condStack.pop();
                String a = condStack.pop();
                Main.out.append("\t%cond" + condNum++ + " = or i1 " + a +", " + b +"\n");
                condStack.push("%cond"+(condNum-1));
                if(LAndExp0()){
                    if(LOrExp0()){
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
            return true;
        }
    }


    public static boolean RelExp(){
        int id = src;
        String tmpString = AddExp();
        tmpStack.push(tmpString);
        if(RelExp0()){
            return true;
        }
        else{
            src = id;
            return false;
        }
    }

    public static boolean RelExp0(){
        int id = src;
        if(match(25)){
            String tmpRegister = AddExp();
            tmpStack.push(tmpRegister);
            String b = tmpStack.pop();
            String a = tmpStack.pop();
            Main.out.append("\t%cond" + condNum++ + " = icmp slt i32 " + a +", " + b +"\n");
            condStack.push("%cond"+(condNum-1));
            if(RelExp0()){
                return true;
            }
            else{
                src = id;
                return false;
            }
        }
        else if(match(26)){
            String tmpRegister = AddExp();
            tmpStack.push(tmpRegister);
            String b = tmpStack.pop();
            String a = tmpStack.pop();
            Main.out.append("\t%cond" + condNum++ + " = icmp sgt i32 " + a +", " + b+"\n");
            condStack.push("%cond"+(condNum-1));
            if(RelExp0()){
                return true;
            }
            else{
                src = id;
                return false;
            }
        }
        else if(match(27)){
            String tmpRegister = AddExp();
            tmpStack.push(tmpRegister);
            String b = tmpStack.pop();
            String a = tmpStack.pop();
            Main.out.append("\t%cond" + condNum++ + " = icmp sle i32 " + a +", " + b +"\n");
            condStack.push("%cond"+(condNum-1));
            if(RelExp0()){
                return true;
            }
            else{
                src = id;
                return false;
            }
        }
        else if(match(28)){
            String tmpRegister = AddExp();
            tmpStack.push(tmpRegister);
            String b = tmpStack.pop();
            String a = tmpStack.pop();
            Main.out.append("\t%cond" + condNum++ + " = icmp sge i32 " + a +", " + b + "\n");
            condStack.push("%cond"+(condNum-1));
            if(RelExp0()){
                return true;
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

    public static boolean EqExp(){
        int id = src;
        if(RelExp()){
            if(EqExp0()){
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

    public static boolean EqExp0(){
        int id = src;
        if(match(23)){
            if(RelExp()){
                String b = tmpStack.pop();
                String a = tmpStack.pop();
                Main.out.append("\t%cond" + condNum++ + " = icmp eq i32 " + a +", " + b + "\n");
                condStack.push("%cond"+(condNum-1));
                if(EqExp0()){
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
        else if(match(24)){
            if(RelExp()){
                String b = tmpStack.pop();
                String a = tmpStack.pop();
                Main.out.append("\t%cond" + condNum++ + " = icmp ne i32 " + a +", " + b + "\n");
                condStack.push("%cond"+(condNum-1));
                if(EqExp0()){
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

    public static boolean LAndExp(){
        int id = src;
        if(EqExp()){
            if(LAndExp0()){
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

    public static boolean LAndExp0(){
        int id = src;
        if(match(30)){
            if(EqExp()) {
                String b = condStack.pop();
                String a = condStack.pop();
                Main.out.append("\t%cond" + condNum++ + " = and i1 " + a +", " + b +"\n");
                condStack.push("%cond"+(condNum-1));
                if (LAndExp0()) {
                    return true;
                } else {
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
