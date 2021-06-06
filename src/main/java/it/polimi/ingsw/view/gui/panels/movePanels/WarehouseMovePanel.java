package it.polimi.ingsw.view.gui.panels.movePanels;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.MoveDepotCommand;
import it.polimi.ingsw.litemodel.LiteResource;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.panels.GuiPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class WarehouseMovePanel  extends GuiPanel {


    private boolean toPaint = false;

    private DepotSlot destDepot;

    DepotSlot[] possibleValuesDepots = { DepotSlot.TOP, DepotSlot.MIDDLE, DepotSlot.BOTTOM, DepotSlot.BUFFER, DepotSlot.SPECIAL1, DepotSlot.SPECIAL2};

    DepotSlot[] possibleValuesStrongbox = {DepotSlot.BUFFER};

    private Image warehouseImage;

    /**
     * Creates a new <code>JPanel</code> with a double buffer
     * and a flow layout.
     */
    public WarehouseMovePanel(GUI gui){
        super(gui);

        InputStream is = getClass().getResourceAsStream("/warehouse.png");
        if (is == null) try {
            throw new IOException("warehouse.png not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            warehouseImage = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.setPreferredSize(new Dimension(320,400));
        this.setOpaque(false);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));



        JPanel topDepot = new JPanel();
        topDepot.setPreferredSize(new Dimension(75, 10));

        insertResourceInDepot(topDepot, DepotSlot.TOP);

        topDepot.setOpaque(false);



        JPanel middleDepot = new JPanel();

        insertResourceInDepot(middleDepot, DepotSlot.MIDDLE);

        middleDepot.setOpaque(false);
        middleDepot.setPreferredSize(new Dimension(75, 10));



        JPanel bottomDepot = new JPanel();

        insertResourceInDepot(bottomDepot, DepotSlot.BOTTOM);

        bottomDepot.setOpaque(false);
        bottomDepot.setPreferredSize(new Dimension(75, 10));




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
            image.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int value = 0;
                    boolean valid = true;

                    String str = JOptionPane.showInputDialog(null, "How many resources do you want to move?");
                    try{
                        value = Integer.parseInt(str);
                    } catch (Exception notParsable){
                        JOptionPane.showMessageDialog(null, "Please use a valid number!");
                        valid = false;
                    }

                    if (valid) {
                        gui.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new MoveDepotCommand(DepotSlot.STRONGBOX, DepotSlot.BUFFER, ResourceBuilder.buildFromType(res.getType(), value)).jsonfy()));
                    }
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

        this.add(topDepot);
        this.add(middleDepot);
        this.add(bottomDepot);
        this.add(Box.createRigidArea(new Dimension(320, 40)));
        this.add(bigStrong);

    }

    @Override
    public void reactToPacket(Packet packet) throws IOException {
        switch (packet.header) {
            case OK -> gui.switchPanels(new WarehouseMovePanel(gui));
            case INVALID -> gui.notifyPlayerError(packet.body);
        }
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
                label.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        destDepot = null;
                        DepotSlot destDepot = (DepotSlot) JOptionPane.showInputDialog(null, "Where do you wanto to move this resource? ", "Move resources",
                                JOptionPane.QUESTION_MESSAGE, null,
                                possibleValuesDepots, possibleValuesDepots[0]);
                        if (destDepot != null) {
                            gui.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new MoveDepotCommand(slot, destDepot, ResourceBuilder.buildFromType(tempRes.getType(), 1)).jsonfy()));
                        }
                    }
                });
            }
            depot.add(label);
        }
    }

    public void setPainted(){
        this.toPaint = true;
    }

    /**
     * Draw the background
     */
    @Override
    protected void paintComponent(Graphics g) {
        if (toPaint) {
            super.paintComponent(g);

            int width = 320;
            int height = 500;
            g.drawImage(warehouseImage, 0, -55, width, height, null);
        }
    }
}