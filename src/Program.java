import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

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

    private void init() {


        //тут все норм

        tabbedPane = new JTabbedPane();
        createTab("candies");
        createTab("dary");

        tabbedPane.addTab("group 1", items_list.get(0));
        tabbedPane.addTab("group 2", items_list.get(1));


        //
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

        //тут не має бути ліста, тут має бути діставання об'єкту з файлу
        Item[] items = {
                new Item("Baton", 10, 25),
                new Item("Chocolate bar", 25, 70),
                new Item("Milk", 10, 40),
        };

        for (Item item : items) {
            div.add(item.getPanel());
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

        // Set the layout manager for the new frame's content pane to GridLayout
        newFrame.setLayout(new GridLayout(4, 2, 10, 10)); // 4 rows, 2 columns, with 10 pixels gap

        // Add labels and text fields for item name, amount, and price
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

        // Add a button to submit the form
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
                tempItem.writeObjectToFile(file.getAbsolutePath());
            }

            newFrame.dispose();
        });
        newFrame.add(btnSave);

        // Set the visibility of the new frame to true
        newFrame.setVisible(true);

    }
}