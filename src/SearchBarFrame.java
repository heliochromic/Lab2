import org.json.JSONException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.List;

public class SearchBarFrame extends JFrame implements KeyListener {
    private final List<Item> items;
    private final JTextField searchField;
    private final JScrollPane scroll;

    public SearchBarFrame(List<Item> items) {
        this.items = items;
        this.searchField = new JTextField();
        this.searchField.addKeyListener(this);
        this.scroll = new JScrollPane();

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(searchField, BorderLayout.CENTER);

        add(searchPanel, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        pack();
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateItemList(String query) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        for (Item item : items) {
            if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                if (item.getAmount() < 0) {
                    try {
                        item.deleteJSONItem();
                    } catch (IOException | JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                panel.add(item.getPanel());
            }
        }
        scroll.setViewportView(panel);
        this.repaint();
        scroll.repaint();
    }


    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        String query = searchField.getText();
        updateItemList(query);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

}