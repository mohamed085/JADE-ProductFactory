package model;

public class Product {
    private int number;
    private int price;
    private String name;

    public Product(int number, int price, String name) {
        this.number = number;
        this.price = price;
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "product name: " + name +
                ", product price: " + price +
                ", number of available product: " + number;
    }
}
