import java.lang.reflect.Array;

public class ChainedHashTable<K extends Comparable<K>, V> implements Dictionary<K, V> {
    public final AVLTree<K, V>[] buckets;
    private final int numBuckets;

    ChainedHashTable(int numBuckets, Class<AVLTree<K, V>> clazz) {
        buckets = (AVLTree<K, V>[]) Array.newInstance(clazz, numBuckets);
    for (int i=0; i<numBuckets; i++)
            buckets[i] = new AVLTree<>();

        this.numBuckets = numBuckets;
    }

    @Override
    public V search(K key) {
        AVLTree<K, V> bucket = getBucket(key);
        return bucket.search(key);
    }

    public AVLTree<K, V> getBucket(K key) {
        return buckets[key.hashCode() % numBuckets];
    }

    @Override
    public void insert(K key, V value) {
        AVLTree<K, V> bucket = getBucket(key);
        bucket.insert(key, value);
    }
}
