

import java.util.ArrayList;

public class FuncVar {
    String funcName;
    String type;
    String name;
    ArrayList<FuncParam> funcParams = new ArrayList<>();

    public ArrayList<FuncParam> getFuncParams() {
        return funcParams;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFuncParams(ArrayList<FuncParam> funcParams) {
        this.funcParams = funcParams;
    }

    public FuncVar(String funcName, String type){
        this.funcName = funcName;
        this.type = type;
    }

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
