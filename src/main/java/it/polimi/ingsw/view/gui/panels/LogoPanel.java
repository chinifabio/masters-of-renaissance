package it.polimi.ingsw.view.gui.panels;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LogoPanel extends JPanel{
    private final Image img;
    public LogoPanel() throws IOException {
        /*setName("RequestPanel");

        JLabel nickLabel = new JLabel();
        nickLabel.setName("requestText");
        nickLabel.setBackground(Color.CYAN.darker());
        nickLabel.setBounds(600,620,300,20);
        nickLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nickLabel.setOpaque(true);

        JTextField textArea = new JTextField(50);
        textArea.setBounds(650,650,200,20);
        textArea.setHorizontalAlignment(SwingConstants.CENTER);
        textArea.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //synchronized (userInteractionWait){
                //    synchronized (userInput) {
                //        userInput = textArea.getText();
                //        textArea.setText("");
                //        //sotto è in testing
                //        nickLabel.setText("Insert the desired number of players: ");
                //        //testing finito
                //    }
                //    userInteractionWait.notifyAll();
                //}
            }
        });

        JButton startGame = new JButton("Start Game!");
        startGame.setBounds(700,50,150,30);
        startGame.addActionListener(e -> {
            nickLabel.setText("Insert nickname:");
            textArea.setText("");
            this.remove(startGame);
            startGame.setVisible(false);
        });

        setBounds(0,0,1920-380,1080-230);
        setBackground(Color.gray);
        add(textArea, BorderLayout.SOUTH);
        add(nickLabel, BorderLayout.SOUTH);
        add(startGame);
        setLayout(null);

         */
        img = ImageIO.read(getClass().getResourceAsStream("/LogoMasters.png"));
    }

    /**
     * Draw the background
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = 1920-380;
        int height = 1080-270;
        g.drawImage(img, 0, 0,width,height, null);
    }

}
