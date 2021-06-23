package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.litemodel.Scoreboard;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.panels.graphicComponents.BgJPanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ScoreboardPanel extends GuiPanel {

    public ScoreboardPanel(GUI gui) {
        super(gui);
    }

    @Override
    public JPanel update() throws IOException {
        JPanel background = new BgJPanel("/Background.png",GUI.width-300, GUI.height);
        background.setPreferredSize(new Dimension(GUI.gameWidth, GUI.gameHeight));

        System.out.println(gui.model.getScoreboard());

        return background;
    }

}
