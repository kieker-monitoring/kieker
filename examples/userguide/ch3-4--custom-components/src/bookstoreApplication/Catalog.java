package bookstoreApplication;

public class Catalog {
    public void getBook(final boolean complexQuery) {
        try {
            if (complexQuery) {
                Thread.sleep(20);
            } else {
                Thread.sleep(2);
            }
        } catch (InterruptedException ex) {}
    }
}
