package it.polimi.ingsw.view.gui.panels.movePanels;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.ActivateProductionCommand;
import it.polimi.ingsw.communication.packet.commands.ReturnCommand;
import it.polimi.ingsw.communication.packet.commands.SetNormalProductionCommand;
import it.polimi.ingsw.litemodel.LiteResource;
import it.polimi.ingsw.litemodel.litewarehouse.LiteProduction;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalTypeInProduction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.NormalProduction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.panels.GuiPanel;
import it.polimi.ingsw.view.gui.panels.PersonalBoardPanel;
import it.polimi.ingsw.view.gui.panels.graphicComponents.BgJPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;

public class ProductionsPanel extends GuiPanel {

    public ProductionsPanel(GUI gui) {
        super(gui);
    }

    @Override
    public JPanel update() throws IOException {
        JPanel background = new BgJPanel("/brickBackground.png",GUI.width-370, GUI.height-78,35,35);
        JPanel result = new JPanel();

        background.setPreferredSize(new Dimension(GUI.gameWidth, GUI.gameHeight));

        result.setLayout(new BoxLayout(result, BoxLayout.Y_AXIS));
        result.setOpaque(false);

        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.X_AXIS));
        middlePanel.setOpaque(false);

        JPanel buttonPanel = new JPanel();
        //--------BACK BUTTON----------
        JButton back = new JButton("Return to PB");
        back.addActionListener(e -> {
            gui.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new ReturnCommand().jsonfy()));
            gui.switchPanels(new PersonalBoardPanel(gui));
        });
        buttonPanel.add(back);
        buttonPanel.setOpaque(false);

        //--------ACTIVATE PRODUCTION BUTTON----------
        JButton activate = new JButton("Activate productions");
        activate.addActionListener(e -> {
            gui.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new ActivateProductionCommand().jsonfy()));
            gui.switchPanels(new PersonalBoardPanel(gui));
        });
        buttonPanel.add(activate);
        buttonPanel.add(Box.createRigidArea(new Dimension(800,0)));
        buttonPanel.setOpaque(false);

        //--------PRODS PANEL----------
        JPanel prodPanel = new JPanel();
        Map<ProductionID, LiteProduction> prods = gui.model.getAllProductions(gui.model.getMe());
        for (Map.Entry<ProductionID, LiteProduction> entry : prods.entrySet()) {
            JPanel prod = new ProdPanel(entry.getKey(), entry.getValue(), gui);
            prod.setOpaque(false);
            prodPanel.add(new JPanel().add(prod));
        }
        prodPanel.setOpaque(false);

        //JPanel activeProdPanel = new JPanel();
        //for (Map.Entry<ProductionID, LiteProduction> entry : prods.entrySet()) {
        //    JPanel prod = new ActivePanel(entry.getKey(), entry.getValue(), gui);
        //    prod.setOpaque(false);
        //    activeProdPanel.add(new JPanel().add(prod));
        //}
        //activeProdPanel.setOpaque(false);

        //------------BUFFER-------------
        //JPanel bufferContainer = new JPanel();
        //JPanel buffer = new BufferMovePanel(gui);
        //bufferContainer.setLayout(new BoxLayout(bufferContainer, BoxLayout.Y_AXIS));
        //buffer.setPreferredSize(new Dimension(275,100));
        //bufferContainer.setOpaque(false);
        //bufferContainer.add(Box.createRigidArea(new Dimension(0,200)));
        //bufferContainer.add(buffer);

        JPanel bufferPanel = new JPanel();
        //bufferPanel.add(bufferContainer);
        bufferPanel.setOpaque(false);

        //----------WareHouse------------
        JPanel warehousPanel = new JPanel();
        ProductionMovePanel warehouse = new ProductionMovePanel(gui);
        warehouse.setPreferredSize(new Dimension(350, 535));
        warehousPanel.add(warehouse);
        warehousPanel.setOpaque(false);

        //--------ExtraDepot------------
        JPanel extraPanel = new JPanel();
        ExtraDepotProductionPanel extraDepotPanel = new ExtraDepotProductionPanel(gui);
        extraPanel.setPreferredSize(new Dimension(350,100));
        extraDepotPanel.setOpaque(false);
        extraPanel.add(extraDepotPanel);
        extraPanel.setOpaque(false);

        JPanel depotAndBufferPanel = new JPanel();
        depotAndBufferPanel.setLayout(new BoxLayout(depotAndBufferPanel, BoxLayout.Y_AXIS));
        depotAndBufferPanel.add(extraPanel);
        depotAndBufferPanel.add(bufferPanel);
        depotAndBufferPanel.setOpaque(false);


        result.add(buttonPanel);
        result.add(prodPanel);
        //result.add(activeProdPanel);
        middlePanel.add(warehousPanel);
        middlePanel.add(Box.createRigidArea(new Dimension(100, 0)));
        middlePanel.add(depotAndBufferPanel);
        result.add(middlePanel);

        background.setOpaque(false);
        result.setOpaque(false);
        background.add(result);
        return background;
    }
}

class ProdPanel extends JPanel {

    public ProdPanel(ProductionID name, LiteProduction prod, GUI gui) throws IOException {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);

