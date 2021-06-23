package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.panels.graphicComponents.BgJPanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class LoadingPanel extends GuiPanel {
    public LoadingPanel(GUI gui) {
        super(gui);
    }

    @Override
    public JPanel update() throws IOException {
        JPanel result = new BgJPanel("/Background.png", GUI.gameWidth, GUI.gameHeight);

        result.setPreferredSize(new Dimension(GUI.gameWidth, GUI.gameHeight));


        JPanel waitingPanel = new JPanel();
        waitingPanel.setLayout(new BoxLayout(waitingPanel, BoxLayout.Y_AXIS));

        JLabel waitingPlayer = new JLabel("Waiting for other players...");
        waitingPlayer.setFont(new Font("Times New Roman", Font.ITALIC, 32));
        waitingPlayer.setForeground(new Color(220, 179, 120));

        JPanel gifPanel = new JPanel();
        Icon imgIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/gif/waiting.gif")));
        JLabel waitingGif = new JLabel(imgIcon);
        gifPanel.setOpaque(false);
        gifPanel.add(Box.createRigidArea(new Dimension(60,0)));
        gifPanel.add(waitingGif);


        waitingPanel.add(waitingPlayer);
        waitingPanel.add(gifPanel);
        waitingPanel.setOpaque(false);

        result.add(Box.createRigidArea(new Dimension(0,800)));
        result.add(waitingPanel);

        return result;
    }
}
