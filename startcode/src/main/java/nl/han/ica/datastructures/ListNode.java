package nl.han.ica.datastructures;

public class ListNode<T> {
    public T value;
    public ListNode nextValue;

    public ListNode(T value, ListNode nextValue){
        this.value = value;
        this.nextValue = nextValue;
    }

    public ListNode(T value){
        this.value = value;
    }

    public ListNode(){
    }
}
