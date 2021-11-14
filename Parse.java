
public class Parse {
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

    public static boolean ConstInitval(){
        int id = src;
        if(ConstExp()){
            return true;
        }
        else{
            src = id;
            return false;
        }
    }

    public static boolean Number(){
        int id = src;
        if(match(4)){
            Main.stack.push(new Register(0,Integer.valueOf(Main.syms.get(src-1).getWord())));
            return true;
        }
        else{
            src = id;
            return false;
        }
    }

    public static boolean Exp(){
        int id = src;
        if(AddExp()){
            return true;
        }
        else{
            src = id;
            return false;
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
            if(Exp()){
                if(match(6)){
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
            return true;
        }
        else if(Number()){
            return true;
        }
        else{
            src = id;
            return false;
        }
    }

    public static boolean FuncRParams(){
        int id = src;
        if(Exp()){
            while(true){
                if(match(16)){
                    if(Exp()){
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

    public static boolean UnaryExp(){
        int id = src;
        if(FunctionIdent()){
            if(match(5)){
                if(FuncRParams()){
                    if(match(6)){
                        return true;
                    }
                    else{
                        src = id;
                        return false;
                    }
                }
                else if(match(6)){
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
        else if(UnaryOp()){
            if(UnaryExp()){
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

    public static boolean UnaryOp(){
        int id = src;
        if(match(10) || match(11)){
            return true;
        }
        else{
            src = id;
            return false;
        }
    }

    public static boolean MulExp0(){
        int id = src;
        if(match(12)){
            if(UnaryExp()){
                Integer b = Main.stack.pop().getNum();
                Integer a = Main.stack.pop().getNum();
                Register register = new Register(Main.regIndex++,a * b);
                Main.out.append("\t%" + register.getId() + " = mul i32 " + a + ", " + b + "\n");
                Main.stack.push(register);
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
                Integer b = Main.stack.pop().getNum();
                Integer a = Main.stack.pop().getNum();
                Register register = new Register(Main.regIndex++,a / b);
                Main.out.append("\t%" + register.getId() + " = sdiv i32 " + a + ", " + b + "\n");
                Main.stack.push(register);
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
                Integer b = Main.stack.pop().getNum();
                Integer a = Main.stack.pop().getNum();
                Register register = new Register(Main.regIndex++,a % b);
                Main.out.append("\t%" + register.getId() + " = srem i32 " + a + ", " + b + "\n");
                Main.stack.push(register);
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

    public static boolean AddExp(){
        int id = src;
        if(MulExp()){
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
    public static boolean AddExp0(){
        int id = src;
        if(match(10)){
            if(MulExp()){
                Integer b = Main.stack.pop().getNum();
                Integer a = Main.stack.pop().getNum();
                Register register = new Register(Main.regIndex++,a-b);
                Main.out.append("\t%" + register.getId() + " = sub i32 " + a + ", " + b + "\n");
                Main.stack.push(register);
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
                Integer b = Main.stack.pop().getNum();
                Integer a = Main.stack.pop().getNum();
                Register register = new Register(Main.regIndex++,a+b);
                Main.out.append("\t%" + register.getId() + " = add i32 " + a + ", " + b + "\n");
                Main.stack.push(register);
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

    public static boolean ConstExp(){
        int id = src;
        if(AddExp()){
            return true;
        }
        else{
            src = id;
            return false;
        }
    }

    public static boolean ConstDef(){
        int id = src;
        if(Ident()){
            Main.out.append("\t%" + Main.regIndex++ + " = alloca i32\n");
            if(match(18)){
                if(ConstInitval()){
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

    public static boolean InitVal(){
        int id = src;
        if(Exp()){
            return true;
        }
        else{
            src = id;
            return false;
        }
    }

    public static boolean VarDef(){
        int id = src;
        if(Ident()){
            Main.out.append("\t%" + Main.regIndex++ + " = alloca i32\n");
            if(match(18)){
                if(InitVal()){
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
            if(match(18)){
                if(Exp()){
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
        else if(match(3)){
            if(Exp()){
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
        else if(Exp()){
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

}
