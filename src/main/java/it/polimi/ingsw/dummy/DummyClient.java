package it.polimi.ingsw.dummy;

import it.polimi.ingsw.communication.Disconnectable;
import it.polimi.ingsw.communication.VirtualSocket;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.litemodel.LiteModelUpdater;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DummyClient implements Runnable, Disconnectable {
    public final VirtualSocket socket;
    private ClientState state;

    private final ExecutorService executor = Executors.newCachedThreadPool();

    public static int port = 4444;
    public static String address = "127.0.0.1";

    public LiteModel model = new LiteModel();
    private boolean connected;

    public DummyClient() throws IOException {
        this.socket = new VirtualSocket(new Socket(address, port));
        this.executor.submit(this.socket);
        connected = true;

        this.state = new InitialClientState();

        this.executor.submit(new LiteModelUpdater(this.socket, this.model));

        // pinger thread
        //SecureConnection.pinger(this);
        this.socket.pinger(this);
    }

    public static void main(String[] args) {
        try {
            Thread t = new Thread(new DummyClient());
            t.setDaemon(true);
            t.start();
            t.join();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void setState(ClientState state) {
        this.state = state;
    }

    /**
     * The client manage the view and the the connection with the server
     */
    @Override
    public void run() {
        while (connected) {
            this.state.start(this);
        }
        this.executor.shutdownNow();
        System.out.println("quitting...");
    }

    public void handleDisconnection() {
        System.out.println("disconnected");
        this.connected = false;
    }

    @Override
    public VirtualSocket disconnectableSocket() {
        return this.socket;
    }
}
