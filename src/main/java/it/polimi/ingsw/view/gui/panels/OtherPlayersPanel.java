package it.polimi.ingsw.view.gui.panels;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.panels.graphicComponents.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * This class is the GUI Panel that allows the player to see other players
 */
public class OtherPlayersPanel extends GuiPanel{

    /**
     * This attribute is the nickname of the player
     */
    private final String player;

    /**
     * This attribute indicates if the Player is in the Endgame phase so he can only see other player
     */
    private final boolean isEnd;

    /**
     * This is the constructor of the class
     * @param gui is the GUI that contains all the needed info
     * @param player is the nickname of the player
     * @param isEnd indicates if the Player is in the Endgame phase
     */
    public OtherPlayersPanel(GUI gui, String player, boolean isEnd) {
        super(gui);
        this.player = player;
        this.isEnd = isEnd;
    }

    /**
     * This method update the current panel after a change
     *
     * @return the current Panel updated
     * @throws IOException if there is an I/O problem
     */
    @Override
    public JPanel update() throws IOException {
        JPanel result = new BgJPanel("/otherBoard.png", GUI.width-NotifyPanel.width+50, GUI.height-120);

        //--------BACK BUTTON----------
        JPanel backPanel = new JPanel();
        JButton back = new JButton("Return");
        if (!isEnd) {
            back.addActionListener(e -> gui.switchPanels(new PersonalBoardPanel(gui)));
        } else {
            back.addActionListener(e -> gui.switchPanels(new WaitingEndGamePanel(gui)));
        }
        backPanel.add(back);
        backPanel.setOpaque(false);

        result.add(backPanel);

        JPanel boardPanel = new JPanel();
        boardPanel.setOpaque(false);
        boardPanel.setLayout(new GridBagLayout());

        JPanel extraDepot = new ExtraDepotPanel(gui, player);
        WarehousePanel warehousePanel = new WarehousePanel(gui, player);

        JPanel devSlot = new DevSlotPanel(gui, player);
        JPanel trackPanel = new FaithTrackPanel(gui, player);

        GridBagConstraints c = new GridBagConstraints();



        c.gridy = 1;
        boardPanel.add(warehousePanel, c);

        c.gridx = 1;
        boardPanel.add(extraDepot, c);

        c.gridx = 2;
        boardPanel.add(devSlot, c);


        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        boardPanel.add(trackPanel, c);

        c.gridy = 2;
        boardPanel.add(Box.createRigidArea(new Dimension(1920-380-200,25)), c);


        result.setName("Homepage");
        result.setLayout(new BorderLayout());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        JPanel buttons = new JPanel();

        JButton otherLeader = new JButton("View " + player + "'s Leaders");
        otherLeader.addActionListener(e -> gui.switchPanels(new OtherPlayerLeaderPanel(gui, player, isEnd)));
        buttons.add(otherLeader);
        buttons.add(backPanel);

        bottomPanel.setBackground(GUI.borderColor);
        buttons.setOpaque(false);

        result.setPreferredSize(new Dimension(GUI.gameWidth, GUI.gameHeight));
        buttons.add(Box.createRigidArea(new Dimension(0,70)));

        result.setVisible(true);

        buttons.setPreferredSize(new Dimension(1720, 100));

        result.add(boardPanel);

        JPanel panelText = new JPanel();
        panelText.setOpaque(false);
        panelText.setLayout(new BoxLayout(panelText,BoxLayout.Y_AXIS));
        panelText.add(Box.createRigidArea(new Dimension(0,40)));
        JTextArea text = new JTextArea();
        text.setText("This is the Personal Board of " + player);
        text.setOpaque(false);
        text.setFont(new Font("Times New Roman",Font.ITALIC,18));
        text.setEditable(false);
        panelText.add(text);

        JPanel buffer = new BufferPanel(gui);
        buffer.setPreferredSize(new Dimension(400,100));
        bottomPanel.add(Box.createRigidArea(new Dimension(30,0)));
        bottomPanel.add(buffer);
        bottomPanel.add(Box.createRigidArea(new Dimension(300,0)));
        bottomPanel.add(panelText);
        bottomPanel.add(buttons);

        result.add(bottomPanel, BorderLayout.SOUTH);

        result.setBackground(GUI.borderColor);

        result.repaint();
        result.revalidate();

        return result;
    }
}
