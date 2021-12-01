

public class FuncParam {
    String type;
    String reg;
    String name;
    int Y = 1;

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FuncParam(String type, String reg, String name) {
        this.type = type;
        this.reg = reg;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }
}


