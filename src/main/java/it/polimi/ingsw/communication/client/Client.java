package it.polimi.ingsw.communication.client;

import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.communication.Disconnectable;
import it.polimi.ingsw.communication.SecureConnection;
import it.polimi.ingsw.communication.VirtualSocket;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.litemodel.LiteModelUpdater;

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

    public Client() throws IOException {
        this.socket = new VirtualSocket(new Socket(address, port));
        this.executor.submit(this.socket);
        this.clientState = new InitialCS();
        this.connected = true;

        this.executor.submit(new LiteModelUpdater(this.socket, this.liteModel));
        this.executor.submit(SecureConnection.pinger(this));
    }

    public static void main(String[] args) {
        try {
            Thread client = new Thread(new Client());
            client.setDaemon(true);
            client.start();
            client.join();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    /**
     * Receive a state and set it as current state
     * @param newClientState
     */
    public void setState(ClientState newClientState){ this.clientState = newClientState; }

    @Override
    public void run() {
        while(connected){
            this.clientState.start(this);
        }
        this.executor.shutdownNow();
        System.out.println(TextColors.colorText(TextColors.WHITE, "Disconnection"));
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