package it.polimi.ingsw.client;

import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.communication.Disconnectable;
import it.polimi.ingsw.communication.VirtualSocket;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.litemodel.LiteModelUpdater;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.cli.printer.NamePrinter;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//mappa con azioni possibli e true/false, nella view poi avrò la possibilità di fare solo quelle true

public class Client implements Runnable, Disconnectable {

    private static int port = 4444;
    public final VirtualSocket socket;
    public static String address = "127.0.0.1";

    private final ExecutorService executor = Executors.newCachedThreadPool();

    public LiteModel liteModel = new LiteModel();

    private ClientState clientState;

    private boolean connected;

    private final View view;

    public Client(View view) throws IOException {
        this.clientState = new InitialCS();
        this.connected = true;

        this.socket = new VirtualSocket(new Socket(address, port));
        this.view = view; this.view.receiveModel(this.liteModel);
        this.executor.submit(new LiteModelUpdater(this.socket, this.liteModel));

        this.socket.pinger(this);
        this.executor.submit(this.view);
        this.executor.submit(this.socket);
    }



    /**
     * Receive a state and set it as current state
     * @param newClientState new state of the client
     */
    public void setState(ClientState newClientState){ this.clientState = newClientState; }

    @Override
    public void run() {
        NamePrinter.titleName(); // todo change for the gui
        view.notifyPlayerWarning("\nAt the start of the game, you have to discard two leader cards and, based on your position in the sequence, you can select up to 2 initial resources.\nAfter that, end your turn. You can type \"help\" to see again the possible moves.\n");

        while(connected){
            try {
                this.clientState.start(this, this.view);
            } catch (InterruptedException e) {
                System.out.println("miseriaccia harry... thread fail");
                return;
            }
        }

        this.executor.shutdownNow();
        System.out.println(TextColors.colorText(TextColors.WHITE, "Quitting..."));
    }

    @Override
    public void handleDisconnection() {
        System.out.println("disconnected");
        this.connected = false;
    }

    @Override
    public VirtualSocket disconnectableSocket() {
        return this.socket;
    }
}