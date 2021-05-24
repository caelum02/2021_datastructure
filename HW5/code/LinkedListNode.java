public class LinkedListNode<V> extends Node<V> {
    public LinkedListNode<V> next;

    LinkedListNode (V value, LinkedListNode<V> next) {
        super(value);
        this.next = next;
    }

    LinkedListNode (V value) {
        this(value, null);
    }
}
