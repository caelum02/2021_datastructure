public class Location extends Pair<Integer, Integer> implements Comparable<Location>{
    Location(Integer first, Integer second) {
        super(first, second);
    }

    @Override
    public int compareTo(Location location) {
        return first.equals(location.first) ? second.compareTo(location.second) : first.compareTo(location.first);
    }

    public boolean equals(Location location) {
//        System.out.println(this + " " + location);
        return first.equals(location.first) && second.equals(location.second);
    }

    public Location add(int i) {
        return new Location(first, second + i);
    }
}
