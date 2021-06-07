package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.model.resource.ResourceType;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class LeaderDepotPanel extends JPanel {


    public static HashMap<ResourceType, String> depotBackground = new HashMap<>(){{
        put(ResourceType.COIN, "/LeaderCardsImages/addDepotsImages/addCoin.png");
        put(ResourceType.SHIELD, "/LeaderCardsImages/addDepotsImages/addShield.png");
        put(ResourceType.STONE, "/LeaderCardsImages/addDepotsImages/addStone.png" );
        put(ResourceType.SERVANT, "/LeaderCardsImages/addDepotsImages/addServant.png");
    }};


    private Image background;

    /**
     * Creates a new <code>JPanel</code> with a double buffer
     * and a flow layout.
     */
    public LeaderDepotPanel(ResourceType resourceType) {
        InputStream is = getClass().getResourceAsStream(depotBackground.get(resourceType));
        if (is == null) try {
            throw new IOException("Background image was not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            background = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.setPreferredSize(new Dimension(200,50));
    }

    /**
     * Draw the background
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = 200;
        int height = 90;
        g.drawImage(background, 15, -15,width,height, null);
    }
}
