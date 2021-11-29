

import java.util.ArrayList;
import java.util.Arrays;
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
    public static int bNum = 0;
    public static int condNum = 1;
    public static boolean exist_var = false;
    public static boolean exit = false;
    public static int curBlock = 1;
    public static boolean initCond = false;
    public static boolean inGlobal = true;
    public static boolean isContinue = false;
    public static boolean isBreak = false;
    public static int ptrNum = 1;
    public static Stack<Integer> whileJump = new Stack<>();
    public static Stack<Integer> continueJump = new Stack<>();
    public static Stack<String> breakJump = new Stack<>();
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
        while(Decl()){
            ;
        }
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
        StringBuilder str = new StringBuilder();
        if(match(7)){
            if(match(8)){
                return "empty_";
            }
            else{
                String tmp = ConstInitval();
                if(tmp != null){
                    str.append(tmp);
//                    String[] str1 = str.toString().split(" ");
//                    var.setArr1(Arrays.asList(str1));
                    while(match(16)){
                        String tmp1 = ConstInitval();
                        if(tmp1 != null){
                            str.append(" " + tmp1);
                        }
                        else{
                            System.out.println("78");
                            System.exit(1);
                            return null;
                        }
                    }
                    if(match(8)){
                        str.append("_");
                        return str.toString();
                    }
                    else{
                        System.out.println(Main.syms.get(src).getWord());
                        System.out.println("451");
                        System.exit(1);
                        return null;
                    }
                }
                else{
                    System.out.println("1001010");
                    System.exit(1);
                    return null;
                }
            }
        }
        else {
            return ConstExp();
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

    public static int LVal(){
        int id = src;
        if(Ident()){
            String name = Main.syms.get(src-1).getWord();
            Var var = getVarByName(name);
            ArrayList<String> tmps = new ArrayList<>();
            int dimens = 0;
            while(match(34)){
                String tmpString = Exp();
                if(match(35)){
                    dimens++;
                     tmps.add(tmpString);
                }
                else{
                    System.out.println("wdf");
                    System.exit(1);
                    return -1;
                }
            }
            if(dimens != var.getDimension()){
                System.out.println("维数错误");
                System.exit(1);
                return -1;
            }
            else{
                if(dimens == 0){
                    return id;
                }
                else if(dimens == 1){
                    Main.out.append("\t%" + reg++ + " = add i32 0, " + tmps.get(0) + "\n");
                    if(var.isGlobal){
                        Main.out.append("\t%ptr" + ptrNum++ + " = getelementptr [" + var.getY() + " x i32], [" + var.getY() + " x i32]* "+ var.getRegister() + ", i32 0, i32 0\n");
                        var.setPtr("%ptr"+(ptrNum-1));
                    }
                    Main.out.append("\t%" + reg++ + " = getelementptr i32, i32* " + var.getPtr() + ", i32 %" + (reg-2) + "\n");
                    Main.out.append("\t%" + reg++ + " = load i32, i32* %" + (reg-2) + "\n");
                    tmpStack.push("%"+(reg-1));
                    return -2;
                }
                else if(dimens == 2){
                    Main.out.append("\t%" + reg++ + " = add i32 0, " + tmps.get(0) + "\n");
                    Main.out.append("\t%" + reg++ + " = mul i32 %" + (reg-2) + ", " + var.getY() + "\n");
                    Main.out.append("\t%" + reg++ + " = add i32 %" + (reg-2) + ", " + tmps.get(1) + "\n");
                    if(var.isGlobal){
                        Main.out.append("\t%ptr" + ptrNum++ + " = getelementptr [" + var.getX() + " x [" + var.getY() + " x i32]], [" + var.getX() + " x [" + var.getY() + " x i32]]* " + var.getRegister() + ", i32 0, i32 0\n");
                        Main.out.append("\t%ptr" + ptrNum++ + " = getelementptr [" + var.getY() +" x i32], [" + var.getY() +" x i32]* "+ "%ptr" + (ptrNum-2) + ", i32 0, i32 0\n");
                        var.setPtr("%ptr"+(ptrNum-1));
                    }
                    Main.out.append("\t%" + reg++ + " = getelementptr i32, i32* " + var.getPtr() + ", i32 %" + (reg-2) + "\n");
                    Main.out.append("\t%" + reg++ + " = load i32, i32* %" + (reg-2) + "\n");
                    tmpStack.push("%"+(reg-1));
                    return -2;
                }
            }
        }
        else{
            src = id;
            return -1;
        }
        return -1;
    }

    public static boolean PrimaryExp(){
        int id = src;
        int x;
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
        else if(( x = LVal()) != -1){
            if(x != -2){
                String name = Main.syms.get(x).getWord();
                System.out.println(name);
                Var tmpVar = getVarByName(name);
                if(tmpVar.isArray){
                    return true;
                }
                else{
                    if(inVarList(name)){
                        Var var = getVarByName(name);
                        if(!var.isConst){
                            exist_var = true;
                        }
                        if(var.isGlobal && var.isConst){
                            System.out.println("yes");
                            tmpStack.push(String.valueOf(var.getValue()));
                        }
                        else{
                            Main.out.append("\t%" + reg++ +" = load i32, i32* " + var.getRegister() +"\n");
                            tmpStack.push("%" + (reg - 1));
                        }
                    }
                    else{
                        for(int i = 0; i < varList.size(); i++){
                            System.out.println(varList.get(i).getName() + " " + varList.get(i).getBlockNum());
                        }
                        System.out.println(name);
                        System.out.println("10000");
                        System.exit(1);
                    }
                    return true;
                }
            }
            else{
                return true;
            }
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
                if(inGlobal && !a.substring(0,1).equals("%")&&!b.substring(0,1).equals("%")){
                    int a1 = Integer.valueOf(a);
                    int b1 = Integer.valueOf(b);
                    int c = a1 * b1;
                    tmpStack.push(String.valueOf(c));
                }
                else{
                    String tmpRegister = "%" + reg;
                    tmpStack.push(tmpRegister);
                    Main.out.append("\t%" + reg++ + " = mul i32 " + a + ", " + b + "\n" );
                }
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
                if(inGlobal && !a.substring(0,1).equals("%")&&!b.substring(0,1).equals("%")){
                    int a1 = Integer.valueOf(a);
                    int b1 = Integer.valueOf(b);
                    int c = a1 / b1;
                    tmpStack.push(String.valueOf(c));
                }
                else{
                    String tmpRegister = "%" + reg;
                    tmpStack.push(tmpRegister);
                    Main.out.append("\t%" + reg++ + " = sdiv i32 " + a + ", " + b + "\n");
                }
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
                if(inGlobal && !a.substring(0,1).equals("%")&&!b.substring(0,1).equals("%")){
                    int a1 = Integer.valueOf(a);
                    int b1 = Integer.valueOf(b);
                    int c = a1 % b1;
                    tmpStack.push(String.valueOf(c));
                }
                else{
                    String tmpRegister = "%" + reg;
                    tmpStack.push(tmpRegister);
                    Main.out.append("\t%" + reg++ + " = srem i32 " + a + ", " + b + "\n");
                }
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
                return null;
            }
        }
        else{
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
                if(inGlobal && !a.substring(0,1).equals("%")&&!b.substring(0,1).equals("%")){
                    int a1 = Integer.valueOf(a);
                    int b1 = Integer.valueOf(b);
                    int c = a1 - b1;
                    tmpStack.push(String.valueOf(c));
                }
                else{
                    String tmpRegister = "%" + reg;
                    tmpStack.push(tmpRegister);
                    Main.out.append("\t%" + reg++ + " = sub i32 " + a + ", " + b +"\n");
                }
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
                if(inGlobal && !a.substring(0,1).equals("%")&&!b.substring(0,1).equals("%")){
                    int a1 = Integer.valueOf(a);
                    int b1 = Integer.valueOf(b);
                    int c = a1 + b1;
                    tmpStack.push(String.valueOf(c));
                }
                else{
                    String tmpRegister = "%" + reg;
                    tmpStack.push(tmpRegister);
                    Main.out.append("\t%" + reg++ + " = add i32 " + a + ", " + b +"\n");
                }
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
        int dimension = 0;
        boolean isArray = false;
        if(Ident()){
            String name = Main.syms.get(src - 1).getWord();
            if(inBlockVarList(name)){
                System.out.println("3000");
                System.exit(1);
            }
            ArrayList<String> tmps = new ArrayList<>();
            while(match(34)){
                String tmp = ConstExp();
                if(tmp != null){
                    tmps.add(tmp);
                    if(match(35)){
                        isArray = true;
                        dimension++;
                    }
                    else{
                        System.out.println("2324341");
                        System.exit(1);
                        return false;
                    }
                }
                else{
                    System.out.println("232434");
                    System.exit(1);
                    return false;
                }
            }
            if(match(18)){
                String tmpRegister;
                if((tmpRegister = ConstInitval()) != null && !exist_var){
                    if(isArray){
                        if(inGlobal){
                            if(noSameGloabl(name)){
                                Var var = new Var("@" + name, name, true, -2,true,true,dimension);
                                varList.add(var);
                                System.out.println(tmpRegister);
                                if(tmpRegister.equals("empty_")){
                                    tmpRegister = tmpRegister.substring(0,tmpRegister.length()-1);
                                }
                                else{
                                    tmpRegister = tmpRegister.substring(0,tmpRegister.length()-2);
                                }
                                if(dimension == 1){
                                    int num = Integer.valueOf(tmps.get(0));
                                    var.setY(num);
                                    var.setX(1);
                                    StringBuilder dimen1 = new StringBuilder();
                                    dimen1.append(tmpRegister);
                                    if(tmpRegister.equals("empty")){
                                        dimen1.setLength(0);
                                        for(int i = 0; i < var.getY(); i++){
                                            dimen1.append(" 0");
                                        }
                                        dimen1.deleteCharAt(0);
                                    }
                                    String[] items = dimen1.toString().split(" ");
                                    for(int i = 0; i< items.length; i++){
                                        System.out.println(items[i]);
                                    }
                                    Main.out.append(var.getRegister() +" = dso_local constant" + " [" + tmps.get(0) + " x i32] [");
                                    int realNum = items.length;
                                    for(int i = 0; i < realNum; i++){
                                        if(i == num -1){
                                            Main.out.append(" i32 " + items[i] + "]\n");
                                        }
                                        else{
                                            Main.out.append(" i32 "+ items[i] + ",");
                                        }
                                    }
                                    for(int i = realNum; i < num; i++){
                                        if(i == num -1){
                                            Main.out.append(" i32 "+ 0 + "]\n");
                                        }
                                        else{
                                            Main.out.append(" i32 "+ 0 + ",");
                                        }
                                    }
                                }
                                else if(dimension == 2){
                                    var.setX(Integer.valueOf(tmps.get(0)));
                                    var.setY(Integer.valueOf(tmps.get(1)));
                                    System.out.println(tmpRegister);
                                    String[] tmpSplit = tmpRegister.split("_ ");
                                    Main.out.append(var.getRegister() + " = dso_local constant ["+ var.getX() + " x [" + var.getY() + " x i32]] [");
                                    for(int i = 0; i < tmpSplit.length; i++){
                                        if(tmpSplit[i].equals("empty")){
//                                            ArrayList<String> tmp = new ArrayList<>();
//                                            for(int j = 0; j < var.getY(); j++){
//                                                tmp.add("0");
//                                                System.out.println(tmp.get(j));
//                                            }
//                                            System.out.println("num = " + tmp.size());
                                            Main.out.append("[" + var.getY() + " x i32] zeroinitializer]\n");
                                        }
                                        else{
                                            Main.out.append("[" + var.getY() + " x i32] [");
                                            String[] tmp = tmpSplit[i].split(" ");
                                            for(int j = 0; j < tmp.length; j++){
                                                System.out.println(tmp[j]);
                                                if(j == var.getY() - 1){
                                                    if(i == var.getX() - 1){
                                                        Main.out.append("i32 " + tmp[j] + "]]\n");
                                                    }
                                                    else{
                                                        Main.out.append("i32 " + tmp[j] + "], ");
                                                    }
                                                }
                                                else{
                                                    Main.out.append("i32 " + tmp[j] + ", ");
                                                }
                                            }
                                            for(int j = tmp.length; j < var.getY(); j++){
                                                if(j == var.getY() - 1){
                                                    Main.out.append("i32 " + 0 + "], ");
                                                }
                                                else{
                                                    Main.out.append("i32 " + 0 + ", ");
                                                }
                                            }
                                            System.out.println("num = " + tmp.length);
                                            System.out.println("=============");
                                        }

                                    }
                                    for(int i = tmpSplit.length; i < var.getX(); i++){
                                        if(i == var.getX()-1){
                                            Main.out.append("[" + var.getY() +" x i32] zeroinitializer]\n");
                                        }
                                        else{
                                            Main.out.append("[" + var.getY() +" x i32] zeroinitializer], \n");
                                        }
                                    }
                                }
                            }
                            else{
                                System.out.println("this global var has been defined1");
                                System.exit(1);
                            }
                        }
                    }
                    else{
                        if(inGlobal){
                            if(noSameGloabl(name)){
                                Var var = new Var("@" + name, name, true, -2,true,false,0);
                                var.setValue(Integer.valueOf(tmpRegister));
                                varList.add(var);
                            }
                            else{
                                System.out.println("this global var has been defined1");
                                System.exit(1);
                            }
                        }
                        else{
                            Var var = new Var("%x" + varList.size(), name, true, curBlock,false,false,0);
                            varList.add(var);
                            Main.out.append("\t" + var.getRegister() + " = alloca i32\n");
                            Main.out.append("\tstore i32 " + tmpRegister +", i32* " + var.getRegister() +"\n");
                        }
                    }
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
                                System.out.println("333");
                                System.exit(1);
                                return false;
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
                       System.out.println("213424");
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
        else{
            src = id;
            return false;
        }
    }

    public static String InitVal(){
        int id = src;
        StringBuilder str = new StringBuilder();
        if(match(7)){
            if(match(8)){
                return "empty_";
            }
            else{
                String tmp = InitVal();
                if(tmp != null){
                    str.append(tmp);
                    while(match(16)){
                        String tmp1 = InitVal();
                        if(tmp1 != null){
                            str.append(" " + tmp1);
                        }
                        else {
                            System.out.println("21233");
                            System.exit(1);
                            return null;
                        }
                    }
                    if(match(8)){
                        str.append("_");
                        return str.toString();
                    }
                    else{
                        System.out.println("1209");
                        System.exit(1);
                        return null;
                    }
                }
                else{
                    System.out.println("12123");
                    System.exit(1);
                    return null;
                }
            }
        }
        else{
            return Exp();
        }
    }

    public static boolean VarDef(){
        int id = src;
        int dimension = 0;
        boolean isArray = false;
        if(Ident()){
            String name = Main.syms.get(src-1).getWord();
            if(inBlockVarList(name)){
                System.out.println("5000");
                System.exit(1);
            }
            ArrayList<String> tmps = new ArrayList<>();
            while(match(34)){
                String tmp = ConstExp();
                if(tmp != null){
                    tmps.add(tmp);
                    if(match(35)){
                        isArray = true;
                        dimension++;
                    }
                    else{
                        System.out.println("7778");
                        System.exit(1);
                        return false;
                    }
                }
                else{
                    System.out.println("777");
                    System.exit(1);
                    return false;
                }
            }
            if(match(18)){
                String tmpRegister;
                if((tmpRegister = InitVal()) != null){
                    if(isArray){
                        if(inGlobal){
                            if(noSameGloabl(name)){
                                Var var = new Var("@" + name, name, false, -2,true,true,dimension);
                                varList.add(var);
                                System.out.println(tmpRegister);
                                if(tmpRegister.equals("empty_")){
                                    tmpRegister = tmpRegister.substring(0,tmpRegister.length()-1);
                                }
                                else{
                                    tmpRegister = tmpRegister.substring(0,tmpRegister.length()-2);
                                }
                                if(dimension == 1){
                                    int num = Integer.valueOf(tmps.get(0));
                                    var.setY(num);
                                    var.setX(1);
                                    StringBuilder dimen1 = new StringBuilder();
                                    dimen1.append(tmpRegister);
                                    if(tmpRegister.equals("empty")){
                                        dimen1.setLength(0);
                                        for(int i = 0; i < var.getY(); i++){
                                            dimen1.append(" 0");
                                        }
                                        dimen1.deleteCharAt(0);
                                    }
                                    String[] items = dimen1.toString().split(" ");
                                    Main.out.append(var.getRegister() +" = dso_local global" + " [" + tmps.get(0) + " x i32] [");
                                    int realNum = items.length;
                                    for(int i = 0; i < realNum; i++){
                                        if(i == num -1){
                                            Main.out.append(" i32 " + items[i] + "]\n");
                                        }
                                        else{
                                            Main.out.append("i32 "+ items[i] + ", ");
                                        }
                                    }
                                    for(int i = realNum; i < num; i++){
                                        if(i == num -1){
                                            Main.out.append(" i32 "+ 0 + "]\n");
                                        }
                                        else{
                                            Main.out.append(" i32 "+ 0 + ",");
                                        }
                                    }
                                }
                                else if(dimension == 2){
                                    var.setX(Integer.valueOf(tmps.get(0)));
                                    var.setY(Integer.valueOf(tmps.get(1)));
                                    System.out.println(tmpRegister);
                                    String[] tmpSplit = tmpRegister.split("_ ");
                                    System.out.println(tmpSplit.length);
                                    System.out.println(tmpSplit[0]);
                                    Main.out.append(var.getRegister() + " = dso_local global ["+ var.getX() + " x [" + var.getY() + " x i32]] ");
                                    for(int i = 0; i < tmpSplit.length; i++){
                                        if(tmpSplit[i].equals("empty")){
//                                            ArrayList<String> tmp = new ArrayList<>();
//                                            for(int j = 0; j < var.getY(); j++){
//                                                tmp.add("0");
//                                                System.out.println(tmp.get(j));
//                                            }
//                                            System.out.println("num = " + tmp.size());
                                            if(tmpSplit.length == 1){
                                                Main.out.append("zeroinitializer\n");
                                            }
                                            else{
                                                Main.out.append("[" + var.getY() + " x i32] zeroinitializer]\n");
                                            }
                                        }
                                        else{
                                            Main.out.append("[" + var.getY() + " x i32] [");
                                            String[] tmp = tmpSplit[i].split(" ");
                                            for(int j = 0; j < tmp.length; j++){
                                                System.out.println(tmp[j]);
                                                if(j == var.getY() - 1){
                                                    if(i == var.getX() - 1){
                                                        Main.out.append("i32 " + tmp[j] + "]]\n");
                                                    }
                                                    else{
                                                        Main.out.append("i32 " + tmp[j] + "], ");
                                                    }
                                                }
                                                else{
                                                    Main.out.append("i32 " + tmp[j] + ", ");
                                                }
                                            }
                                            for(int j = tmp.length; j < var.getY(); j++){
                                                if(j == var.getY() - 1){
                                                    Main.out.append("i32 " + 0 + "], ");
                                                }
                                                else{
                                                    Main.out.append("i32 " + 0 + ", ");
                                                }
                                            }
                                            System.out.println("num = " + tmp.length);
                                            System.out.println("=============");
                                        }

                                    }
                                    if(!tmpSplit[0].equals("empty")) {
                                        for (int i = tmpSplit.length; i < var.getX(); i++) {
                                            if (i == var.getX() - 1) {
                                                Main.out.append("[" + var.getY() + " x i32] zeroinitializer]\n");
                                            } else {
                                                Main.out.append("[" + var.getY() + " x i32] zeroinitializer], \n");
                                            }
                                        }
                                    }
                                }
                            }
                            else{
                                System.out.println("this global var has been defined1");
                                System.exit(1);
                            }
                        }
                        else{
                            Main.out.append("\t%ptr" + ptrNum++);
                            Var var = new Var("%ptr" + (ptrNum-1), name, false, -2,false,true,dimension);
                            varList.add(var);
                            if(tmpRegister.equals("empty_")){
                                tmpRegister = tmpRegister.substring(0,tmpRegister.length()-1);
                            }
                            else{
                                tmpRegister = tmpRegister.substring(0,tmpRegister.length()-2);
                            }
                            if(dimension == 1){
                                var.setX(1);
                                var.setY(Integer.valueOf(tmps.get(0)));
                                Main.out.append(" = alloca [" + var.getY() +" x i32]\n");
                                Main.out.append("\t%ptr" + ptrNum++ + " = getelementptr [" + var.getY() + " x i32], [" + var.getY() + " x i32]* " + var.getRegister() + ", i32 0, i32 0\n");
                                var.setPtr("%ptr"+(ptrNum-1));
                                StringBuilder dimen1 = new StringBuilder();
                                dimen1.append(tmpRegister);
                                if(tmpRegister.equals("empty")){
                                    dimen1.setLength(0);
                                    for(int i = 0; i < var.getY(); i++){
                                        dimen1.append(" 0");
                                    }
                                    dimen1.deleteCharAt(0);
                                    Main.out.append("\tcall void @memset(i32* " + var.getPtr() + ", i32 0, i32 " + var.getY()*4 + ")");
                                }
                                else{
                                    Main.out.append("\tcall void @memset(i32* " + var.getPtr() + ", i32 0, i32 " + var.getY()*4 + ")");
                                    String[] items = dimen1.toString().split(" ");
                                    for(int i = 0; i < items.length; i++){
//                                        if( i == 0){
//                                            Main.out.append("\tstore i32 " + items[i] + ", i32* " + var.getPtr() + "\n");
//                                        }
//                                        else{
                                        Main.out.append("\t%ptr" + ptrNum++ + " = getelementptr i32, i32* " + var.getPtr() + ", i32 " + i + "\n");
                                        var.getElems().add("%ptr"+(ptrNum-1));
                                        Main.out.append("\tstore i32 " + items[i] + ", i32* " + "%ptr" + (ptrNum - 1) + "\n");
                                    }
                                    for(int i = items.length; i < var.getY(); i++){
                                        var.getElems().add("0");
                                    }
                                }
                            }
                            else if(dimension == 2){
                                var.setX(Integer.valueOf(tmps.get(0)));
                                var.setY(Integer.valueOf(tmps.get(1)));
                                Main.out.append(" = alloca [" + var.getX() + " x [" + var.getY() +" x i32]]\n");
                                Main.out.append("\t%ptr" + ptrNum++ + " = getelementptr [" + var.getX() + " x [" + var.getY() + " x i32]], [" + var.getX() +" x [" + var.getY() + " x i32]]* " + var.getRegister() + ", i32 0, i32 0\n");
                                Main.out.append("\t%ptr" + ptrNum++ + " = getelementptr [" + var.getY() + " x i32], [" + var.getY() + " x i32]* %ptr" + (ptrNum-2) + ", i32 0, i32 0\n");
                                var.setPtr("%ptr"+(ptrNum-1));
                                Main.out.append("\tcall void @memset(i32* " + var.getPtr() + ", i32 0, i32 " + var.getX()*var.getY()*4 + ")\n");
                                String[] tmpSplit = tmpRegister.split("_ ");
                                for(int i = 0; i < tmpSplit.length; i++){
                                    String[] tmp = tmpSplit[i].split(" ");
                                    for(int j = 0; j < tmp.length; j++){
//                                        if(i == 0 && j == 0){
//                                            Main.out.append("\tstore i32 " + tmp[j] + ", i32* " + var.getPtr() + "\n");
//                                            var.getElems().add("%ptr"+(ptrNum-1));
//                                        }
//                                        else{
                                            Main.out.append("\t%ptr" + ptrNum++ + " = getelementptr i32, i32* " + var.getPtr() + ", i32 " + (var.getY()*i+j) + "\n");
                                            var.getElems().add("%ptr"+(ptrNum-1));
                                            Main.out.append("\tstore i32 " + tmp[j] + ", i32* " + "%ptr" + (ptrNum - 1) + "\n");
                                    }
                                    for(int j = tmp.length; j < var.getY(); j++){
                                        var.getElems().add("0");
                                    }
                                }
                            }
                        }
                    }
                    else{
                        if(inGlobal){
                            if(noSameGloabl(name) && !exist_var){
                                Var var = new Var("@" + name, name, false, -2,true,false,0);
                                var.setGlobal(true);
                                if(tmpRegister.substring(0,1).equals("%")){
                                    var.setValue(0);
                                }
                                else{
                                    var.setValue(Integer.valueOf(tmpRegister));
                                }
                                varList.add(var);
                                Main.out.append(var.getRegister() + " = dso_local global i32 " + var.getValue() + "\n");
                            }
                            else{
//                            for(int i = 0; i < varList.size(); i++){
//                                System.out.println(varList.get(i).getName() + " " + varList.get(i).isGlobal());
//                            }
//                            System.out.println(name);
                                System.out.println("this globalvar has been defined2");
                                System.exit(1);
                            }
                        }
                        else{
                            Var var = new Var("%x"+ varList.size(),name,false, curBlock,false,false,0);
                            varList.add(var);
                            Main.out.append("\t" + var.getRegister() + " = alloca i32\n");
                            Main.out.append("\tstore i32 " + tmpRegister +", i32* " + var.getRegister()+"\n");
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
                if(isArray){
                    if(inGlobal){
                        if(noSameGloabl(name)){
                            Var var = new Var("@" + name, name, false, -2,true,true,dimension);
                            varList.add(var);
                            if(dimension == 1){
                                int num = Integer.valueOf(tmps.get(0));
                                var.setY(num);
                                var.setX(1);
                                Main.out.append(var.getRegister() + " = dso_local global [" + var.getY() + " x i32] zeroinitializer\n");
                            }
                            else if(dimension == 2){
                                var.setX(Integer.valueOf(tmps.get(0)));
                                var.setY(Integer.valueOf(tmps.get(1)));
                                Main.out.append(var.getRegister() + " = dso_local global [" + var.getX() + " x [" + var.getY() +" x i32]] zeroinitializer\n");
                            }
                        }
                        else{
                            System.out.println("this globalvar has been defined3");
                            System.exit(1);
                        }
                    }
                }
                else{
                    if(inGlobal){
                        if(noSameGloabl(name)){
                            Var var = new Var("@" + name, name, false, -2,true,false,0);
                            var.setGlobal(true);
                            var.setValue(0);
                            varList.add(var);
                            Main.out.append("\t" + var.getRegister() + " = dso_local global i32 " + var.getValue() + "\n");
                        }
                        else{
                            System.out.println("this globalvar has been defined3");
                            System.exit(1);
                        }
                    }
                    else{
                        Var var = new Var("%x"+ varList.size(),name,false, curBlock,false,false,0);
                        varList.add(var);
                        Main.out.append("\t" + var.getRegister() + " = alloca i32\n");
                    }
                }
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
        int x = LVal();
        if(x != -1){
            if(x != -2){
                String name = Main.syms.get(x).getWord();
                if(getVarByName(name) == null){
                    for(int i = 0; i < varList.size(); i++){
                        System.out.println(varList.get(i).getName() + " " + varList.get(i).getBlockNum());
                    }
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
                    if(Exp() != null){
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
            }
            return true;
        }
        else if(match(7)){
            src--;
            int tmp = curBlock;
            Block();
            removeBlockVar();
            curBlock = tmp;
            return true;
        }
        else if(match(3)){
            String tmpRegister;
            if((tmpRegister = Exp()) != null){
                if(match(9)){
                    if(tmpRegister.length()>= 2 && tmpRegister.substring(0,2).equals("%x")){
                        Main.out.append("\t%" + reg++ +" = load i32, i32* " + tmpRegister +"\n");
                        exit = true;
                        Main.out.append("\tret i32 %" + (reg-1) + "\n\n");
                    }
                    else{
                        exit = true;
                        Main.out.append("\tret i32 " + tmpRegister + "\n\n");
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
        //if-else
        else if(match(20)){
            if(match(5)){
                String tmpCond = Cond();
                if(tmpCond != null){
                    if(match(6)){
                        boolean ifContinue = false;
                        boolean ifBreak = false;
                        initCond = true;
                        Main.out.append("\tbr i1 " + tmpCond + ", label %block" + bNum++ + ", label %block" + bNum++ +"\n\n");
                        Main.out.append("block" + (bNum-2) + ":\n");
                        curBlock = bNum - 2;
                        blockStack.push("%block" + (bNum - 2));
                        blockStack.push("%block" + (bNum - 1));
                        elseJump.push("block" + (bNum - 1));
                        if(Stmt()){
                            if(isContinue){
                                ifContinue = true;
                                isContinue = false;
                            }
                            if(isBreak){
                                ifBreak = true;
                                isBreak = false;
                            }
                            initCond = false;
                            removeBlockVar();
                            int tmpSize = Main.out.length();
                            endJump.push(tmpSize);
                            if(match(21)){
                                boolean elseContinue = false;
                                boolean elseBreak = false;
//                                Main.out.append(elseJump.pop() + ":\n");
                                String tmp = elseJump.pop();
                                Main.out.append(tmp+":\n");
                                initCond = true;
                                curBlock = Integer.valueOf(tmp.substring(5,tmp.length()));
                                if(Stmt()){
                                    if(isContinue){
                                        elseContinue = true;
                                        isContinue = false;
                                    }
                                    if(isBreak){
                                        elseBreak = true;
                                        isBreak = false;
                                    }
                                    removeBlockVar();
                                    initCond = false;
                                    endJump.push(Main.out.length());
                                    Main.out.append("block" + bNum++ + ":\n");
                                    curBlock = bNum - 1;
                                    blockStack.push("%block" + (bNum - 1));
                                    if(!elseContinue && !elseBreak){
                                        Main.out.insert(endJump.pop(),"\tbr label %block" + (bNum - 1) + "\n\n");
                                    }
                                    else{
                                        endJump.pop();
//                                        if(elseContinue){
//                                            elseContinue = false;
//                                        }
//                                        if(elseBreak){
//                                            elseBreak = false;
//                                        }
                                    }
                                    if(!ifContinue && !ifBreak){
                                        if(!exit){
                                            Main.out.insert(endJump.pop(),"\tbr label %block" + (bNum - 1) + "\n\n");
                                        }
                                        else{
                                            exit = false;
                                        }
                                    }
                                    else{
                                        endJump.pop();
//                                        if(ifContinue){
//                                            ifContinue = false;
//                                        }
//                                        if(ifBreak){
//                                            ifBreak = false;
//                                        }
                                    }
                                    return true;
                                }
                                else{
                                    System.out.println("循环出错");
                                    System.exit(1);
                                    return false;
                                }
                            }
                            else{
                                String tmp = elseJump.pop();
                                Main.out.append(tmp+":\n\n");
                                curBlock = Integer.valueOf(tmp.substring(5,tmp.length()));
                                int t = endJump.pop();
                                if(ifContinue || ifBreak){
                                    ;
//                                    if(ifBreak){
//                                        ifBreak = false;
//                                    }
//                                    if(ifContinue){
//                                        ifContinue = false;
//                                    }
                                }
                                else{
                                    Main.out.insert(t,"\tbr label %" + tmp + "\n\n");
                                }
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
        //while循环
        else if(match(31)){
            Main.out.append("\tbr label %block" + bNum++ + "\n");
            Main.out.append("block" + (bNum-1) + ":\n");
            continueJump.push(bNum-1);
            whileJump.push((bNum-1));
            if(match(5)){
                String tmpCond = Cond();
                if(tmpCond != null){
                    if(match(6)){
                        Main.out.append("\tbr i1 " + tmpCond + ", label %block" + bNum++ + ", label %block" + bNum++ +"\n\n");
                        elseJump.push("block"+(bNum-1));
                        breakJump.push("block"+(bNum-1));
                        Main.out.append("block" + (bNum - 2) + ":\n");
                        if(Stmt()){
                            if(!isContinue && !isBreak){
                                Main.out.append("\tbr label %block" + whileJump.pop() + "\n");
                            }
                            else{
                                if(isContinue){
                                    isContinue = false;
                                }
                                if(isBreak){
                                    isBreak = false;
                                }
                            }
                            if(breakJump.size() != 0){
                                breakJump.pop();
                            }
                            if(continueJump.size() != 0){
                                continueJump.pop();
                            }
                            Main.out.append(elseJump.pop() + ":\n");
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
        //break
        else if(match(32)){
            if(match(9)){
                isBreak = true;
                Main.out.append("\tbr label %" + breakJump.peek() + "\n");
                return true;
            }
            else{
                src = id;
                return false;
            }
        }
        //continue
        else if(match(33)){
            if(match(9)){
                Main.out.append("\tbr label %block" + continueJump.peek() + "\n");
                isContinue = true;
                return true;
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
            if(!initCond){
                bNum++;
                curBlock = bNum -1;
            }
            else{
                initCond = false;
            }
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
                for(int i = 0; i < varList.size(); i++){
                    System.out.println(varList.get(i).getName());
                }
                System.out.println("80001");
                System.out.println(Main.syms.get(src).getWord());
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
                Main.out.append("define dso_local i32 @main(){\n");
                inGlobal = false;
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
            System.out.println(Main.syms.get(src-1).getWord());
            System.out.println(Main.syms.get(src).getWord());
            System.out.println("100004");
            System.exit(1);
        }
    }

    public static String Cond(){
        if(LOrExp()){
            if(condStack.size() == 0){
                String tmpString = tmpStack.pop();
                Main.out.append("\t%" + reg++ + " = sub i32 " + tmpString +", 0\n");
                Main.out.append("\t%cond" + condNum++ + " = icmp sgt i32 "+ "%" + (reg-1) +", 0\n");
                return "%cond" + (condNum - 1);
            }
            else{
                return condStack.pop();
            }
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
            if(LAndExp()){
                String b = condStack.pop();
                String a = condStack.pop();
                Main.out.append("\t%cond" + condNum++ + " = or i1 " + a +", " + b +"\n");
                condStack.push("%cond"+(condNum-1));
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
            if(varList.get(i).getName().equals(name) && varList.get(i).getBlockNum() == curBlock){
                return true;
            }
        }
        for (int i = 0; i < varList.size(); i++){
            if(varList.get(i).getName().equals(name) && varList.get(i).getBlockNum() != -1){
                return true;
            }
        }
        return false;
    }

    public static boolean inBlockVarList(String name){
        for (int i = 0; i < varList.size(); i++){
            if(varList.get(i).getName().equals(name) && varList.get(i).getBlockNum() == curBlock){
                return true;
            }
        }
        return false;
    }

    public static Var getVarByName(String name){
        for(int i = 0; i < varList.size(); i++){
            if(varList.get(i).getName().equals(name) && varList.get(i).getBlockNum() == curBlock){
                return varList.get(i);
            }
        }
        for(int i = varList.size() - 1; i >= 0; i--){
            if(varList.get(i).getName().equals(name) && varList.get(i).getBlockNum() != -1){
                return varList.get(i);
            }
        }
        return null;
    }

    public static void removeBlockVar(){
        for(int i = 0; i < varList.size(); i++){
            if(varList.get(i).getBlockNum() == curBlock){
                varList.get(i).setBlockNum(-1);
            }
        }
    }

    public static boolean noSameGloabl(String name){
        for(int i = 0; i < varList.size(); i++){
            Var var = varList.get(i);
            if(var.getName().equals(name) && var.isGlobal){
                return false;
            }
        }
        return true;
    }

}
