package it.polimi.ingsw.view.gui.panels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class NotifyPanel extends JPanel{

    public NotifyPanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    public void appendMessage(String message, Color bg) {
        JLabel toAdd = new MessageLabel(message, bg);

        toAdd.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(toAdd);
        add(Box.createRigidArea(new Dimension(0, 5)));
        revalidate();
    }

}

class MessageLabel extends JLabel {
    public MessageLabel(String text, Color bg) {
        super(text);

        setOpaque(true);
        setBackground(bg);
        setBorder(new EmptyBorder(5, 5, 5, 5));
    }
}
