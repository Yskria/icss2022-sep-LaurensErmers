package nl.han.ica.datastructures;

public class HANLinkedList<ASTNode> implements IHANLinkedList<ASTNode>{

    private ListNode<ASTNode> beginNode = null;

    @Override
    public void addFirst(ASTNode value) {
        if(beginNode == null){
            beginNode = new ListNode<>((ASTNode) value);
        } else {
            ListNode<ASTNode> newTreeNode = new ListNode<>(value);
            newTreeNode.nextValue = beginNode;
            beginNode = newTreeNode;
        }
    }

    @Override
    public void clear() {
        beginNode = null;
    }

    @Override
    public void insert(int index, ASTNode value) {
        ListNode<ASTNode> addNode = new ListNode<ASTNode>();
        if(beginNode.value == null){
            System.out.println("index node cannot be null");
        } else {
            addNode.value = value;
        }
    }

    @Override
    public void delete(int pos) {
        LinkedListIterator<ASTNode> p = findPreviousNode(pos);
        if(p.current.nextValue != null){
            p.current.nextValue = p.current.nextValue.nextValue;
        }
    }

    public LinkedListIterator<ASTNode> findPreviousNode(int pos){
        ListNode<ASTNode> itr = beginNode;

        while(itr.nextValue != null && itr.nextValue.value.equals(pos)){
            itr = itr.nextValue;
        }
        return new LinkedListIterator<ASTNode>(itr);
    }

    @Override
    public ASTNode get(int pos) {
        if(pos > getSize()){
            throw new RuntimeException("out of bounds");
        }
        int count = 0;
        ListNode<ASTNode> llitr = beginNode;

        while (llitr != null && count != pos){
            llitr = llitr.nextValue;
            count++;
        }
        return llitr.value;
    }

    @Override
    public void removeFirst() {
        if (beginNode != null) {
            beginNode = beginNode.nextValue;
        }
    }

    @Override
    public ASTNode getFirst() {
        return beginNode.value;
    }

    @Override
    public int getSize() {
        ListNode<ASTNode> current = beginNode;
        int size = 0;
        while (current != null) {
            size++;
            current = current.nextValue;
        }
        return size;
    }
}
