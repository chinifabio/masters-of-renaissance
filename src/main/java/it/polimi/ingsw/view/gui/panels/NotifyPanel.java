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
        JTextArea toAdd = new MessageLabel(message, bg);

        toAdd.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(toAdd);
        add(Box.createRigidArea(new Dimension(0, 5)));
        revalidate();
    }

}

class MessageLabel extends JTextArea {
    public MessageLabel(String text, Color bg) {
        super(5,20);

        setEnabled(false);

        setLineWrap(true);
        setWrapStyleWord(true);

        append(text);
        setMaximumSize(new Dimension(NotifyPanel.width, 50));

        setOpaque(true);
        setBackground(bg);
        setBorder(new EmptyBorder(5, 5, 5, 5));
    }
}
