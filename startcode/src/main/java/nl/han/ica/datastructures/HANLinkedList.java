package nl.han.ica.datastructures;

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
        if(beginNode.value == null){
            System.out.println("index node cannot be null");
        } else {
            addNode.value = value;
        }
    }

    @Override
    public void delete(int pos) {
        LinkedListIterator<T> p = findPreviousNode(pos);
        if(p.current.nextValue != null){
            p.current.nextValue = p.current.nextValue.nextValue;
        }
    }

    public LinkedListIterator<T> findPreviousNode(int pos){
        ListNode<T> itr = beginNode;

        while(itr.nextValue != null && itr.nextValue.value.equals(pos)){
            itr = itr.nextValue;
        }
        return new LinkedListIterator<T>(itr);
    }

    @Override
    public T get(int pos) {
        ListNode<T> llitr = beginNode.nextValue;

        while (llitr != null && !llitr.value.equals(pos)){
            llitr = llitr.nextValue;
        }
        return llitr.value;
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
