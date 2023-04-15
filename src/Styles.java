import javax.swing.*;
import java.awt.*;

public class Styles {
    public static JButton buttonNormalization(JButton button) {
        button.setBackground(new Color(139, 135, 220));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBorder(BorderFactory.createLineBorder(new Color(66, 48, 132), 5));
        //button.setFocusPainted(false);
        return button;
    }

    public static JButton tabsButtonNormalization(JButton button) {
        button.setBackground(new Color(139, 135, 220));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        //button.setBorder(BorderFactory.createLineBorder(new Color(66, 48, 132), 5));
        //button.setFocusPainted(false);
        return button;
    }
}
