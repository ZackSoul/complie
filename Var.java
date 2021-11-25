
public class Var {
    String register;
    String name;
    boolean isConst;
    int blockNum;

    public int getBlockNum() {
        return blockNum;
    }

    public void setBlockNum(int blockNum) {
        this.blockNum = blockNum;
    }

    public Var(String register, String name, boolean isConst, int blockNum){
        this.register = register;
        this.name = name;
        this.isConst = isConst;
        this.blockNum = blockNum;
    }

    public String getRegister() {
        return register;
    }

    public void setRegister(String register) {
        this.register = register;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isConst() {
        return isConst;
    }

    public void setConst(boolean aConst) {
        isConst = aConst;
    }
}
