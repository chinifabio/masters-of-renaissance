package it.polimi.ingsw.view.gui.panels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class NotifyPanel extends JPanel{

    public static final int width = 300;

    public NotifyPanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    public void appendMessage(String message, Color bg) {
        JLabel label = new JLabel();
        label.setOpaque(true);
        label.setBackground(bg);
        label.setBorder(new EmptyBorder(5, 5, 5, 5));

        label.setAlignmentX(CENTER_ALIGNMENT);

        label.setText(String.format("<html><div WIDTH=%d>%s</div></html>", width - 25, message));

        add(label);
        add(Box.createRigidArea(new Dimension(0, 10)));
        revalidate();
    }

}
