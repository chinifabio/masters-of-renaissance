package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.panels.graphicComponents.BgJPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class LoadingPanel extends GuiPanel {
    public LoadingPanel(GUI gui) {
        super(gui);
    }

    @Override
    public JPanel update() throws IOException {
        JPanel result = new BgJPanel("/Background.png", GUI.gameWidth, GUI.gameHeight);

        result.setLayout(new BoxLayout(result,BoxLayout.Y_AXIS));
        result.setPreferredSize(new Dimension(GUI.gameWidth, GUI.gameHeight));


        JPanel waitingPanel = new JPanel();

        JLabel waitingPlayer = new JLabel("Please wait for other players to join the match");
        waitingPlayer.setFont(new Font("Times New Roman", Font.ITALIC, 26));
        waitingPlayer.setForeground(new Color(220, 179, 120));

        Icon imgIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/gif/waiting.gif")));
        JLabel waitingGif = new JLabel(imgIcon);

        waitingPanel.add(waitingPlayer);
        waitingPanel.add(waitingGif);
        waitingPanel.setOpaque(false);

        result.add(Box.createRigidArea(new Dimension(0,200)));
        result.add(waitingPanel, BorderLayout.CENTER);

        return result;
    }
}
