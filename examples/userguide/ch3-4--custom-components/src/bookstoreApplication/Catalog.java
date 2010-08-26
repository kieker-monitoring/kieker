package bookstoreTracing;

public class Catalog {
    public void getBook(boolean complexQuery) {
	if (complexQuery) {
		Bookstore.waitabit(20);
	} else {
		Bookstore.waitabit(2);
	}
    }
}
