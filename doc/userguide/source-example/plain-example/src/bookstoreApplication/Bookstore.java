package bookstoreApplication;

public class Bookstore {

    private final Catalog catalog = new Catalog();
    private final CRM crm = new CRM(catalog);

    public void searchBook() {
        catalog.getBook(false);
        crm.getOffers();
    }
}
