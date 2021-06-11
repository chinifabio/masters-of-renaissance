package it.polimi.ingsw.view.gui.panels;

import javax.swing.*;
import java.awt.*;

public class LayoutForChat extends JFrame {
    private static final String LOREM_IPSUM = "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
            + " Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown"
            + " printer took a galley of type and scrambled it to make a type specimen book. It has survived"
            + " not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged."
            + " It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages,"
            + " and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";
    private static final int CHAT_BOX_WIDTH = 150;
    private JPanel boxPanel;

    public LayoutForChat() {
        super("");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        boxPanel = new JPanel();
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));
        addLeftChat();
        addRightChat();

        JScrollPane scrollPane = new JScrollPane(boxPanel);
        add(scrollPane, BorderLayout.CENTER);

        setMinimumSize(new Dimension(CHAT_BOX_WIDTH + 150, 400));
        setLocationByPlatform(true);
        pack();

        new Timer(2000, e -> {
            boolean right = (int) (Math.random() * 2) == 0;
            if (right)
                addRightChat();
            else
                addLeftChat();

            JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
            SwingUtilities.invokeLater(() -> verticalScrollBar.setValue(verticalScrollBar.getMaximum()));
        }).start();
    }

    private void addChat(int flowLayoutAlign, Color borderColor) {
        JPanel panel = new JPanel(new FlowLayout(flowLayoutAlign));
        JLabel label = new JLabel();
        label.setBorder(BorderFactory.createLineBorder(borderColor));
        label.setText("<html><body style='width: " + CHAT_BOX_WIDTH + "px;'>" + loremIpsum());
        panel.add(label);
        boxPanel.add(panel);
        boxPanel.revalidate();
    }

    private void addRightChat() {
        addChat(FlowLayout.TRAILING, Color.red);
    }

    private void addLeftChat() {
        addChat(FlowLayout.LEADING, Color.green);
    }

    private String loremIpsum() {
        return LOREM_IPSUM.substring(0, (int) (Math.random() * LOREM_IPSUM.length()));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LayoutForChat().setVisible(true));
    }
}
