import javax.swing.*;

public class Item {
    private static int id = 0;
    private int startAmount;
    private String name;
    private JButton delete, edit;
    Item (String name, JButton delete, JButton edit){
        this.name = name;
        this.delete = delete;
        this.edit = edit;
        id++;
    }

    public static int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JButton getDelete() {
        return delete;
    }

    public void setDelete(JButton delete) {
        this.delete = delete;
    }

    public JButton getEdit() {
        return edit;
    }

    public void setEdit(JButton edit) {
        this.edit = edit;
    }
}
