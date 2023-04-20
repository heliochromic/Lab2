import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class Program extends JFrame {

    JPanel scrollPanel, buttonPanel, backgroundPanel, s, a, st;
    ArrayList<JPanel> items_list = new ArrayList<>();
    JButton search, add_group, delete_group, add_item, stats;
    JTabbedPane tabbedPane;
    GridBagLayout layout;
    GridBagConstraints constraints;
    Set<String> tabs;

    public Program() {
        this.setName("vidrah?");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        init();
        setVisible(true);
    }

    private static JLabel createLabel(String text, int width) {
        JLabel label = new JLabel(text);
        label.setPreferredSize(new Dimension(width, 30));
        return label;
    }


    private void init() {

        tabbedPane = new JTabbedPane();
        tabs = new TreeSet<>();
        File[] files = new File(".\\item_groups").listFiles();
        assert files != null;
        for (File f : files) {
            tabs.add(f.getName());
        }
        int mda = 0;
        for (String tab : tabs) {
            createTab(tab);
            tabbedPane.add(tab.split("\\.")[0], items_list.get(mda));
            mda++;
        }
        //JSONToArray(".\\item_groups\\Baking.json");
        scrollPanel = new JPanel();
        scrollPanel.setPreferredSize(new Dimension((int) (this.getWidth() * 0.88), this.getHeight()));

        scrollPanel.add(tabbedPane);

        search = Styles.buttonNormalization(new JButton("Search"));
        search.addActionListener(this::searchActionPerformed);
        add_group = Styles.buttonNormalization(new JButton("Add group"));
        add_group.addActionListener(this::addGroupActionPerformed);
        delete_group = Styles.buttonNormalization(new JButton("Delete group"));
        delete_group.addActionListener(this::deleteGroupActionPerformed);
        add_item = Styles.buttonNormalization(new JButton("Add item"));
        add_item.addActionListener(this::addItemActionPerformed);
        stats = Styles.buttonNormalization(new JButton("Statistics"));
        stats.addActionListener(this::statistic);

        s = new JPanel();
        a = new JPanel();
        st = new JPanel();

        s.add(search);
        a.add(add_group);
        a.add(delete_group);
        a.add(add_item);
        st.add(stats);

        buttonPanel = new JPanel(new GridLayout(7, 1));
        buttonPanel.setBackground(new Color(66, 48, 132));
        buttonPanel.setPreferredSize(new Dimension(((int) (this.getWidth() * 0.3)), this.getHeight()));
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(search);
        buttonPanel.add(add_group);
        buttonPanel.add(delete_group);
        buttonPanel.add(add_item);
        buttonPanel.add(stats);

        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 10, 20, 10));

        layout = new GridBagLayout();
        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.8;
        constraints.insets = new Insets(0, 0, 0, 0);
        layout.setConstraints(scrollPanel, constraints);
        constraints.weightx = 0.2;
        layout.setConstraints(buttonPanel, constraints);

        backgroundPanel = new JPanel(layout);
        backgroundPanel.add(scrollPanel);
        backgroundPanel.add(buttonPanel);
        this.getContentPane().add(backgroundPanel);
        this.pack();
    }


    public void createTab(String name) {
        String filename = "";
        try {
            try {
                if (name.split("\\.")[1].equals("json")) filename = ".\\item_groups\\" + name;
            } catch (ArrayIndexOutOfBoundsException ex) {
                filename = ".\\item_groups\\" + name + ".json";
            }
            File f = new File(filename);
            if (f.createNewFile()) System.out.println("New " + filename + " was created");
            else System.out.println("File already exists.");
        } catch (IOException e) {
            System.out.println("damn");
            e.printStackTrace();
        }
        items_list.add(createScrollableTable(filename));
    }

    public JPanel createScrollableTable(String filename) {
        JPanel headerRow = new JPanel(new GridLayout(1, 5));
        JPanel div = new JPanel(new GridLayout(0, 1));
        headerRow.setPreferredSize(new Dimension(div.getWidth(), 50));
        headerRow.add(createLabel("Item Name", 60));
        headerRow.add(createLabel("Amount", 10));
        headerRow.add(createLabel("Price", 30));
        headerRow.add(new JPanel());

        div.add(headerRow, BorderLayout.NORTH);
        div.add(headerRow, BorderLayout.NORTH);

        if (new File(filename).length() > 0) {
            ArrayList<Item> items = readJSON(filename);
            for (Item item : items) {
                if (!Item.uniqueNames.contains(item.getName().strip())) {
                    div.add(item.getPanel());
                    Item.uniqueNames.add(item.getName().strip());
                }
            }
        }


        JScrollPane scrollPane = new JScrollPane(div);
        scrollPane.setPreferredSize(new Dimension(850, 670));
        JPanel p = new JPanel();
        p.add(scrollPane);
        return p;
    }

    private void searchActionPerformed(ActionEvent e) {
        ArrayList<Item> allItems = new ArrayList<>();
        File[] filessss = new File("item_groups").listFiles();
        System.out.println(Arrays.toString(filessss));
        for (File f : Objects.requireNonNull(filessss)) {
            allItems.addAll(readJSON(f.getAbsolutePath()));
        }
        new SearchBarFrame(allItems).setSize(new Dimension(850,600));
    }

    private void addItemActionPerformed(ActionEvent e) {
        JFrame newFrame = new JFrame();
        newFrame.setTitle("New Frame");
        newFrame.setSize(300, 180);
        newFrame.setResizable(false);
        setLocationRelativeTo(null);

        newFrame.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel lblItemName = new JLabel("Item Name:");
        JTextField txtItemName = new JTextField();
        newFrame.add(lblItemName);
        newFrame.add(txtItemName);

        JLabel lblAmount = new JLabel("Amount:");
        JTextField txtAmount = new JTextField();
        newFrame.add(lblAmount);
        newFrame.add(txtAmount);

        JLabel lblPrice = new JLabel("Price:");
        JTextField txtPrice = new JTextField();
        newFrame.add(lblPrice);
        newFrame.add(txtPrice);

        newFrame.add(new JLabel());
        JButton btnSave = new JButton("Save");
        Styles.tabsButtonNormalization(btnSave);
        btnSave.addActionListener(e1 -> {
            Item newItem;
            try {
                String itemName = txtItemName.getText();
                if (!Item.uniqueNames.contains(itemName)) {
                    Item.uniqueNames.add(itemName);
                    String amount = txtAmount.getText();
                    String price = txtPrice.getText();
                    File[] files = new File(".\\item_groups").listFiles();
                    if (!itemName.isEmpty() && !amount.isEmpty() && !price.isEmpty() && files != null) {
                        String fileName = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
                        System.out.println(fileName);
                        File file = null;
                        int index = 0;
                        while (index < files.length) {
                            if (files[index].getName().equals(fileName + ".json")) {
                                file = files[index];
                                break;
                            }
                            index++;
                        }

                        System.out.println(file);

                        System.out.println();
                        System.out.println(tabbedPane.getSelectedIndex());

                        newItem = new Item(itemName, Integer.parseInt(amount), Double.parseDouble(price));
                        assert file != null;
                        newItem.addItemIntoJSON(file.getAbsolutePath());
                        JPanel panel = items_list.get(tabbedPane.getSelectedIndex());

                        JScrollPane scrollPane = (JScrollPane) panel.getComponent(0);
                        JPanel div = (JPanel) scrollPane.getViewport().getView();
                        div.add(newItem.getPanel());
                        panel.revalidate();
                        panel.repaint();
                    }
                } else {
                    JOptionPane.showMessageDialog(newFrame, "This product already exists in lists", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(newFrame, "Please fill in all the fields.", "Error", JOptionPane.ERROR_MESSAGE);
                txtItemName.setText("");
                txtAmount.setText("");
                txtPrice.setText("");
            }
            newFrame.dispose();
        });
        newFrame.add(btnSave);
        newFrame.setVisible(true);
    }

    private void addGroupActionPerformed(ActionEvent e) {
        int response = JOptionPane.showConfirmDialog(this.getParent(), "Чи хочете ви додати нову групу товарів?", "Add?",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.NO_OPTION) {
        } else if (response == JOptionPane.YES_OPTION) {
            String newGroup = JOptionPane.showInputDialog(this.getParent(), "Введіть нову назву: ");
            if (!tabs.contains(newGroup + ".json")) {
                if (newGroup.strip().length() > 0) {
                    createTab(newGroup);
                    tabs.add(newGroup);
                    tabbedPane.add(newGroup, items_list.get(items_list.size() - 1));
                } else {
                    JOptionPane.showMessageDialog(this.getParent(), "Стрічка пуста, спробуйте ще раз");
                }
            } else {
                JOptionPane.showMessageDialog(this.getParent(), "Така групу товарів вже існує, спробуйте ще раз");
            }
        } else if (response == JOptionPane.CLOSED_OPTION) {
            System.out.println("JOptionPane closed");
        }
    }

    public void deleteGroupActionPerformed(ActionEvent e) {
        int response = JOptionPane.showConfirmDialog(this.getParent(), "Do you want to delete the current group of items?", "Delete?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            File[] filess = new File("item_groups").listFiles();
            int selectedIndex = 0;
            String fileNamee = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
            while (true) {
                assert filess != null;
                if (!(selectedIndex < filess.length)) break;
                if (filess[selectedIndex].getName().equals(fileNamee + ".json")) {
                    break;
                }
                selectedIndex++;
            }
            if (selectedIndex < tabbedPane.getTabCount()) {
                File[] files = new File("item_groups").listFiles();
                if (files != null && selectedIndex < files.length) {
                    try {
                        String fileName = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
                        System.out.println(fileName);
                        int index = 0;
                        while (index < files.length) {
                            if (files[index].getName().equals(fileName + ".json")) {
                                System.out.println(index);
                                File file = files[index];
                                System.out.println(file.getAbsolutePath());
                                items_list.remove(tabbedPane.getSelectedIndex());
                                tabbedPane.remove(tabbedPane.getSelectedIndex());
                                Files.deleteIfExists(Paths.get(file.getAbsolutePath()));
                                System.out.println("File was deleted");
                                break;
                            }
                            index++;
                        }
                    } catch (IOException ex) {
                        System.out.println("Failed to delete file");
                    }
                } else {
                    System.out.println("Invalid index or item_groups directory is empty");
                }
            } else {
                System.out.println("Invalid selected index");
            }
        } else if (response == JOptionPane.NO_OPTION) {
        } else if (response == JOptionPane.CLOSED_OPTION) {
            System.out.println("JOptionPane closed");
        }
    }

    public ArrayList<Item> readJSON(String path) {
        ArrayList<Item> tempArrayList = new ArrayList<>();
        if ((new File(path)).length() > 0) {
            try {
                JSONTokener tokens = new JSONTokener(new FileReader(path));
                JSONArray items = new JSONArray(tokens);
                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    tempArrayList.add(new Item(item.getString("item_name"), item.getInt("amount"), item.getDouble("price")));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return tempArrayList;
    }

    private void statistic(ActionEvent e) {
        double total = 0;
        JFrame frameSt = new JFrame();
        frameSt.setTitle("Statistic");
        frameSt.setSize(1000, 700);
        frameSt.setResizable(false);
        File[] files = new File("item_groups").listFiles();
        frameSt.setLayout(new GridLayout(Objects.requireNonNull(files).length + 1, 1));
        for (File f : files) {
            double totalGr = 0;
            JPanel group = new JPanel(new BorderLayout());
            JLabel name = new JLabel(f.getName());
            name.setHorizontalAlignment(JLabel.HORIZONTAL);
            group.add(name, BorderLayout.NORTH);
            System.out.println(f.getAbsolutePath());
            ArrayList<Item> items = readJSON(f.getAbsolutePath());
            System.out.println(items);
            JPanel it = new JPanel(new GridLayout(items.size(), 2));
            for (Item item : items) {
                it.add(item.getPanel());
                double res = item.getPrice() * item.getAmount();
                it.add(new JLabel(res + " $"));
                totalGr += res;
            }
            group.add(it, BorderLayout.CENTER);
            JLabel totGr = new JLabel("Загальна вартість продуктів у групі: " + totalGr + " $");
            total += totalGr;
            totGr.setHorizontalAlignment(JLabel.HORIZONTAL);
            group.add(totGr, BorderLayout.SOUTH);
            frameSt.add(group);
            JLabel totalPrice = new JLabel("Сумарна вартість товарів на складі: " + total + " $");
            totalPrice.setHorizontalAlignment(JLabel.HORIZONTAL);
            frameSt.add(totalPrice);
        }
        frameSt.setVisible(true);
    }



    private void statistic(ActionEvent e) {
        double total = 0;
        JFrame frameSt = new JFrame();
        frameSt.setTitle("Statistic");
        frameSt.setSize(1000, 700);
        frameSt.setResizable(false);
        JPanel contentPane = new JPanel(new GridLayout());
        //  contentPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        JScrollPane scrollPane = new JScrollPane(contentPane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        frameSt.setContentPane(scrollPane);
        File[] files = new File("item_groups").listFiles();
        for (File f : files) {

            double totalGr = 0;
            JPanel group = new JPanel(new BorderLayout());
            //  group.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            JLabel name = new JLabel(f.getName());
            name.setHorizontalAlignment(JLabel.HORIZONTAL);
            group.add(name, BorderLayout.NORTH);
            System.out.println(f.getAbsolutePath());
            ArrayList<Item> items = readJSON(f.getAbsolutePath());
            System.out.println(items);
            if(items.isEmpty()){
                JLabel empty = new JLabel("група не має жодних товарів");
                empty.setHorizontalAlignment(JLabel.HORIZONTAL);
                group.add(empty, BorderLayout.CENTER);
                contentPane.add(group);
                continue;
            }
            JPanel it = new JPanel(new GridLayout(items.size(), 2));
            for (Item item : items) {
                JLabel itemOP=new JLabel("Назва товару: <"+item.getName() +"> Кількість : "+item.getAmount()+" Ціна за одиницю: "+item.getPrice()+" $");
                itemOP.setPreferredSize(new Dimension(700, 20));
                it.add(itemOP);
                double res = item.getPrice() * item.getAmount();
                JLabel sc=new JLabel("Всього : "+res + " $");
                sc.setPreferredSize(new Dimension(300, 20));
                it.add(sc);
                totalGr += res;
            }
            group.add(it, BorderLayout.CENTER);
            JLabel totGr = new JLabel("Загальна вартість продуктів у групі: " + totalGr + " $");
            total += totalGr;
            totGr.setHorizontalAlignment(JLabel.HORIZONTAL);
            group.add(totGr, BorderLayout.SOUTH);
            contentPane.add(group);
            group.setPreferredSize(new Dimension(950, (items.size() * 60) + 70));
        }
        JLabel totalPrice = new JLabel("Сумарна вартість товарів на складі: " + total + " $");
        totalPrice.setHorizontalAlignment(JLabel.HORIZONTAL);
        totalPrice.setPreferredSize(new Dimension(950,20));
        contentPane.add(totalPrice);
        frameSt.setVisible(true);
    }
    /*

    Місце для методів іри і не кроку вгору до мого коду

    */
}