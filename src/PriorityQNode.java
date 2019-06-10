public class PriorityQNode {
    private int data;
    private PriorityQNode next;
    private int priority;
    private Cell coreCell;
    PriorityQNode(int data, int priority) {
        this.data = data;
        this.priority = priority;
    }
    PriorityQNode(Cell coreCell, int priority){
        this.coreCell = coreCell;
        this.priority = priority;
    }
    void setPriority(int priority){
        this.priority = priority;
    }

    public Cell getCoreCell() {
        return coreCell;
    }

    public int getData() {
        return data;
    }

    public int getPriority() {
        return priority;
    }

    public void setData(int data) {
        this.data = data;
    }

    public void setNext(PriorityQNode next) {
        this.next = next;
    }

    public void setCoreCell(Cell coreCell) {
        this.coreCell = coreCell;
    }

    public PriorityQNode getNext() {
        return next;
    }
}
