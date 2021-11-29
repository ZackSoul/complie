

import java.util.ArrayList;

public class Var {
    String register;
    String name;
    boolean isConst;
    int blockNum;
    boolean isGlobal = false;
    int value = 0;
    int x = 1;
    int y = 1;
    String ptr;
    ArrayList<String> elems = new ArrayList<>();

    public ArrayList<String> getElems() {
        return elems;
    }

    public void setElems(ArrayList<String> elems) {
        this.elems = elems;
    }

    public String getPtr() {
        return ptr;
    }

    public void setPtr(String ptr) {
        this.ptr = ptr;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


    public ArrayList<String> getArr1() {
        return arr1;
    }

    public void setArr1(ArrayList<String> arr1) {
        this.arr1 = arr1;
    }

    public ArrayList<String> getArr2() {
        return arr2;
    }

    public void setArr2(ArrayList<String> arr2) {
        this.arr2 = arr2;
    }

    public ArrayList<String> arr1 = new ArrayList<>();
    public ArrayList<String> arr2 = new ArrayList<>();

    public boolean isArray() {
        return isArray;
    }

    public void setArray(boolean array) {
        isArray = array;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    boolean isArray = false;
    int dimension = 0;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public void setGlobal(boolean global) {
        isGlobal = global;
    }

    public int getBlockNum() {
        return blockNum;
    }

    public void setBlockNum(int blockNum) {
        this.blockNum = blockNum;
    }


    public Var(String register, String name, boolean isConst, int blockNum, boolean isGlobal, boolean isArray, int dimension){
        this.register = register;
        this.name = name;
        this.isConst = isConst;
        this.blockNum = blockNum;
        this.isGlobal = isGlobal;
        this.isArray = isArray;
        this.dimension = dimension;
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
