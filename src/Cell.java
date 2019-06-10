public class Cell {
    private int value;
    private boolean isConst = true;
    private PriorityQNode itemPQN;
    Cell(int value){
        this.value = value;
    }
    Cell(){}
    public void setConst(boolean isConst){
        this.isConst = isConst;
    }
    public boolean getConst(){
        return isConst;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setItemPQN(PriorityQNode itemPQN) {
        this.itemPQN = itemPQN;
    }

    public PriorityQNode getItemPQN() {
        return itemPQN;
    }

}
