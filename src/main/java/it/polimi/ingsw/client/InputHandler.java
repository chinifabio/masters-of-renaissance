package it.polimi.ingsw.client;

import it.polimi.ingsw.view.View;

import java.util.LinkedList;

public abstract class InputHandler implements Runnable {

    private final LinkedList<String> data = new LinkedList<>();

    public final Object dataLock = new Object();

    //public final View view;

}
