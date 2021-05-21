package it.polimi.ingsw.client;

import java.util.Scanner;

public class InputHandler {
    private boolean gui = false;

    public InputHandler(boolean gui) {
        this.gui = gui;
    }

    public String getNick(){
        if(gui) {
            String nick = "pippo";
            //GUI
            return nick;
        }
        else return new Scanner(System.in).nextLine();
    }


}
