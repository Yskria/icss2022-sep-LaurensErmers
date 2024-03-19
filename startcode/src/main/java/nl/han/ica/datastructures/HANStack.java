package nl.han.ica.datastructures;

public class HANStack<T> implements IHANStack{

    ListNode<T> topOfStack;

    @Override
    public void push(Object value) {
    topOfStack = new ListNode<T>((T) value, topOfStack);
    }

    @Override
    public Object pop() {
        T popItem = topOfStack.value;
        topOfStack = topOfStack.nextValue;
        return popItem;
    }

    @Override
    public Object peek() {
        return topOfStack.value;
    }
}
