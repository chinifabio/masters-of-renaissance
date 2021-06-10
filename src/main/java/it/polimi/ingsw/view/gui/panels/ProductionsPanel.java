package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.SetNormalProductionCommand;
import it.polimi.ingsw.litemodel.LiteResource;
import it.polimi.ingsw.litemodel.litewarehouse.LiteProduction;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalTypeInProduction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.NormalProduction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.panels.movePanels.BufferMovePanel;
import it.polimi.ingsw.view.gui.panels.movePanels.ExtraDepotMovePanel;
import it.polimi.ingsw.view.gui.panels.movePanels.MoveResourcesPanel;
import it.polimi.ingsw.view.gui.panels.movePanels.WarehouseMovePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ProductionsPanel extends GuiPanel{

    public ProductionsPanel(GUI gui) throws IOException {
        super(gui);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);

        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.X_AXIS));
        middlePanel.setOpaque(false);

        //--------BACK BUTTON----------
        JPanel backPanel = new JPanel();
        JButton back = new JButton("Return to PB");
        back.addActionListener(e -> {
            try {
                gui.switchPanels(new PersonalBoardPanel(gui));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        backPanel.add(back);
        backPanel.add(Box.createRigidArea(new Dimension(900,0)));
        backPanel.setOpaque(false);

        //--------PRODS PANEL----------
        JPanel prodPanel = new JPanel();
        Map<ProductionID, LiteProduction> prods = gui.model.getAllProductions(gui.model.getMe());
        for (Map.Entry<ProductionID, LiteProduction> entry : prods.entrySet()) {
            JPanel prod = new ProdPanel(entry.getKey(), entry.getValue(), gui);
            prod.setOpaque(false);
            prodPanel.add(new JPanel().add(prod));
        }
        prodPanel.setOpaque(false);


        //------------BUFFER-------------
        JPanel bufferContainer = new JPanel();
        JPanel buffer = new BufferMovePanel(gui);
        bufferContainer.setLayout(new BoxLayout(bufferContainer, BoxLayout.Y_AXIS));
        buffer.setPreferredSize(new Dimension(275,100));
        bufferContainer.setOpaque(false);
        bufferContainer.add(Box.createRigidArea(new Dimension(0,200)));
        bufferContainer.add(buffer);

        JPanel bufferPanel = new JPanel();
        bufferPanel.add(bufferContainer);
        bufferPanel.setOpaque(false);

        //----------WareHouse------------
        JPanel warehousPanel = new JPanel();
        WarehouseMovePanel warehouse = new WarehouseMovePanel(gui);
        warehouse.setPreferredSize(new Dimension(350, 500));
        warehousPanel.add(warehouse);
        warehousPanel.setOpaque(false);

        //--------ExtraDepot------------
        JPanel extraPanel = new JPanel();
        ExtraDepotMovePanel extraDepotPanel = new ExtraDepotMovePanel(gui);
        extraPanel.setPreferredSize(new Dimension(350,100));
        extraDepotPanel.setOpaque(false);
        extraPanel.add(extraDepotPanel);
        extraPanel.setOpaque(false);

        JPanel depotAndBufferPanel = new JPanel();
        depotAndBufferPanel.setLayout(new BoxLayout(depotAndBufferPanel, BoxLayout.Y_AXIS));
        depotAndBufferPanel.add(extraPanel);
        depotAndBufferPanel.add(bufferPanel);
        depotAndBufferPanel.setOpaque(false);


        this.add(backPanel);
        this.add(prodPanel);
        middlePanel.add(warehousPanel);
        middlePanel.add(Box.createRigidArea(new Dimension(100, 0)));
        middlePanel.add(depotAndBufferPanel);
        this.add(middlePanel);
    }

    @Override
    public void reactToPacket(Packet packet) throws IOException {
        switch (packet.header) {
            case OK -> gui.switchPanels(new MoveResourcesPanel(gui));
            case INVALID -> gui.notifyPlayerError(packet.body);
        }
    }
}

class ProdPanel extends JPanel {

    public ProdPanel(ProductionID name, LiteProduction prod, GUI gui) throws IOException {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);

        // placing the name of the production
        JLabel nameLabel = new JLabel(name.name());
        nameLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(nameLabel);

        // render of the production

        BookBackgroundPanel book = new BookBackgroundPanel();
        book.setLayout(new GridLayout(1, 2));
        book.setPreferredSize(new Dimension(BookBackgroundPanel.w, BookBackgroundPanel.h));

        JPanel req = new JPanel();
        req.setLayout(new GridLayout(0, 1));
        req.setOpaque(false);
        for(LiteResource p : prod.getRequired()) req.add(new ResourceLabel(p.getType(), p.getAmount()));

        JPanel out = new JPanel();
        out.setLayout(new GridLayout(0, 1));
        out.setOpaque(false);
        for(LiteResource p : prod.getOutput()) out.add(new ResourceLabel(p.getType(), p.getAmount()));

        book.add(req);
        book.add(out);

        book.setAlignmentX(CENTER_ALIGNMENT);
        add(book);

        if (prod.isUnknown()) {
            JButton normalizer = new JButton("Normalize");
            normalizer.addActionListener(e -> {
                try {
                    gui.switchPanels(new ProductionNormalizer(gui, prod.getRequired(), prod.getOutput(),name));
                } catch (IOException ioException) {
                    gui.notifyPlayerError("cannot start normalizer");
                }
            });
            normalizer.setAlignmentX(CENTER_ALIGNMENT);
            add(normalizer);
        }

        setBorder(BorderFactory.createLineBorder(GUI.borderColor, 2));
    }
}

