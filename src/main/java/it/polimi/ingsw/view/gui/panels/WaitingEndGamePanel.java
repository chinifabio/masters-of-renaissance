package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.panels.graphicComponents.BgJPanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class is the GUI Panel where the Player waits that the other players end their last turn
 */
public class WaitingEndGamePanel extends GuiPanel{

    /**
     * This is the constructor of the class
     * @param gui is the GUI that contains all the needed info
     */
    public WaitingEndGamePanel(GUI gui) {
        super(gui);
    }

    /**
     * This method update the current panel after a change
     *
     * @return the current Panel updated
     * @throws IOException if there is an I/O problem
     */
    @Override
    public JPanel update() throws IOException {
        JPanel background = new BgJPanel("/Background.png",GUI.width-300, GUI.height);
        background.setPreferredSize(new Dimension(GUI.gameWidth, GUI.gameHeight));

        JPanel waitingPanel = new JPanel();
        waitingPanel.setLayout(new BoxLayout(waitingPanel, BoxLayout.Y_AXIS));

        JLabel waitingPlayer = new JLabel("Please wait while all the other players end their own turn...");
        waitingPlayer.setFont(new Font("Times New Roman", Font.ITALIC, 32));
        waitingPlayer.setForeground(new Color(220, 179, 120));

        JPanel gifPanel = new JPanel();
        Icon imgIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/gif/dog.gif")));
        JLabel waitingGif = new JLabel(imgIcon);
        gifPanel.setOpaque(false);
        gifPanel.add(Box.createRigidArea(new Dimension(60,0)));

        gifPanel.add(waitingGif);

        waitingPanel.add(waitingPlayer);
        waitingPanel.add(gifPanel);
        waitingPanel.setOpaque(false);

        List<String> players = new ArrayList<>(gui.model.getPlayers().keySet());
        players.remove(gui.model.getMe());
        players.remove("Lorenzo il Magnifico");

        if (!players.isEmpty()) {
            JButton viewPlayer = new JButton("View other Player");
            viewPlayer.addActionListener(e -> {
                String toSee = (String) JOptionPane.showInputDialog(null, "Who do you want to see?", "View other player",
                        JOptionPane.QUESTION_MESSAGE, null,
                        players.toArray(), players.get(0));
                if (toSee != null) {
                    gui.switchPanels(new OtherPlayersPanel(gui, toSee, true));
                }
            });
            waitingPanel.add(viewPlayer);
        }

        background.add(Box.createRigidArea(new Dimension(0,800)));
        background.add(waitingPanel);

        return background;
    }

}
