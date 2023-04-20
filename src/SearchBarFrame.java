import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class SearchBarFrame extends JFrame implements KeyListener {
    private final List<Item> items;
    private final JTextField searchField;
    private final JList<String> itemList;
    private final DefaultListModel<String> listModel;

    public SearchBarFrame(List<Item> items) {
        this.items = items;
        this.listModel = new DefaultListModel<>();
        this.searchField = new JTextField();
        this.searchField.addKeyListener(this);
        this.itemList = new JList<>(listModel);

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(searchField, BorderLayout.CENTER);

        JScrollPane itemListScrollPane = new JScrollPane(itemList);

        add(searchPanel, BorderLayout.NORTH);
        add(itemListScrollPane, BorderLayout.CENTER);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateItemList(String query) {
        listModel.clear();
        for (Item item : items) {
            if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                listModel.addElement(item.getName());
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // do nothing
    }

    @Override
    public void keyReleased(KeyEvent e) {
        String query = searchField.getText();
        updateItemList(query);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // do nothing
    }

}
