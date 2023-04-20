import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

public class Item {
    public static Set<String> uniqueNames = new HashSet<>();
    private final JLabel nameL;
    private final JLabel amountL;
    private final JLabel priceL;
    JPanel panel, buttonPanel;
    String name;
    int amount;
    double price;

    Item(String name, int amount, double price) {
        this.name = name;
        this.amount = amount;
        this.price = price;

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
        JButton buy = new JButton("add");
        JButton edit = new JButton("edit");
        JButton delete = new JButton("del");
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

    private void buyActionPerformed(ActionEvent e) {
        try {
            editJSONProperty("Кількість", String.valueOf(this.amount+1));
        } catch (FileNotFoundException | JSONException ex) {
            throw new RuntimeException(ex);
        }
        this.amount++;
        this.amountL.setText(String.valueOf(amount));
        System.out.println("buy");
        panel.getParent().repaint();
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
                    if (!uniqueNames.contains(newName)) {
                        if (newName.strip().length() > 0) {
                            editJSONProperty(answer, newName);
                            this.name = newName;
                            this.nameL.setText(newName);
                            panel.getParent().repaint();
                            break;
                        } else {JOptionPane.showMessageDialog(this.panel, "Стрічка пуста, спробуйте ще раз");}
                    } else {JOptionPane.showMessageDialog(this.panel, "Такий товар вже існує, на жаль, не можливо його додати ще раз");}
                }
            } else if (answer.equals("Ціна")) {
                while (true) {
                    String newPrice = JOptionPane.showInputDialog(this.panel, "Введіть нову ціну (вона має бути в форматі #.##)");
                    if (isNumber(newPrice)) {
                        editJSONProperty(answer, newPrice);
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
        } catch (FileNotFoundException | JSONException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void deleteActionPerformed(ActionEvent e) {
        if (this.amount > 1) {
            try {editJSONProperty("Кількість", String.valueOf(this.amount-1));} catch (FileNotFoundException | JSONException ex) {throw new RuntimeException(ex);}
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
                try {deleteJSONItem();} catch (IOException | JSONException ex) {throw new RuntimeException(ex);}
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
                JSONArray jsonArray = new JSONArray();
                itemToObject(fileName, jsonArray);
            } else {

                FileReader fileReader = new FileReader(fileName);
                JSONTokener jsonTokener = new JSONTokener(fileReader);
                JSONArray jsonArray = new JSONArray(jsonTokener);

                itemToObject(fileName, jsonArray);
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void itemToObject(String fileName, JSONArray jsonArray) throws JSONException, IOException {
        JSONObject item = new JSONObject();
        item.put("item_name", this.name);
        item.put("amount", this.amount);
        item.put("price", this.price);
        jsonArray.put(item);

        FileWriter fileWriter = new FileWriter(fileName);
        fileWriter.write(jsonArray.toString(4));
        fileWriter.flush();
        fileWriter.close();
    }

    public void editJSONProperty(String key, String newValue) throws FileNotFoundException, JSONException {
        File[] files = new File("item_groups").listFiles();
        for (File file : Objects.requireNonNull(files)) {
            FileReader fileReader = new FileReader(file.getPath());
            System.out.println(file.getAbsolutePath());
            JSONTokener tokens = new JSONTokener(fileReader);
            JSONArray itemsArray = new JSONArray(tokens);
            if (key.equals("Назва")) {
                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject item = itemsArray.getJSONObject(i);
                    System.out.println(item.getString("item_name") + " - " + newValue + " - " + this.getName());
                    if (item.getString("item_name").equals(this.getName())) {
                        item.put("item_name", newValue);
                        try {
                            FileWriter fileWriter = new FileWriter(file.getPath());
                            fileWriter.write(itemsArray.toString());
                            fileWriter.flush();
                            fileWriter.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        return;
                    }
                }
            } else if (key.equals("Ціна")) {
                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject item = itemsArray.getJSONObject(i);
                    System.out.println(item.getString("price") + " - " + newValue + " - " + this.getPrice());
                    if (item.getString("item_name").equals(this.getName())) {
                        item.put("price", Double.parseDouble(newValue));
                        try {
                            FileWriter fileWriter = new FileWriter(file.getPath());
                            fileWriter.write(itemsArray.toString());
                            fileWriter.flush();
                            fileWriter.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        return;
                    }
                }
            } else if (key.equals("Кількість")) {
                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject item = itemsArray.getJSONObject(i);
                    System.out.println(item.getString("item_name") + " - " + newValue + " - " + this.getName());
                    if (item.getString("item_name").equals(this.getName())) {
                        item.put("amount", Integer.parseInt(newValue));
                        try {
                            FileWriter fileWriter = new FileWriter(file.getPath());
                            fileWriter.write(itemsArray.toString());
                            fileWriter.flush();
                            fileWriter.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        return;
                    }
                }
            }
        }
    }

    public void deleteJSONItem() throws IOException, JSONException {
        File[] files = new File("item_groups").listFiles();
        for (File file : Objects.requireNonNull(files)) {
            FileReader fileReader = new FileReader(file.getPath());
            System.out.println(file.getAbsolutePath());
            JSONTokener tokens = new JSONTokener(fileReader);
            JSONArray itemsArray = new JSONArray(tokens);
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject item = itemsArray.getJSONObject(i);
                if (item.getString("item_name").equals(this.getName())) {
                    itemsArray.remove(i);

                    // Write the updated JSON data back to the file
                    FileWriter fileWriter = new FileWriter(file.getPath());
                    fileWriter.write(itemsArray.toString());
                    fileWriter.flush();
                    fileWriter.close();

                    return;
                }
            }
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

