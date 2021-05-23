public class BinaryTreeNode<E> extends Node<E> {
    public BinaryTreeNode<E> left, right;

    BinaryTreeNode (E item, BinaryTreeNode<E> left, BinaryTreeNode<E> right) {
        super(item);
        this.left = left;
        this.right = right;
    }

    BinaryTreeNode (E item) {
        this(item, null, null);
    }

    BinaryTreeNode () {
        this(null);
    }
}
