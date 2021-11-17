

public class Var {
    String register;
    String name;
    boolean isConst;

    public Var(String register,String name, boolean isConst){
        this.register = register;
        this.name = name;
        this.isConst = isConst;
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
