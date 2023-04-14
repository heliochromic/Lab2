import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Item {
    private static int id = 0;
    private JButton buy, edit, delete;
    private JLabel nameL;
    private JLabel amountL;
    private JLabel priceL;
    private String name;
    private final int price;
    JPanel panel;
    private int amount;

    Item(String name, int amount, int price) {
        this.name = name;
        this.amount = amount;
        this.price = price;
        id++;

        panel = new JPanel(new GridLayout(1,6));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        nameL = new JLabel(name);
        amountL = new JLabel(String.valueOf(amount));
        priceL = new JLabel(String.valueOf(price));
        buy = new JButton("add");
        edit = new JButton("edit");
        delete = new JButton("delete");
        buy.addActionListener(this::buyActionPerformed);

        edit.addActionListener(this::editActionPerformed);

        delete.addActionListener(this::deleteActionPerformed);
        panel.add(nameL);
        panel.add(amountL);
        panel.add(priceL);
        panel.add(buy);
        panel.add(edit);
        panel.add(delete);
    }

    public JPanel getPanel() {
        return panel;
    }

    // Separate methods for each action
    private void buyActionPerformed(ActionEvent e) {
        this.amount++;
        this.amountL.setText(String.valueOf(amount));
        System.out.println("buy");
        panel.getParent().repaint(); // Force a repaint of the parent container
    }

    private void editActionPerformed(ActionEvent e) {
        this.amount+=2;
        this.amountL.setText(String.valueOf(amount));
        System.out.println("edit");
        panel.getParent().repaint(); // Force a repaint of the parent container
    }

    private void deleteActionPerformed(ActionEvent e) {
        this.amount--;
        this.amountL.setText(String.valueOf(amount));
        System.out.println("delete");
        panel.getParent().repaint();
    }
}
