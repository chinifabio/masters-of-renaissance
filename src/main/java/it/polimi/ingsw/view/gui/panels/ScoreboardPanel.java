package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.litemodel.Scoreboard;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.panels.graphicComponents.BgJPanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

/**
 * This class is the GUI Panel of the Leaderboard
 */
public class ScoreboardPanel extends GuiPanel {

    /**
     * This is the constructor of the class
     * @param gui is the GUI that contains all the needed info
     */
    public ScoreboardPanel(GUI gui) {
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

        JPanel totalPanel = new JPanel();
        totalPanel.setLayout(new BoxLayout(totalPanel, BoxLayout.Y_AXIS));
        totalPanel.add(Box.createRigidArea(new Dimension(0,50)));
        totalPanel.setOpaque(false);
        JPanel leaderBoard = new JPanel();
        JLabel ranking = new JLabel();
        Icon scoreIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/Leaderboard.png")));
        ranking.setIcon(scoreIcon);
        leaderBoard.add(ranking);
        leaderBoard.setOpaque(false);
        totalPanel.add(leaderBoard);
        totalPanel.add(Box.createRigidArea(new Dimension(0,50)));


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(0,3));
        mainPanel.setOpaque(false);



        boolean firstPlayer = true;
        for (Scoreboard.BoardEntry entry : gui.model.getScoreboard().getBoard()) {
            JLabel trophy = new JLabel();
            JLabel name = new JLabel();
            name.setText(entry.getNickname());
            JLabel points = new JLabel();
            points.setText(String.valueOf(entry.getScore()));

            if (firstPlayer){
                Icon imgIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/trophy.png")));
                trophy.setIcon(imgIcon);
                name.setFont(new Font("Times New Roman",Font.ITALIC,32));
                name.setForeground(GUI.borderColor);
                points.setFont(new Font("Times New Roman",Font.ITALIC,32));
                points.setForeground(Color.RED);
            } else {
                name.setFont(new Font("Times New Roman",Font.ITALIC,26));
                name.setForeground(GUI.borderColor);
                points.setFont(new Font("Times New Roman",Font.ITALIC,26));
                points.setForeground(GUI.borderColor);
            }



            mainPanel.add(trophy);
            mainPanel.add(name);
            if (entry.getScore() > -1) {
                mainPanel.add(points);
            }
            firstPlayer = false;
        }

        totalPanel.add(mainPanel);
        background.add(totalPanel);
        return background;
    }


}
