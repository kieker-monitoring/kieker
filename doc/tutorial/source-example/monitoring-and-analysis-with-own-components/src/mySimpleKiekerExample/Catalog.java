package mySimpleKiekerExample;

public class Catalog {
	public static void getBook(boolean complexQuery) {
		if (complexQuery) {
			Bookstore.waitabit(20);
		} else {
			Bookstore.waitabit(2);
		}
	}
}