        // placing the name of the production
        JLabel nameLabel = new JLabel(name.name());
        nameLabel.setAlignmentX(CENTER_ALIGNMENT);
        nameLabel.setForeground(Color.WHITE);
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
        else{
            add(Box.createRigidArea(new Dimension(0, 26)));
        }

        for(LiteResource p : prod.getAdded()) add(new ResourceLabel(p.getType(), p.getAmount()));

        setBorder(BorderFactory.createLineBorder(GUI.borderColor, 2));
    }
}

class ActivePanel extends JPanel{

    public ActivePanel(ProductionID name, LiteProduction prod, GUI gui) throws IOException {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);

        // render of the added productions

        JPanel book = new JPanel();

        JPanel added = new JPanel();
        added.setLayout(new GridLayout(0, 1));
        added.setOpaque(false);
        for(LiteResource p : prod.getAdded()) added.add(new ResourceLabel(p.getType(), p.getAmount()));

        book.add(added);
        book.setOpaque(false);

        book.setAlignmentX(CENTER_ALIGNMENT);
        add(book);

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
    private final ProductionID id;
    List<String> possibleValues = new ArrayList<>();

    public ProductionNormalizer(GUI gui, List<LiteResource> required, List<LiteResource> output, ProductionID id) throws IOException {
        super(gui);

        this.required = required;
        this.output = output;

        this.id = id;
    }

    @Override
    public JPanel update() throws IOException {
        JPanel result = new JPanel();

        for (ResourceType storable : ResourceType.storable()) possibleValues.add(storable.name());

        //setLayout(new GridLayout(0, 1));
        result.setLayout(new BoxLayout(result, BoxLayout.Y_AXIS));
        result.add(Box.createRigidArea(new Dimension(0,200)));

        //--------BACK BUTTON----------
        JPanel backPanel = new JPanel();
        JButton back = new JButton("Back");
        back.addActionListener(e -> gui.switchPanels(new ProductionsPanel(gui)));
        backPanel.add(back);
        backPanel.setOpaque(false);

        JPanel backgroundPanel = new BookBackgroundPanel();
        JPanel mainPanel = new JPanel();
        mainPanel.add(backgroundPanel);

        JPanel req = new JPanel();
        req.setLayout(new BoxLayout(req, BoxLayout.Y_AXIS));
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

        req.setOpaque(false);
        for(LiteResource p : required) {
            if (p.getType() == ResourceType.UNKNOWN){
                for (int i = 0; i < p.getAmount(); i++){
                    JButton resource = resourceButtonProd(p.getType());
                    resource.setOpaque(false);
                    resource.setContentAreaFilled(false);
                    resource.addActionListener(e -> {
                        String chosenResource = (String) JOptionPane.showInputDialog(null, "Choose the Resource", "Normalize",
                                JOptionPane.QUESTION_MESSAGE, null,
                                possibleValues.toArray(), possibleValues.get(0));
                        if (chosenResource != null){
                            newReq.add(ResourceBuilder.buildFromType(ResourceType.valueOf(chosenResource), 1).liteVersion());
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

        out.setLayout(new BoxLayout(out, BoxLayout.Y_AXIS));

        //out.setLayout(new GridLayout(1, 0));
        out.setOpaque(false);
        for(LiteResource p : output) {
            if (p.getType() == ResourceType.UNKNOWN){
            for (int i = 0; i < p.getAmount(); i++){
                JButton resource = resourceButtonProd(p.getType());
                resource.setOpaque(false);
                resource.setContentAreaFilled(false);
                resource.addActionListener(e -> {
                    String chosenResource = (String) JOptionPane.showInputDialog(null, "Choose the Resource", "Normalize",
                            JOptionPane.QUESTION_MESSAGE, null,
                            possibleValues.toArray(), possibleValues.get(0));
                    if (chosenResource != null){
                        newOut.add(ResourceBuilder.buildFromType(ResourceType.valueOf(chosenResource), 1).liteVersion());
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
                gui.switchPanels(new ProductionsPanel(gui));
            } catch (IllegalTypeInProduction illegalTypeInProduction) {
                JOptionPane.showMessageDialog(null, "Please, complete the process");
                illegalTypeInProduction.printStackTrace();
            }
        });

        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1, 0));
        buttons.setOpaque(false);
        buttons.add(confirm);

        //result.setOpaque(false);
        result.add(backPanel);
        result.add(new JLabel("Click on resource to normalize the unknown"));
        mainPanel.add(req);
        mainPanel.add(Box.createRigidArea(new Dimension(50,0)));
        mainPanel.add(out);
        mainPanel.setOpaque(false);
        result.add(mainPanel);
        result.add(confirm);

        return result;
    }

    public JButton resourceButtonProd(ResourceType type) throws IOException {
        JButton butt = new JButton();

        InputStream img = getClass().getResourceAsStream("/WarehouseRes/"+type.name().toLowerCase()+".png");
        assert img != null;
        butt.setIcon(new ImageIcon(GUI.getScaledImage(ImageIO.read(img), 25, 25)));
        butt.setAlignmentX(Component.CENTER_ALIGNMENT);

        return butt;
    }
}