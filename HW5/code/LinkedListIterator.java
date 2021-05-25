import java.util.Iterator;

public class LinkedListIterator<V> implements Iterator<V> {
    private final LinkedList<V> list;
    private LinkedListNode<V> curr;

    public LinkedListIterator(LinkedList<V> list) {
        this.list = list;
        curr = list.head;
    }

    @Override
    public boolean hasNext() {
        return curr != list.tail;
    }

    @Override
    public V next() {
        curr = curr.next;
        return curr.value;
    }

    public V get() {
        return curr.value;
    }
}
