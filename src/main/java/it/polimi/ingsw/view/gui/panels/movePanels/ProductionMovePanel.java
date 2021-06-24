package it.polimi.ingsw.view.gui.panels.movePanels;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.MoveDepotCommand;
import it.polimi.ingsw.communication.packet.commands.MoveInProductionCommand;
import it.polimi.ingsw.litemodel.LiteResource;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.Production;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.view.gui.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProductionMovePanel extends JPanel{

    private ProductionID destProd;

    private Image warehouseImage;

    private final GUI gui;

    /**
     * Creates a new <code>JPanel</code> with a double buffer
     * and a flow layout.
     */
    public ProductionMovePanel(GUI gui) throws IOException {
        this.gui = gui;

        InputStream is = getClass().getResourceAsStream("/warehouse.png");
        assert is != null;
        warehouseImage = ImageIO.read(is);

        this.setPreferredSize(new Dimension(320,600));
        this.setOpaque(false);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));



        JPanel topDepot = new JPanel();
        topDepot.setPreferredSize(new Dimension(75, 30));

        insertResourceInDepot(topDepot, DepotSlot.TOP);

        topDepot.setOpaque(false);



        JPanel middleDepot = new JPanel();

        insertResourceInDepot(middleDepot, DepotSlot.MIDDLE);

        middleDepot.setOpaque(false);
        middleDepot.setPreferredSize(new Dimension(75, 30));



        JPanel bottomDepot = new JPanel();

        insertResourceInDepot(bottomDepot, DepotSlot.BOTTOM);

        bottomDepot.setOpaque(false);
        bottomDepot.setPreferredSize(new Dimension(75, 30));



        JPanel bigStrong = new JPanel();

        JPanel strongbox = new JPanel();
        strongbox.setLayout(new GridLayout(2,2));

        for (LiteResource res : gui.model.getDepot(gui.model.getMe(), DepotSlot.STRONGBOX).getResourcesInside()){
            JPanel resource = new JPanel();
            resource.setLayout(new OverlayLayout(resource));
            resource.setOpaque(false);
            JLabel amount = new JLabel();
            amount.setFont(new Font(amount.getName(), Font.BOLD, 20));
            if (res.getType() == ResourceType.SERVANT){
                amount.setForeground(Color.WHITE);
            } else {
                amount.setForeground(Color.BLACK);
            }

            amount.setBackground(new Color(0, 0, 0, 10));
            amount.setText(String.valueOf(res.getAmount()));

            JButton image = new JButton();
            createResourceLabel(image, GUI.resourceImages.get(res.getType()));
            image.setActionCommand("depotResourcePressed");
            image.addActionListener(e -> {
                destProd = null;

                List<ProductionID> possibleValuesProductions = new ArrayList<>();

                gui.getModel().getAllProductions(gui.getModel().getMe()).forEach((k,v) -> possibleValuesProductions.add(k));

                ProductionID destProd = (ProductionID) JOptionPane.showInputDialog(null, "Where do you want to move this resource? ", "Move resources",
                        JOptionPane.QUESTION_MESSAGE, null,
                        possibleValuesProductions.toArray(), possibleValuesProductions.get(0));
                if (destProd != null) {
                    gui.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new MoveInProductionCommand(DepotSlot.STRONGBOX, destProd, ResourceBuilder.buildFromType(res.getType(), 1)).jsonfy()));
                }
            });
            resource.add(amount);
            image.setAlignmentX(0.31f);
            image.setAlignmentY(0.55f);
            resource.add(image);

            strongbox.add(resource);
        }

        strongbox.setOpaque(false);

        strongbox.setPreferredSize(new Dimension(270, 120));

        bigStrong.add(strongbox);
        bigStrong.setOpaque(false);

        JPanel finale = new JPanel();
        finale.setLayout(new BoxLayout(finale,BoxLayout.Y_AXIS));
        finale.setOpaque(false);

        finale.add(topDepot);
        finale.add(Box.createRigidArea(new Dimension(320, 20)));
        finale.add(middleDepot);
        finale.add(Box.createRigidArea(new Dimension(320, 20)));
        finale.add(bottomDepot);
        finale.add(Box.createRigidArea(new Dimension(320, 40)));
        finale.add(bigStrong);

        this.add(Box.createRigidArea(new Dimension(100, 110)));
        this.add(finale);
    }

    public void createResourceLabel(JButton button, String resource){
        InputStream url = this.getClass().getResourceAsStream("/" + resource);
        BufferedImage img = null;
        try {
            assert url != null;
            img = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Image scaledImage = GUI.getScaledImage(img, 55, 55);
        ImageIcon icon1 = new ImageIcon(scaledImage);
        button.setIcon(icon1);
        button.setPreferredSize(new Dimension(57, 57));
        button.setContentAreaFilled(false);
    }

    public void insertResourceInDepot(JPanel depot, DepotSlot slot){
        depot.add(Box.createRigidArea(new Dimension(35,0)));
        LiteResource tempRes = gui.model.getDepot(gui.getModel().getMe(), slot).getResourcesInside().get(0);

        for (int i = 0; i <  tempRes.getAmount(); i++){
            JButton label = new JButton();
            createResourceLabel(label, GUI.resourceImages.get(tempRes.getType()));
            if (slot != DepotSlot.STRONGBOX){

                label.setActionCommand("depotResourcePressed");
                label.addActionListener(e -> {
                    destProd = null;

                    List<ProductionID> possibleValuesProductions = new ArrayList<>();

                    gui.getModel().getAllProductions(gui.getModel().getMe()).forEach((k,v) -> possibleValuesProductions.add(k));

                    ProductionID destProd = (ProductionID) JOptionPane.showInputDialog(null, "Where do you want to move this resource? ", "Move resources",
                            JOptionPane.QUESTION_MESSAGE, null,
                            possibleValuesProductions.toArray(), possibleValuesProductions.get(0));
                    if (destProd != null) {
                        gui.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new MoveInProductionCommand(slot, destProd, ResourceBuilder.buildFromType(tempRes.getType(), 1)).jsonfy()));
                    }
                });
            }
            depot.add(label);
        }
    }

    /**
     * Draw the background
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = 320;
        int height = 500;
        g.drawImage(warehouseImage, 0, 50, width, height, null);
    }

}
