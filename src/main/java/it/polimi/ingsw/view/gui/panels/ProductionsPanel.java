package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.litemodel.LiteResource;
import it.polimi.ingsw.litemodel.litewarehouse.LiteProduction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;
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
import java.util.Map;
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



        //--------PRODS PANEL----------
        JPanel prodPanel = new JPanel();
        Map<ProductionID, LiteProduction> prods = gui.model.getAllProductions(gui.model.getMe());
        for (Map.Entry<ProductionID, LiteProduction> entry : prods.entrySet()) {
            JPanel prod = new ProdPanel(entry.getKey().name(), entry.getValue(), gui);
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

    public ProdPanel(String name, LiteProduction prod, GUI gui) throws IOException {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);

        // placing the name of the production
        JLabel nameLabel = new JLabel(name);
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
                    new ProductionNormalizer(prod.getRequired(), prod.getOutput());
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
        setOpaque(false);

        JLabel amount = new JLabel();
        amount.setFont(new Font(amount.getName(), Font.BOLD, 17));
        amount.setForeground(Color.GREEN);

        amount.setBackground(new Color(0, 0, 0, 10));
        amount.setText(String.valueOf(am));

        JLabel image = new JLabel();
        InputStream img = getClass().getResourceAsStream("/WarehouseRes/"+type.name().toLowerCase()+".png");
        assert img != null;
        image.setIcon(new ImageIcon(GUI.getScaledImage(ImageIO.read(img), 25, 25)));

        image.setAlignmentX(0.31f);
        image.setAlignmentY(0.55f);
        add(amount);
        add(image);

        setAlignmentX(CENTER_ALIGNMENT);
    }
}

class ProductionNormalizer extends JFrame implements ActionListener {

    private final List<LiteResource> required;
    private final List<LiteResource> output;


    public ProductionNormalizer(List<LiteResource> required, List<LiteResource> output) throws IOException {
        super("Production Normalizer");

        this.required = required;
        this.output = output;

        setLayout(new GridLayout(0, 1));


        JPanel req = new JPanel();
        req.setLayout(new GridLayout(1, 0));
        req.setOpaque(false);
        for(LiteResource p : required) req.add(new ResourceLabel(p.getType(), p.getAmount()));

        JPanel out = new JPanel();
        out.setLayout(new GridLayout(1, 0));
        out.setOpaque(false);
        for(LiteResource p : output) out.add(new ResourceLabel(p.getType(), p.getAmount()));

        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1, 0));
        buttons.setOpaque(false);
        //for(ResourceType t : ResourceBuilder.buildListOfStorable().stream().map(Resource::type).collect(Collectors.toList())) req.add(new ResourceButton(t, gui));

        add(new JLabel("Click on resource to normalize the unknown"));
        add(req);
        add(out);




    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "done":
                break;
        }
    }
}
