package it.polimi.ingsw.view.gui.panels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * This class is a Panel that notify the Player during the match
 */
public class NotifyPanel extends JPanel{

    /**
     * This attribute is the width of the panel
     */
    public static final int width = 300;

    /**
     * This is the constructor of the class
     */
    public NotifyPanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    /**
     * This method inserts new messages below existing ones
     * @param message is the new message to append
     * @param bg is the color of the message
     */
    public synchronized void appendMessage(String message, Color bg) {
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
