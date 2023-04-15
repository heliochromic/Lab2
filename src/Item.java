import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class Item {
    private static int id = 0;
    JPanel panel, buttonPanel;
    DecimalFormat df = new DecimalFormat("#.##");
    private JButton buy, edit, delete;
    private JLabel nameL;
    private JLabel amountL;
    private JLabel priceL;
    private String name;
    private int amount;
    private double price;

    Item(String name, int amount, double price) {
        this.name = name;
        this.amount = amount;
        this.price = price;
        id++;

        panel = new JPanel(new GridLayout(1, 4));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.setPreferredSize(new Dimension(800, 30));

        nameL = new JLabel(name);
        //nameL.setPreferredSize(new Dimension(20, 60));
        amountL = new JLabel(String.valueOf(amount));
        //amountL.setPreferredSize(new Dimension(10, 60));
        priceL = new JLabel(String.valueOf(price) + "0");
        //priceL.setPreferredSize(new Dimension(10, 60));
        buttonPanel = new JPanel(new GridLayout(1, 3));
        buy = new JButton("add");
        edit = new JButton("edit");
        delete = new JButton("del");
        buy.addActionListener(this::buyActionPerformed);

        edit.addActionListener(this::editActionPerformed);

        delete.addActionListener(this::deleteActionPerformed);
        panel.add(nameL);
        panel.add(amountL);
        panel.add(priceL);
        buttonPanel.add(Styles.tabsButtonNormalization(buy));
        buttonPanel.add(Styles.tabsButtonNormalization(edit));
        buttonPanel.add(Styles.tabsButtonNormalization(delete));
        panel.add(buttonPanel);
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
        String[] list = {"Назва", "Ціна"};
        String answer = (String) JOptionPane.showInputDialog(this.panel, "Оберіть поле для зміни", this.name, JOptionPane.QUESTION_MESSAGE,
                null, list, "idk");
        System.out.println(answer);
        try {
            if (answer.equals("Назва")) {
                while (true) {
                    String newName = JOptionPane.showInputDialog(this.panel, "Введіть нову назву: ");
                    if (newName.strip().length() > 0) {
                        this.name = newName;
                        this.nameL.setText(newName);
                        panel.getParent().repaint();
                        break;
                    } else {
                        JOptionPane.showMessageDialog(this.panel, "Стрічка пуста, спробуйте ще раз");
                    }
                }
            } else if (answer.equals("Ціна")) {
                while (true) {
                    String newPrice = JOptionPane.showInputDialog(this.panel, "Введіть нову ціну (вона має бути в форматі #*.##)");
                    if (isNumber(newPrice)) {
                        this.price = Double.parseDouble(newPrice);
                        if (newPrice.charAt(newPrice.length() - 2) == '.') newPrice += "0";
                        else if (!newPrice.contains(".")) newPrice += ".00";

                        this.priceL.setText(newPrice);
                        panel.getParent().repaint();
                        break;
                    } else {
                        JOptionPane.showMessageDialog(this.panel, "Ціна не відповідає умовам");
                    }
                }
            }
        } catch (NullPointerException ex) {
            System.out.println("ну ок");
        }
    }

    private void deleteActionPerformed(ActionEvent e) {
        if (this.amount > 1) {
            this.amount--;
            this.amountL.setText(String.valueOf(amount));
            System.out.println("delete");
            panel.getParent().repaint();
        } else {
            int response = JOptionPane.showConfirmDialog(this.panel, "Кількість не може бути меншою за один,\nможливо ви хочете видалити товар", "Delete?",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.NO_OPTION) {
                System.out.println("ok");
            } else if (response == JOptionPane.YES_OPTION) {
                this.panel.getParent().remove(this.panel);
            } else if (response == JOptionPane.CLOSED_OPTION) {
                System.out.println("JOptionPane closed");
            }
        }
    }
    public void writeObjectToFile(String filename) {
        try {
            FileOutputStream fos = new FileOutputStream(filename, true);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
            fos.close();
            System.out.println("Object has been written to file: " + filename);
        } catch (IOException e) {
            System.out.println("Failed to write object to file: " + filename);
            e.printStackTrace();
        }
    }

    public boolean isNumber(String input) {
        return Pattern.compile("^\\d+(.\\d{1,2})?$").matcher(input).matches();
    }
}
