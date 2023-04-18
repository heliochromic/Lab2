import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

public class Item {
    private static int id = 0;
    JPanel panel, buttonPanel;
    String name;
    int amount;
    double price;
    private JButton buy, edit, delete;
    private JLabel nameL;
    private JLabel amountL;
    private JLabel priceL;

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
        priceL = new JLabel(String.valueOf(price));
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

    public void addItemIntoJSON(String fileName) {
        try {
            File file = new File(fileName);
            if (file.length() == 0) {
                // If file is empty, create a new JSONArray
                JSONArray jsonArray = new JSONArray();
                JSONObject item = new JSONObject();
                item.put("item_name", this.name);
                item.put("amount", this.amount);
                item.put("price", this.price);
                jsonArray.put(item);

                FileWriter fileWriter = new FileWriter(fileName);
                fileWriter.write(jsonArray.toString(4)); // Pretty print JSON
                fileWriter.flush();
                fileWriter.close();
            } else {
                // If file is not empty, parse the existing JSONArray
                FileReader fileReader = new FileReader(fileName);
                JSONTokener jsonTokener = new JSONTokener(fileReader);
                JSONArray jsonArray = new JSONArray(jsonTokener);

                JSONObject item = new JSONObject();
                item.put("item_name", this.name);
                item.put("amount", this.amount);
                item.put("price", this.price);

                jsonArray.put(item);

                FileWriter fileWriter = new FileWriter(fileName);
                fileWriter.write(jsonArray.toString(4)); // Pretty print JSON
                fileWriter.flush();
                fileWriter.close();
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isNumber(String input) {
        return Pattern.compile("^\\d+(.\\d{1,2})?$").matcher(input).matches();
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public double getPrice() {
        return price;
    }
}

