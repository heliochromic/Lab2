import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Program extends JFrame implements ActionListener {

    JPanel scrollPanel, buttonPanel, backgroundPanel, s, a, st;
    ArrayList<JPanel> items_list = new ArrayList<>();
    JTextField search_bar;
    JButton search, add_group, add_item, stats;
    JTabbedPane tabbedPane;
    GridBagLayout layout, button_layout;
    GridBagConstraints constraints, button_constraints;

    public Program() {
        setName("vidrah?");
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
        createTab("diary");

        tabbedPane.addTab("group 1", items_list.get(0));
        tabbedPane.addTab("group 2", items_list.get(1));


        //
        scrollPanel = new JPanel();
        scrollPanel.setPreferredSize(new Dimension((int) (this.getWidth() * 0.8), this.getHeight()));

        scrollPanel.add(tabbedPane);

        search_bar = new JTextField();
        search = buttonNormalization(new JButton("Search"));
        add_group = buttonNormalization(new JButton("Add group"));
        add_item = buttonNormalization(new JButton("Add item"));
        stats = buttonNormalization(new JButton("Statistics"));

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

    public JButton buttonNormalization(JButton button) {
        button.setBackground(new Color(139, 135, 220));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBorder(BorderFactory.createLineBorder(new Color(66, 48, 132), 5));
        //button.setFocusPainted(false);
        return button;
    }

    public void createTab(String name) {
        String filename = "";
        try {
            filename = name + ".ser";
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
        //headerRow.add(createLabel("ID", 10));
        headerRow.add(createLabel("Item Name", 60));
        headerRow.add(createLabel("Amount", 10));
        headerRow.add(createLabel("Price", 30));
        headerRow.add(new JPanel());
        headerRow.add(new JPanel());// placeholder for buttons

        div.add(headerRow, BorderLayout.NORTH);


        //тут не має бути ліста, тут має бути діставання об'єкту з файлу
        Item[] items = {
               new Item("Baton", 10, 25),
                new Item("Chocolate bar", 25, 70),
                new Item("Milk", 10, 40)
        };

        for (Item item : items) {
            //Item item = items[i];
            div.add(item.getPanel());
        }


        JScrollPane scrollPane = new JScrollPane(div);
        scrollPane.setPreferredSize(new Dimension(790, 670));
        JPanel p = new JPanel();
        p.add(scrollPane);
        return p;
    }

    public void addItemToFile(String filename, String item_name) {
        try {
            FileOutputStream fos = new FileOutputStream(filename, true);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            //oos.writeObject();
            //додавати об'єкт, зробити як по кнопочці, так і заготовку в різних табах
        } catch (IOException e) {
            System.out.println("damn");
            e.printStackTrace();
        }

    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}