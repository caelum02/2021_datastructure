public class Pair<T, U> {
    T first;
    U second;

    Pair (T first, U second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return "(" + first.toString() + ", " + second.toString() + ")";
    }
}
