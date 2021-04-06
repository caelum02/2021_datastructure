public class ComparableNode<T extends Comparable<T>> extends Node<T> implements Comparable<Node<T>> {
    public ComparableNode(T item) {
        super(item);
    }

    @Override
    public int compareTo(Node<T> node) {
        return getItem().compareTo(node.getItem());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ComparableNode))
            return false;

        ComparableNode<T> other = (ComparableNode<T>) o;
        return getItem().equals(other.getItem());
    }

    @Override
    public int hashCode() {
        return getItem().hashCode();

    }
}
