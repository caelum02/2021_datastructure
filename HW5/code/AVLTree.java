public class AVLTree<K extends Comparable<K>, V> {
    private final AVLNode<K, V> NIL = new AVLNode<>(null, null, 0);
    private AVLNode<K, V> root;

    AVLTree() {
        root = NIL;
    }

    public V search(K key) {
        AVLNode<K, V> node = getNode(key);
        return node.value;
    }

    public void insert(K key, V value) {
        root = insert(root, key, value);
    }

    public AVLNode<K, V> insert(AVLNode<K, V> node, K key, V value) {
        if (node == NIL)
            return new AVLNode<>(key, value, NIL, NIL);

        if(key.equals(node.key)) {
            node.value = value;
            return node;
        }

        if (key.compareTo(node.key) < 0)
            node.setLeft(insert(node.left, key, value));

        else if (key.compareTo(node.key) > 0)
            node.setRight(insert(node.right, key, value));

        if (needBalance(node))
            node = balanceSubtree(node);

        return node;
    }

    private AVLNode<K, V> balanceSubtree(AVLNode<K, V> node) {
        int delta = node.delta;
        if (needBalance(node.left))
            node.setLeft(balanceSubtree(node.left));
        if (needBalance(node.right))
            node.setRight(balanceSubtree(node.right));

        if (needBalance(node)) {
            if (delta > 1) {
                if (node.left.delta < 0)
                    node.setLeft(leftRotation(node.left));
                node = rightRotation(node);
            }

            else  { // delta < -1
                if (node.right.delta > 0)
                    node.setRight(rightRotation(node.right));
                node = leftRotation(node);
            }
        }

        return node;
    }

    private boolean needBalance(AVLNode<K, V> node) {
        int delta = node.delta;
        return -1 > delta || delta > 1;
    }

    private AVLNode<K, V> getNode(K key) {
        AVLNode<K, V> node = root;
        while (node != NIL) {
            if (key.compareTo(node.key) < 0)
                node = node.left;
            else if (key.compareTo(node.key) > 0)
                node = node.right;
            else break;
        }

        return node;
    }

    private AVLNode<K, V> rightRotation(AVLNode<K, V> node) {
        AVLNode<K, V> newRoot = node.left;
        node.setLeft(newRoot.right);
        newRoot.setRight(node);

        return newRoot;
    }

    private AVLNode<K, V> leftRotation(AVLNode<K, V> node) {
        AVLNode<K, V> newRoot = node.right;
        node.setRight(newRoot.left);
        newRoot.setLeft(node);

        return newRoot;
    }
}

