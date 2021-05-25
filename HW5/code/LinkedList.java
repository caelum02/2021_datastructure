import java.util.Iterator;

public class LinkedList<V> implements Iterable<V> {
    public LinkedListNode<V> head, tail, NIL;
    public int size;

    LinkedList (V headValue) {
        NIL = new LinkedListNode<>(null, null);
        NIL.next = NIL;
        tail = head = new LinkedListNode<>(headValue, NIL);

        size = 0;
    }

    LinkedList () {
        this(null);
    }

    public void append(V value) {
        LinkedListNode<V> newTail = new LinkedListNode<>(value, NIL);
        tail.next = newTail;
        tail = newTail;
        size++;
    }

    public V remove(int i) {
        LinkedListNode<V> node = head;

        while (i >= 0 && node != NIL) {
            node = node.next;
            i--;
        }

        return node.value;
    }

    public V get(int i) {
        LinkedListNode<V> node = head;

        while(i >= 0 && node != NIL) {
            node = node.next;
            i--;
        }

        return node.value;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        head.next = NIL;
        tail = head;
        size = 0;
    }

    @Override
    public Iterator<V> iterator() {
        return new LinkedListIterator<>(this);
    }

    @Override
    public String toString() {
        String str = "";
        for (V s : this) {
            str = str.concat(s.toString());
            str += " ";
        }

        return str.substring(0, str.length()-1);
    }
}
