import java.lang.reflect.Array;

public class PatternDB {
    ChainedHashTable<Pattern, LinkedList<Location>> hashTable;

    PatternDB () {
        hashTable = new ChainedHashTable<>(100, (Class<AVLTree<Pattern, LinkedList<Location>>>) (new AVLTree<Pattern, LinkedList<Location>>()).getClass());
    }

    public void save(Pattern pattern, Location location) {
        LinkedList<Location> locations = hashTable.search(pattern);

        if (locations == null) {
            locations = new LinkedList<>();
            hashTable.insert(pattern, locations);
        }

        locations.append(location);
    }

    public void printSlot(int index) {
        System.out.println(hashTable.buckets[index]);
    }

    public LinkedList<Location> search(String pattern) {
        int numPatterns = (pattern.length() - 1) / 6 + 1, lastStride = (pattern.length() - 1) % 6 + 1;

        LinkedList<Location>[] patternLocs = (LinkedList<Location>[]) Array.newInstance(LinkedList.class, numPatterns);
        for (int i=0; i<numPatterns-1; i++) {
            LinkedList<Location> searchResult = hashTable.search(new Pattern(pattern.substring(i*6, (i+1)*6)));
            if (searchResult == null)
                return new LinkedList<>();
            patternLocs[i] = searchResult;
        }
        LinkedList<Location> searchResult = hashTable.search(new Pattern(pattern.substring(pattern.length()-6)));
        if (searchResult == null)
            return new LinkedList<>();
        patternLocs[numPatterns-1] = searchResult;

        LinkedList<Location> locations = new LinkedList<>();

        LinkedListIterator<Location>[] iters = new LinkedListIterator[numPatterns];
        for (int i=0; i<numPatterns; i++) {
            iters[i] = (LinkedListIterator<Location>) patternLocs[i].iterator();
            if(!iters[i].hasNext())
                return locations;
            iters[i].next();
        }

        // dfs
        for(Location patternLoc : patternLocs[0]) {
//            System.out.println("0 " + patternLoc);
            if (dfs(patternLoc, 1, numPatterns, lastStride, iters))
                locations.append(patternLoc);
        }

        return locations;
    }

    private static boolean dfs(Location prevLocation, int depth, int numPatterns, int lastStride, LinkedListIterator<Location>[] iters) {
        if (depth >= numPatterns)
            return true;

        LinkedListIterator<Location> iter = iters[depth];
        Location value = iter.get();
        while(iter.hasNext() && prevLocation.compareTo(value) > 0) {
            value = iter.next();
        }

//        System.out.println(depth + " " + value.toString());

        int stride = depth == numPatterns-1 ? lastStride : 6;
        if (prevLocation.add(stride).equals(value))
            return dfs(value, depth+1, numPatterns, lastStride, iters);
        return false;
    }

}