class BookBackgroundPanel extends JPanel {

    private final Image bg;

    public static final int w = 373/2;
    public static final int h = 261/2;

    public BookBackgroundPanel() throws IOException {
        InputStream img = this.getClass().getResourceAsStream("/production.png");
        assert img != null;
        bg = GUI.getScaledImage(ImageIO.read(img), w, h);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bg, 0, 0, w, h, null);
    }
}

class ResourceLabel extends JPanel {
    public ResourceLabel(ResourceType type, int am) throws IOException {
        setLayout(new OverlayLayout(this));
        this.setBackground(new Color(0,0,0,30));

        JLabel amount = new JLabel();
        amount.setFont(new Font(amount.getName(), Font.BOLD, 17));
        amount.setForeground(Color.white);

        amount.setText(String.valueOf(am));

        JLabel image = new JLabel();
        InputStream img = getClass().getResourceAsStream("/WarehouseRes/"+type.name().toLowerCase()+".png");
        assert img != null;
        image.setIcon(new ImageIcon(GUI.getScaledImage(ImageIO.read(img), 25, 25)));

        image.setAlignmentX(0.60f);
        image.setAlignmentY(0.70f);
        add(amount);
        add(image);

        setAlignmentX(CENTER_ALIGNMENT);
    }
}


class ProductionNormalizer extends GuiPanel {

    private final List<LiteResource> required;
    private final List<LiteResource> output;
    List<String> possibleValues = new ArrayList<>();

