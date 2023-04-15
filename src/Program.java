import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;


public class Program extends JFrame {

    JPanel scrollPanel, buttonPanel, backgroundPanel, s, a, st;
    ArrayList<JPanel> items_list = new ArrayList<>();
    JTextField search_bar;
    JButton search, add_group, add_item, stats;
    JTabbedPane tabbedPane;
    GridBagLayout layout;
    GridBagConstraints constraints;

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

    public static Item[] JSONToArray(String fileName) {

        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(fileName)) {
            //Read JSON file
            var obj = jsonParser.parse(reader);

            if (obj != null) {
                JSONArray employeeList = (JSONArray) obj;
                System.out.println(employeeList.size());
                System.out.println(employeeList);
            }
            ArrayList<Item> tempItems = new ArrayList<>();
            //Iterate over employee array
            /*employeeList.forEach(emp -> {
                return tempItems.add(parseEmployeeObject((JSONObject) emp));
            });*/

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private static Item parseEmployeeObject(JSONObject employee, int index) {
        //Get employee object within list
        JSONObject itemObj = (JSONObject) employee.get(String.valueOf(index));

        //Get employee first name
        //String firstName = (String) employeeObject.get("firstName");
        return new Item((String) itemObj.get("name"), (int) itemObj.get("amount"), (double) itemObj.get("price"));
    }

    private void init() {

        tabbedPane = new JTabbedPane();
        Set<String> tabs = new TreeSet<>();
        tabs.add("Candies");
        tabs.add("Diary");
        tabs.add("Baking");
        tabs.add("Hygiene");
        int mda = 0;
        for (String tab : tabs) {
            createTab(tab);
            tabbedPane.add(tab, items_list.get(mda));
            mda++;
        }
        //JSONToArray(".\\item_groups\\Baking.json");
        scrollPanel = new JPanel();
        scrollPanel.setPreferredSize(new Dimension((int) (this.getWidth() * 0.88), this.getHeight()));

        scrollPanel.add(tabbedPane);

        search_bar = new JTextField();

        search = Styles.buttonNormalization(new JButton("Search"));
        add_group = Styles.buttonNormalization(new JButton("Add group"));
        add_item = Styles.buttonNormalization(new JButton("Add item"));
        add_item.addActionListener(this::addItemActionPerformed);
        stats = Styles.buttonNormalization(new JButton("Statistics"));

        s = new JPanel();
        a = new JPanel();
        st = new JPanel();

        s.add(search_bar);
        s.add(search);
        a.add(add_group);
        a.add(add_item);
        st.add(stats);

        buttonPanel = new JPanel(new GridLayout(7, 1));
        buttonPanel.setBackground(new Color(66, 48, 132));
        buttonPanel.setPreferredSize(new Dimension(((int) (this.getWidth() * 0.3)), this.getHeight()));
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(search_bar);
        search_bar.setBorder(BorderFactory.createLineBorder(new Color(66, 48, 132), 5));
        search_bar.setFont(new Font("Arial", Font.BOLD, 16));
        buttonPanel.add(search);
        buttonPanel.add(add_group);
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
            filename = ".\\item_groups\\" + name + ".json";
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


        /*Item[] items = {
                new Item("Baton", 10, 25),
                new Item("Chocolate bar", 25, 70),
                new Item("Milk", 10, 40),
        };*/



        /*if (items != null){
            for (Item item : items) {
                div.add(item.getPanel());
            }
        }*/


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
            String itemName = txtItemName.getText();
            String amount = txtAmount.getText();
            String price = txtPrice.getText();
            File[] files = new File(".\\item_groups").listFiles();
            if (files != null) {
                File file = files[tabbedPane.getSelectedIndex()];
                Item tempItem = new Item(itemName, Integer.parseInt(amount), Double.parseDouble(price));
                tempItem.addItemIntoJSON(file.getAbsolutePath());
            }
            newFrame.dispose();
            if (new File(".\\item_groups\\Baking.json").length() > 0) {
                JSONToArray(".\\item_groups\\Baking.json");
            }
        });
        newFrame.add(btnSave);
        newFrame.setVisible(true);
    }

}