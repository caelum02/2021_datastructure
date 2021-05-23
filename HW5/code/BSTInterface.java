public interface BSTInterface<K extends Comparable<K>, V> {
    public V search(K key);
    public V delete(K key);
    public void add(K key, V value);
}
