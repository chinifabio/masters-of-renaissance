package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.model.resource.ResourceType;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * This class is the GUI Panel of the extra Depots created after the activation of the Special Ability
 */
public class LeaderDepotPanel extends JPanel {

    /**
     * This attribute is the map of images for the extra Depots, differentiated by the type of resources they can contain
     */
    public static HashMap<ResourceType, String> depotBackground = new HashMap<>(){{
        put(ResourceType.COIN, "/LeaderCardsImages/addDepotsImages/addCoin.png");
        put(ResourceType.SHIELD, "/LeaderCardsImages/addDepotsImages/addShield.png");
        put(ResourceType.STONE, "/LeaderCardsImages/addDepotsImages/addStone.png" );
        put(ResourceType.SERVANT, "/LeaderCardsImages/addDepotsImages/addServant.png");
    }};

    /**
     * This attribute is the background image
     */
    private Image background;

    /**
     * This is the constructor of the class
     * @param resourceType is the resource type of the Extra Depot
     */
    public LeaderDepotPanel(ResourceType resourceType) {
        InputStream is = getClass().getResourceAsStream(depotBackground.get(resourceType));
        if (is == null) try {
            throw new IOException("Background image was not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            assert is != null;
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
