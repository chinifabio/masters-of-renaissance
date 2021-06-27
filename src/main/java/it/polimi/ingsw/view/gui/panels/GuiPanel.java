package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.view.gui.GUI;

import javax.swing.*;
import java.io.IOException;

/**
 * This class is an improving of a Java Panel that contains a GUI attribute and the method update
 */
public abstract class GuiPanel {

    /**
     * This attribute is the GUI that contains all the info needed
     */
    protected final GUI gui;

    /**
     * This is the constructor of the class
     * @param gui is the GUI that contains the info needed
     */
    public GuiPanel(GUI gui) {
        this.gui = gui;
    }

    /**
     * This method update the current panel after a change
     * @return the current Panel updated
     * @throws IOException if there is an I/O problem
     */
    public abstract JPanel update() throws IOException;
}
