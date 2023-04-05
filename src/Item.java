import javax.swing.*;

public class Item {
    private static int id = 0;
    private int amount, price;
    private String name;

    Item(String name, int amount, int price) {
        this.name = name;
        this.amount = amount;
        this.price = price;
        id++;
    }

    public static int getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }
}
