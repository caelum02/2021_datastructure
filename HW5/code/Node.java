public abstract class Node<V> {
    public V value;

    Node (V value) {
        this.value = value;
    }

    Node () {
        this(null);
    }
}


