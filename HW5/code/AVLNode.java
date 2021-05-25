public class AVLNode<K extends Comparable<K>, V> extends Node<V> {
    public AVLNode<K, V> left, right;
    public final K key;
    public int height, delta;

    AVLNode (K key, V item, int height) {
        super(item);
        this.key = key;
        this.height = height;
        this.left = this.right = null;
    }

    AVLNode (K key, V item, AVLNode<K, V> left, AVLNode<K, V> right) {
        super(item);
        this.key = key;
        this.left = left;
        this.right = right;
        updateHeight();
    }

    public void setLeft(AVLNode<K, V> node) {
        left = node;
        updateHeight();
    }

    public void setRight(AVLNode<K, V> node) {
        right = node;
        updateHeight();
    }

    private void updateHeight() {
        height = Integer.max(left.height, right.height) + 1;
        delta = left.height - right.height;
    }
}