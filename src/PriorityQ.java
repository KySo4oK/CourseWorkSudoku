public class PriorityQ {
    private PriorityQNode back = null;
    private PriorityQNode front = null;
    private int num = 0;

    PriorityQ(int n) {
        for(int i=0; i<n; i++){
            PriorityQNode temp = new PriorityQNode((int)(Math.random()*10+1),(int)(Math.random()*10+1));
            push(temp);
        }
    }

    PriorityQ(){}

    void push(PriorityQNode a) {
        if(isEmpty()) {
            back = a;
            front = a;
            a.setNext(null);
        } else {
            PriorityQNode temp = back;
            while (temp!=null) {
                if(back.getPriority()<=a.getPriority()){
                    a.setNext(back);
                    back = a;
                    break;
                }
                if(a.getPriority()<front.getPriority()){
                    front.setNext(a);
                    front = a;
                    a.setNext(null);
                    break;
                }
                if(a.getPriority()<temp.getPriority() && a.getPriority()>=temp.getNext().getPriority()) {
                    a.setNext(temp.getNext());
                    temp.setNext(a);
                    break;
                }
                temp = temp.getNext();
            }
        }
        num++;

    }

    boolean isEmpty() {
        return num==0;
    }

    void clear(){
        back = null;
        front = null;
        num = 0;
    }

    PriorityQNode pop() {
        PriorityQNode temp = front;
        PriorityQNode t = back;
        while (t.getNext()!=null){
            if(t.getNext()==front) {
                front = t;
                front.setNext(null);
                break;
            }
            t = t.getNext();
        }
        num--;
        return temp;
    }

    void print() {
        PriorityQNode temp = back;
        while (temp != null) {
            System.out.print(temp.getData() + "(" + temp.getPriority() + ") ");
            temp = temp.getNext();
        }
        System.out.println("");
    }

    int size() { return num; }




}
