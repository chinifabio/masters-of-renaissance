package it.polimi.ingsw.view.gui;

public class tryGUI {

    public static void main (String[] args){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LogoFrame name = new LogoFrame();
                name.printIntro();
            }
        });
    }
}
