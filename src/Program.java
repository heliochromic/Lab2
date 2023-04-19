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
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;


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
        for (File f : files) {
            tabs.add(f.getName());
        }
        System.out.println(files);
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
        add_group = Styles.buttonNormalization(new JButton("Add group"));
        add_group.addActionListener(this::addGroupActionPerformed);
        delete_group = Styles.buttonNormalization(new JButton("Delete group"));
        delete_group.addActionListener(this::deleteGroupActionPerformed);
        add_item = Styles.buttonNormalization(new JButton("Add item"));
        add_item.addActionListener(this::addItemActionPerformed);
        stats = Styles.buttonNormalization(new JButton("Statistics"));

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

    private void addItemActionPerformed(ActionEvent e) {
        JFrame newFrame = new JFrame();
        newFrame.setTitle("New Frame");
        newFrame.setSize(300, 180);
        newFrame.setResizable(false);

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
                        File file = files[tabbedPane.getSelectedIndex()];
                        newItem = new Item(itemName, Integer.parseInt(amount), Double.parseDouble(price));
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
            int selectedIndex = tabbedPane.getSelectedIndex();
            if (selectedIndex >= 0 && selectedIndex < tabbedPane.getTabCount()) {
                File[] files = new File("item_groups").listFiles();
                if (files != null && selectedIndex < files.length) {
                    try {
                        Files.deleteIfExists(Paths.get(files[selectedIndex].getPath()));
                        System.out.println("File was deleted");
                        items_list.remove(selectedIndex);
                        tabbedPane.remove(selectedIndex);
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
            // Do nothing
        } else if (response == JOptionPane.CLOSED_OPTION) {
            System.out.println("JOptionPane closed");
        }
    }

    public ArrayList<Item> readJSON(String path) {
        ArrayList<Item> tempArrayList = new ArrayList<>();
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
        return tempArrayList;
    }



    /*

    Місце для методів іри і не кроку вгору до мого коду

    */
}