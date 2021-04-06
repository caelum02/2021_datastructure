import java.util.Iterator;

/**
 * Genre, Title 을 관리하는 영화 데이터베이스.
 * 
 * MyLinkedList 를 사용해 각각 Genre와 Title에 따라 내부적으로 정렬된 상태를  
 * 유지하는 데이터베이스이다. 
 */
public class MovieDB {

	private SortedLinkedList<MovieList> genreList;

    public MovieDB() {
        // FIXME implement this

    	// HINT: MovieDBGenre 클래스를 정렬된 상태로 유지하기 위한 
    	// MyLinkedList 타입의 멤버 변수를 초기화 한다.

		genreList = new SortedLinkedList<>();
    }

    public void insert(MovieDBItem item) {
        // FIXME implement this
        // Insert the given item to the MovieDB.

		String genre = item.getGenre(), title = item.getTitle();

		for(MovieList movieList : genreList) {
			if(genre.equals(movieList.getGenreName())) {
				movieList.add(title);
				return;
			}
		}

		// 해당 장르의 영화가 아직 존재하지 않을경우 (movieList 존재 하지 않음)
		MovieList movieList = new MovieList(genre);
		movieList.add(title);
		genreList.add(movieList);
    }

    public void delete(MovieDBItem item) {
        // FIXME implement this
        // Remove the given item from the MovieDB.

		String genre = item.getGenre(), title = item.getTitle();

		Iterator<MovieList> it = new MyLinkedListIterator<>(genreList);
		MovieList movieList;

		while(it.hasNext()) {
			movieList = it.next();

			if(genre.equals(movieList.getGenreName())) {
				if(movieList.erase(title))
					it.remove();

				return;
			}
		}
    }

    public MyLinkedList<MovieDBItem> search(String term) {
        // FIXME implement this
        // Search the given term from the MovieDB.
        // You should return a linked list of MovieDBItem.
        // The search command is handled at SearchCmd class.
    	
    	// Printing search results is the responsibility of SearchCmd class. 
    	// So you must not use System.out in this method to achieve specs of the assignment.

		MyLinkedList<MovieDBItem> results = new MyLinkedList<>();

      	for(MovieList movieList : genreList) {
      		for(MovieDBItem item : movieList.searchAll(term))
      			results.add(item);
		}

        return results;
    }
    
    public MyLinkedList<MovieDBItem> items() {
        // FIXME implement this
        // Search the given term from the MovieDatabase.
        // You should return a linked list of QueryResult.
        // The print command is handled at PrintCmd class.

    	// Printing movie items is the responsibility of PrintCmd class. 
    	// So you must not use System.out in this method to achieve specs of the assignment.

    	MyLinkedList<MovieDBItem> results = new MyLinkedList<>();
    	for(MovieList movieList : genreList) {
    		String genre = movieList.getGenreName();
    		for(String title : movieList)
    			results.add(new MovieDBItem(genre, title));
		}
        
    	return results;
    }
}

class MovieList extends SortedLinkedList<String> implements Comparable<MovieList>{

	private String genre;

	public MovieList(String genre) {
		super();
		this.genre = genre;
		head.setItem(this.genre);
	}

	public MovieList() {
		this(null);
	}

	public String getGenreName() {
		return genre;
	}

	@Override
	public int compareTo(MovieList movieList) {
		return ((ComparableNode<String>) head).compareTo(movieList.head);
	}

	public MyLinkedList<MovieDBItem> searchAll(String term) {
		MyLinkedList<MovieDBItem> result = new MyLinkedList<>();
		for (String title : this) {
			if(title.contains(term))
				result.add(new MovieDBItem(genre, title));
		}

		return result;
	}

	// return true if the last movie is erased
	public boolean erase(String title) {
		Iterator<String> it = new MyLinkedListIterator<>(this);

		String item;
		while(it.hasNext()) {
			item = it.next();
			if(title.equals(item)) {
				it.remove(); break;
			}
		}

		return isEmpty();
	}
}