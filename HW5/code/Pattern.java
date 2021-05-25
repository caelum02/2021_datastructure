public class Pattern implements Comparable<Pattern>{
    String pattern;

    Pattern (String pattern) {
        this.pattern = pattern;
    }

    @Override
    public int hashCode() {
        int sum = 0;
        for (int i=0; i<6; i++) {
            sum += pattern.charAt(i);
        }

        return sum;
    }

    @Override
    public int compareTo(Pattern pattern) {
        return this.pattern.compareTo(pattern.pattern);
    }

    @Override
    public String toString() {
        return pattern;
    }
}
