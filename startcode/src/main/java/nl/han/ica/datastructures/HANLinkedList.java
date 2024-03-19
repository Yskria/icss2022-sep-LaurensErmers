package nl.han.ica.datastructures;

import java.util.LinkedList;

public class HANLinkedList<T> implements IHANLinkedList<T>{

    private ListNode<T> beginNode;

    @Override
    public void addFirst(T value) {
        if(beginNode == null){
            beginNode = new ListNode<>((T) value);
        } else {
            ListNode<T> newTreeNode = new ListNode<>();
            newTreeNode.value = value;
            beginNode = newTreeNode;
        }
    }

    @Override
    public void clear() {
        beginNode = null;
    }

    @Override
    public void insert(int index, T value) {
        ListNode<T> addNode = new ListNode<T>();
        addNode.value = value;
        //??????
    }

    @Override
    public void delete(int pos) {
        if(beginNode != null){
            //??????
        }
    }

    @Override
    public T get(int pos) {
        return null;
    }

    @Override
    public void removeFirst() {
        beginNode = beginNode.nextValue;
    }

    @Override
    public T getFirst() {
        return beginNode.value;
    }

    @Override
    public int getSize() {
        return 0;
        //??????
    }
}
