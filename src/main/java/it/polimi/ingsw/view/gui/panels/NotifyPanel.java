package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.view.gui.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NotifyPanel extends JPanel{

    protected String message = "";
    protected JTextArea textArea;
    private final static String newline = "\n";

    public NotifyPanel() {
        super(new GridBagLayout());

        textArea = new JTextArea(5, 20);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        //Add Components to this panel.
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;

        c.fill = GridBagConstraints.HORIZONTAL;

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        textArea.append(this.message + newline);
        add(scrollPane, c);
    }

    public void appendMessage(String message) {
        this.message = message;
        textArea.append(this.message + newline);

        //Make sure the new text is visible, even if there
        //was a selection in the text area.
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}
