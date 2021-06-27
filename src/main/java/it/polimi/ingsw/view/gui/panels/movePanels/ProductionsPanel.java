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

/**
 * This class is the GUI Panel for the Productions
 */
public class ProductionsPanel extends GuiPanel {

    /**
     * This is the constructor of the class
     * @param gui is the GUI that contains all the info needed
     */
    public ProductionsPanel(GUI gui) {
        super(gui);
    }

    /**
     * This method update the current panel after a change
     *
     * @return the current Panel updated
     * @throws IOException if there is an I/O problem
     */
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

/**
 * This class is the GUI Panel of the single Production
 */
class ProdPanel extends JPanel {

    /**
     * This is the constructor of the class
     * @param name is the ID of the Production
     * @param prod is the Production that will be showed
     * @param gui is the GUI that contains all the Production's info
     * @throws IOException if there is an I/O problem
     */
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
        JPanel added = new JPanel();
        added.setLayout(new BoxLayout(added, BoxLayout.X_AXIS));
        added.setOpaque(false);
        for(LiteResource p : prod.getAdded()) added.add(new ResourceLabel(p.getType(), p.getAmount()));
        add(added);
        setBorder(BorderFactory.createLineBorder(GUI.borderColor, 2));
    }
}

/**
 * This class is the GUI Panel of the Productions' Book
 */
class BookBackgroundPanel extends JPanel {

    /**
     * This attribute is the background image
     */
    private final Image bg;

    /**
     * This attribute is the width of the image
     */
    public static final int w = 373/2;

    /**
     * This attribute is the height of the image
     */
    public static final int h = 261/2;

    /**
     * This is the constructor of the class
     * @throws IOException if there is an I/O problem
     */
    public BookBackgroundPanel() throws IOException {
        InputStream img = this.getClass().getResourceAsStream("/production.png");
        assert img != null;
        bg = GUI.getScaledImage(ImageIO.read(img), w, h);
        setOpaque(false);
    }

    /**
     * Draw the background
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bg, 0, 0, w, h, null);
    }
}

/**
 * This class is the GUI Panel of the Resources
 */
class ResourceLabel extends JPanel {
    /**
     * This is the constructor of the class
     * @param type is the Resource type
     * @param am is the amount of the resource
     * @throws IOException if there is an I/O problem
     */
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

/**
 * This class is the GUI Panel of the Production Normalizer
 */
class ProductionNormalizer extends GuiPanel {

    /**
     * This attribute is the List of resources required for the production
     */
    private final List<LiteResource> required;

    /**
     * This attribute is the List of resources that the player will obtain after the production
     */
    private final List<LiteResource> output;

    /**
     * This attribute is the ID of the Production to normalize
     */
    private final ProductionID id;

    /**
     * This attribute is the list of Resources that the player can choose
     */
    List<String> possibleValues = new ArrayList<>();

    /**
     * This is the constructor of the class
     * @param gui is the GUI that contains all the info
     * @param required is the list of resources required
     * @param output is the list of resources that the player could obtain
     * @param id is the ID of the Production to normalize
     * @throws IOException if there is an I/O problem
     */
    public ProductionNormalizer(GUI gui, List<LiteResource> required, List<LiteResource> output, ProductionID id) throws IOException {
        super(gui);

        this.required = required;
        this.output = output;

        this.id = id;
    }

    /**
     * This method create the JButton of the resource
     * @param type is the Resource type
     * @return the JButton of the Resource created
     * @throws IOException if there is an I/O problem
     */
    public JButton resourceButtonProd(ResourceType type) throws IOException {
        JButton butt = new JButton();

        InputStream img = getClass().getResourceAsStream("/WarehouseRes/"+type.name().toLowerCase()+".png");
        assert img != null;
        butt.setIcon(new ImageIcon(GUI.getScaledImage(ImageIO.read(img), 25, 25)));
        butt.setAlignmentX(Component.CENTER_ALIGNMENT);

        return butt;
    }

    /**
     * This method update the current panel after a change
     *
     * @return the current Panel updated
     * @throws IOException if there is an I/O problem
     */
    @Override
    public JPanel update() throws IOException {
        JPanel background = new BgJPanel("/Background.png",GUI.width-300, GUI.height);
        background.setPreferredSize(new Dimension(GUI.gameWidth, GUI.gameHeight));
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
        JLabel text = new JLabel("Click on resource to normalize the unknown");
        text.setForeground(Color.WHITE);
        result.add(text);
        mainPanel.add(req);
        mainPanel.add(Box.createRigidArea(new Dimension(50,0)));
        mainPanel.add(out);
        mainPanel.setOpaque(false);
        result.setOpaque(false);
        result.add(mainPanel);
        result.add(confirm);
        background.add(result);

        return background;
    }
}
