package com.kirja.xxx.reader;

public class Item {
    private String barcode;
    private String author;
    private String name;
    private int checkouts;
    private int totalcheckouts;
    private int itemcount;
    boolean found;

    public Item(String barcode) {
        this.barcode = barcode;
        found = false;
        getItemData();
    }


    public double getCheckoutAverage() {
        return itemcount/checkouts;
    }

    //niteiden määrä & keskiarvo
    private void getItemData() {
        /*
        for (int i = 0; i < database.size() - 1; i++) {
            String s = database.get(i).get("barcode");
            if (s.equals(this.barcode)) {
                this.name = database.get(i).get("name");
                this.author = database.get(i).get("author");
                this.checkouts += database.get(i).get("checkouts");
                found = true;
            }
            countTotalCheckouts();
        }
        */
    }

    private void countTotalCheckouts() {
        this.checkouts = 0;
        /*
        for (int i = 0; i < database.size() - 1; i++) {
            String name = database.get(i).get("name");
            String author = database.get(i).get("author");
            if (name.equals(this.name) && author.equals(this.author)) {
                this.totalcheckouts += database.get(i).get("checkouts");
                this.itemcount++;
            }
        }
        */
    }

    public int getTotalCheckouts () {
        return this.totalcheckouts;
    }

    public int getCheckouts () {
        return this.checkouts;
    }

    public int getItemCount () {
        return this.itemcount;
    }

    public String getBarcode () {
        return this.barcode;
    }

    public String getAuthor () {
        return this.author;
    }

    public boolean isFound () {
        return found;
    }

}