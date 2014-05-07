package com.etam;

/**
 * User: vryzhuk
 * Date: 5/7/14
 * Time: 12:38 PM
 */
public class Item {
    public String link;
    public String lot;
    public String name;
    public String price;
    public String endRegistration;
    public String beginDate;
    public String place;

    public Item() {
    }

    public Item(String link, String lot, String name, String price, String endRegistration, String beginDate) {
        this.link = link;
        this.lot = lot;
        this.name = name;
        this.price = price;
        this.endRegistration = endRegistration;
        this.beginDate = beginDate;
    }

    public Item(String link, String lot, String name, String price, String endRegistration, String beginDate, String place) {
        this.link = link;
        this.lot = lot;
        this.name = name;
        this.price = price;
        this.endRegistration = endRegistration;
        this.beginDate = beginDate;
        this.place = place;
    }

    @Override
    public String toString() {
        return "Item{" +
                "link='" + link + '\'' +
                ", lot='" + lot + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", endRegistration='" + endRegistration + '\'' +
                ", beginDate='" + beginDate + '\'' +
                ", place='" + place +"\'" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (beginDate != null ? !beginDate.equals(item.beginDate) : item.beginDate != null) return false;
        if (endRegistration != null ? !endRegistration.equals(item.endRegistration) : item.endRegistration != null)
            return false;
        if (link != null ? !link.equals(item.link) : item.link != null) return false;
        if (lot != null ? !lot.equals(item.lot) : item.lot != null) return false;
        if (name != null ? !name.equals(item.name) : item.name != null) return false;
        if (place != null ? !place.equals(item.place) : item.place != null) return false;
        if (price != null ? !price.equals(item.price) : item.price != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = link != null ? link.hashCode() : 0;
        result = 31 * result + (lot != null ? lot.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (endRegistration != null ? endRegistration.hashCode() : 0);
        result = 31 * result + (beginDate != null ? beginDate.hashCode() : 0);
        result = 31 * result + (place != null ? place.hashCode() : 0);
        return result;
    }
}
