import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import static java.lang.reflect.Array.getLength;

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
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        init();
        setVisible(true);
    }

    private void init() {


        //тут все норм

        tabbedPane = new JTabbedPane();
        createTab("candies");
        createTab("diary");
        for (JPanel i : items_list){
            tabbedPane.addTab("wow",i);
        }

        //
        scrollPanel = new JPanel();
        scrollPanel.setPreferredSize(new Dimension((int) (this.getWidth() * 0.7), this.getHeight()));

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
        constraints.weightx = 0.7;
        constraints.insets = new Insets(0, 0, 0, 0);
        layout.setConstraints(scrollPanel, constraints);
        constraints.weightx = 0.3;
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
            filename = name + ".txt";
            File f = new File(filename);
            if (f.createNewFile()) System.out.println("New " + filename + " was created");
            else System.out.println("File already exists.");
        } catch (IOException e) {
            System.out.println("damn");
            e.printStackTrace();
        }
        items_list.add(createScrollableTable(filename));
    }

    public JPanel createScrollableTable(String filename){
        String[] columnNames = {"id","item","amount","buy","edit","delete"};
        //тут треба, щоб з файлу діставалися продукти і закидувалися в таблицю
        Object[][] rowData = {
                {1,"Ketchup",10,new JButton("✔"),new JButton("✖"), new JButton("✍️")},
                {2,"Ketchup",10,new JButton("✔"),new JButton("✖"), new JButton("✍️")},
                {3,"Ketchup",10,new JButton("✔"),new JButton("✖"), new JButton("✍️")},
                {4,"Ketchup",10,new JButton("✔"),new JButton("✖"), new JButton("✍️")}
        };
        //Подивитися як закидуватися в таблицю кнопки чи переробити це все з GridBagLayout
        //
        DefaultTableModel model = new DefaultTableModel(rowData, columnNames);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
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

    //public void getItemsFromFile(String filename)
    //тут треба буде отримувати item по стрінгу idk, я вже попаяний розуміти цей код,
    // як на цій залупі можна було model comp це йобана таємниця всесвітнього масштабу
    //от хочеться просто людського написати це і забути, бо просто придумуєш собі ідею, а потім хочеш відкинутися в намаганнях її реалізуватию


    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}