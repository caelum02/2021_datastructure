public interface Dictionary<K extends Comparable<K>, V> {
    public V search(K key);
    public void insert(K key, V value);
}
