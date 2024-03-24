package nl.han.ica.datastructures;

public class LinkedListIterator<ASTNode> {
    ListNode current;
    LinkedListIterator(ListNode currentNode){
        current = currentNode;
    }

    public boolean isValid(){
        return current != null;
    }

    public Object retrieve(){
        return isValid() ? current.value : null;
    }

    public void advance(){
        if(isValid()){
            current = current.nextValue;
        }
    }
}
