import javax.swing.*;
import java.awt.*;

public class Item {
    private static int id = 0;
    private JButton buy, edit, delete;
    private JLabel name, amount, price;

    Item(JLabel name, JLabel amount, JLabel price) {
        this.name = name;
        this.amount = amount;
        this.price = price;
        id++;
    }

    public static int getId() {
        return id;
    }

    public JLabel getName() {
        return name;
    }

    public JLabel getAmount() {
        return amount;
    }

    public JLabel getPrice() {
        return price;
    }

    public JButton getBuy() {
        return buy;
    }

    public void setBuy(JButton buy) {
        buy.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        this.buy = buy;
    }

    public JButton getEdit() {
        return edit;
    }

    public void setEdit(JButton edit) {
        edit.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        this.edit = edit;
    }

    public JButton getDelete() {
        return delete;
    }

    public void setDelete(JButton delete) {
        delete.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        this.delete = delete;
    }
}
