public class Test {
    public static void main(String[] args) {
        MovieDB db = new MovieDB();
        db.insert(new MovieDBItem("Action", "Spider Man"));
        db.insert(new MovieDBItem("Action", "John Wick"));

        for(MovieDBItem item : db.items()) {
            System.out.println(item.getGenre() + " " + item.getTitle());
        }

    }
}
