package nl.han.ica.datastructures;

public class LinkedListIterator<ASTNode> {
    ListNode<ASTNode> current;
    LinkedListIterator(ListNode<ASTNode> currentNode){
        current = currentNode;
    }

    public boolean isValid(){
        return current != null;
    }

    public ASTNode retrieve(){
        return isValid() ? current.value : null;
    }

    public void advance(){
        if(isValid()){
            current = current.nextValue;
        }
    }
}
