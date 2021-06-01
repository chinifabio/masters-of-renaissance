package it.polimi.ingsw.view.gui.panels;

import javax.swing.*;
import java.awt.*;

public class WarehousePanel  extends JPanel {


    /**
     * Creates a new <code>JPanel</code> with a double buffer
     * and a flow layout.
     */
    public WarehousePanel() {
        this.setPreferredSize(new Dimension(320,400));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.yellow);
    }
}
