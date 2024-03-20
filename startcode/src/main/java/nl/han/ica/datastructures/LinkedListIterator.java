package nl.han.ica.datastructures;

public class LinkedListIterator<T> {
    ListNode<T> current;
    LinkedListIterator(ListNode<T> currentNode){
        current = currentNode;
    }

    public boolean isValid(){
        return current != null;
    }

    public T retrieve(){
        return isValid() ? current.value : null;
    }

    public void advance(){
        if(isValid()){
            current = current.nextValue;
        }
    }
}
