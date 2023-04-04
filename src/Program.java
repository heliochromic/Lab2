import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static java.lang.reflect.Array.getLength;

public class Program extends JFrame implements ActionListener {

    JScrollPane items;
    JPanel scrollPanel, buttonPanel, backgroundPanel;
    ArrayList<JPanel> items_list;
    JTextField search_bar;
    JButton search, add_group, add_item, stats;
    JTabbedPane tabbedPane;
    GridBagLayout layout;
    GridBagConstraints constraints;

    public Program() {
        setName("vidrah?");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        init();

        setVisible(true);
    }

    private void init() {

        scrollPanel = new JPanel();
        scrollPanel.setPreferredSize(new Dimension((int) (this.getWidth() * 0.7), this.getHeight()));


        search_bar = new JTextField();
        search = buttonNormalization(new JButton("Search"));
        add_group = buttonNormalization(new JButton("Add group"));
        add_item = buttonNormalization(new JButton("Add item"));
        stats = buttonNormalization(new JButton("Statistics"));

        buttonPanel = new JPanel(new GridLayout(5,1));
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setPreferredSize(new Dimension(((int) (this.getWidth() * 0.3)), this.getHeight()));
        buttonPanel.add((search_bar));
        buttonPanel.add(search);
        buttonPanel.add(add_group);
        buttonPanel.add(add_item);
        buttonPanel.add(stats);

        buttonPanel.setBorder(BorderFactory.createEmptyBorder(60,5,60,5));

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

    public JButton buttonNormalization(JButton button){
        button.setBackground(new Color(192, 192, 192));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        button.setFocusPainted(false);
        return button;
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