    public ProductionNormalizer(GUI gui, List<LiteResource> required, List<LiteResource> output, ProductionID id) throws IOException {
        super(gui);

        this.required = required;
        this.output = output;

        Map<String, ResourceType> resourcesMap = new HashMap<>(){{ // todo same as line 238 because is better
            put("COIN", ResourceType.COIN);
            put("STONE", ResourceType.STONE);
            put("SHIELD",ResourceType.SHIELD);
            put("SERVANT",ResourceType.SERVANT);
        }};

        for (ResourceType storable : ResourceType.storable()) possibleValues.add(storable.name());

        setLayout(new GridLayout(0, 1));

        //--------BACK BUTTON----------
        JPanel backPanel = new JPanel();
        JButton back = new JButton("Back");
        back.addActionListener(e -> {
            try {
                gui.switchPanels(new ProductionsPanel(gui));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        backPanel.add(back);
        //backPanel.add(Box.createRigidArea(new Dimension(900,0)));
        backPanel.setOpaque(false);

        JPanel req = new JPanel();

        List<LiteResource> newReq = new ArrayList<>();
        for (LiteResource resource : required){
            if (resource.getType() == ResourceType.UNKNOWN) {
                for (int i = 0; i < resource.getAmount(); i++) {
                    newReq.add(ResourceBuilder.buildFromType(resource.getType(), 1).liteVersion());
                }
            } else {
                newReq.add(ResourceBuilder.buildFromType(resource.getType(), resource.getAmount()).liteVersion());
            }
        }


        List<LiteResource> newOut = new ArrayList<>();
        for (LiteResource resource : output){
            if (resource.getType() == ResourceType.UNKNOWN) {
                for (int i = 0; i < resource.getAmount(); i++) {
                    newOut.add(ResourceBuilder.buildFromType(resource.getType(), 1).liteVersion());
                }
            } else {
                newOut.add(ResourceBuilder.buildFromType(resource.getType(), resource.getAmount()).liteVersion());
            }
        }

        req.setLayout(new GridLayout(1, 0));
        req.setOpaque(false);
        for(LiteResource p : required) {
            if (p.getType() == ResourceType.UNKNOWN){
                for (int i = 0; i < p.getAmount(); i++){
                    JButton resource = new JButton();
                    resourceButtonProd(resource,p.getType());
                    resource.addActionListener(e -> {
                        String choosenResource = (String) JOptionPane.showInputDialog(null, "Choose the Resource? ", "Normalize",
                                JOptionPane.QUESTION_MESSAGE, null,
                                possibleValues.toArray(), possibleValues.get(0));
                        if (choosenResource != null){
                            newReq.add(ResourceBuilder.buildFromType(resourcesMap.get(choosenResource), 1).liteVersion());
                            newReq.remove(ResourceBuilder.buildFromType(ResourceType.UNKNOWN,1).liteVersion());
                            try {
                                gui.switchPanels(new ProductionNormalizer(gui, newReq, newOut, id));
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        }
                    });
                    req.add(resource);
                }
            } else {
                ResourceLabel resource = new ResourceLabel(p.getType(), p.getAmount());
                req.add(resource);
            }

        }

        JPanel out = new JPanel();


        out.setLayout(new GridLayout(1, 0));
        out.setOpaque(false);
        for(LiteResource p : output) {
            if (p.getType() == ResourceType.UNKNOWN){
            for (int i = 0; i < p.getAmount(); i++){
                JButton resource = new JButton();
                resourceButtonProd(resource,p.getType());
                resource.addActionListener(e -> {
                    String choosenResource = (String) JOptionPane.showInputDialog(null, "Choose the Resource? ", "Normalize",
                            JOptionPane.QUESTION_MESSAGE, null,
                            possibleValues.toArray(), possibleValues.get(0));
                    if (choosenResource != null){
                        newOut.add(ResourceBuilder.buildFromType(resourcesMap.get(choosenResource), 1).liteVersion());
                        newOut.remove(ResourceBuilder.buildFromType(ResourceType.UNKNOWN,1).liteVersion());
                        try {
                            gui.switchPanels(new ProductionNormalizer(gui, newReq, newOut, id));
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                });
                out.add(resource);
            }
         } else {
                ResourceLabel resource = new ResourceLabel(p.getType(), p.getAmount());
                out.add(resource);
            }
        }

        List<Resource> toSendReq = new ArrayList<>();
        for (LiteResource res : newReq){
            toSendReq.add(ResourceBuilder.buildFromType(res.getType(), res.getAmount()));
        }

        List<Resource> toSendOut = new ArrayList<>();
        for (LiteResource res : newOut){
            toSendOut.add(ResourceBuilder.buildFromType(res.getType(), res.getAmount()));
        }

        JButton confirm = new JButton("Confirm");
        confirm.addActionListener(e -> {
            try {
                gui.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new SetNormalProductionCommand(id, new NormalProduction(toSendReq, toSendOut)).jsonfy()));
            } catch (IllegalTypeInProduction illegalTypeInProduction) {
                JOptionPane.showMessageDialog(null, "Please, complete the process");
                illegalTypeInProduction.printStackTrace();
            }
        });

        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1, 0));
        buttons.setOpaque(false);
        buttons.add(confirm);


        add(backPanel);
        add(new JLabel("Click on resource to normalize the unknown"));
        add(req);
        add(out);
        add(confirm);



    }

    public void resourceButtonProd(JButton image, ResourceType type) throws IOException {
        InputStream img = getClass().getResourceAsStream("/WarehouseRes/"+type.name().toLowerCase()+".png");
        assert img != null;
        image.setIcon(new ImageIcon(GUI.getScaledImage(ImageIO.read(img), 25, 25)));

        add(image);

        setAlignmentX(CENTER_ALIGNMENT);
    }


    @Override
    public void reactToPacket(Packet packet) throws IOException {
        switch (packet.header){
            case OK -> {
                gui.switchPanels(new ProductionsPanel(gui));
                gui.notifyPlayer(packet.body);
            }
            case INVALID -> {
                gui.switchPanels(new ProductionsPanel(gui));
                gui.notifyPlayerError(packet.body);
            }
        }
    }
}
