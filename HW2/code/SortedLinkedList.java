public class SortedLinkedList<T extends Comparable<T>> extends MyLinkedList<T> implements ListInterface<T> {
    public SortedLinkedList(T headItem) {
        super(new ComparableNode<T>(headItem));
    }

    public SortedLinkedList() {
        this(null);
    }

    @Override
    public void add(T item) {
        ComparableNode<T> curr = (ComparableNode<T>) head,
                next = (ComparableNode<T>) curr.getNext();

        ComparableNode<T> newNode = new ComparableNode<T>(item);

        // 다음 노드가 존재하고, 다음 노드의 item보다 item이 크거나 같을 경우 계속 찾음
        while (next != null && newNode.compareTo(next) >= 0) {
            // 이미 같은 item 등록되어 있을 경우 등록하지 않음
            if(newNode.equals(next))
                return;

            curr = next; next = (ComparableNode<T>) curr.getNext();
        }

        curr.setNext(newNode);
        newNode.setNext(next);

        numItems++;
    }
}
