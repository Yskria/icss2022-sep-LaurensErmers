package nl.han.ica.datastructures;

public class HANStack<ASTNode> implements IHANStack<ASTNode>{

    ListNode<ASTNode> topOfStack;

    @Override
    public void push(ASTNode value) {
    topOfStack = new ListNode<ASTNode>(value, topOfStack);
    }

    @Override
    public ASTNode pop() {
        ASTNode popItem = topOfStack.value;
        topOfStack = topOfStack.nextValue;
        return popItem;
    }

    @Override
    public ASTNode peek() {
        return topOfStack.value;
    }
}
