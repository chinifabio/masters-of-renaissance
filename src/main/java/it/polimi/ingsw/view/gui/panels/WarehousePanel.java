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
        this.setOpaque(false);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel topDepot = new JPanel();
        topDepot.setBackground(Color.yellow);

        JPanel middleDepot = new JPanel();
        middleDepot.setBackground(Color.pink);


        JPanel bottomDepot = new JPanel();
        bottomDepot.setBackground(Color.magenta);


        JPanel strongbox = new JPanel();
        strongbox.setPreferredSize(new Dimension(320, 150));
        strongbox.setBackground(Color.blue.brighter());

        this.add(topDepot);

        this.add(middleDepot);

        this.add(bottomDepot);

        this.add(Box.createRigidArea(new Dimension(320, 20)));
        this.add(strongbox);
        this.add(Box.createRigidArea(new Dimension(320, 10)));
    }
}
