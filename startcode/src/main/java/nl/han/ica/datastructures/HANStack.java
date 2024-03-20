package nl.han.ica.datastructures;

public class HANStack<T> implements IHANStack<T>{

    ListNode<T> topOfStack;

    @Override
    public void push(T value) {
    topOfStack = new ListNode<T>(value, topOfStack);
    }

    @Override
    public T pop() {
        T popItem = topOfStack.value;
        topOfStack = topOfStack.nextValue;
        return popItem;
    }

    @Override
    public T peek() {
        return topOfStack.value;
    }
}
