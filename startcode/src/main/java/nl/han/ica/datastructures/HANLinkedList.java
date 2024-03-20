package nl.han.ica.datastructures;

public class HANLinkedList<ASTNode> implements IHANLinkedList<ASTNode>{

    private ListNode<ASTNode> beginNode = null;

    @Override
    public void addFirst(ASTNode value) {
        if(beginNode == null){
            beginNode = new ListNode<>((ASTNode) value);
        } else {
            ListNode<ASTNode> newTreeNode = new ListNode<>();
            newTreeNode.value = value;
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
        ListNode<ASTNode> llitr = beginNode.nextValue;

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
    public ASTNode getFirst() {
        return beginNode.value;
    }

    @Override
    public int getSize() {
        return 0;
        //??????
    }
}
