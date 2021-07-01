package it.polimi.ingsw.view.gui.panels.movePanels;
import it.polimi.ingsw.view.gui.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WarehouseMoveDepot implements ActionListener {

    private final GUI gui;

    public WarehouseMoveDepot(GUI gui) {
        this.gui = gui;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "depotResourcePressed" -> {}

            case "middleResourcePressed" -> {}

            case "bottomResourcePressed" -> {}
        }
    }
}
