package mySimpleKiekerExampleManual;

public class CRM {
    private final Catalog catalog;

    public CRM(final Catalog catalog) {
        this.catalog = catalog;
    }

    public void getOffers() {
        this.catalog.getBook(false);
    }
}
