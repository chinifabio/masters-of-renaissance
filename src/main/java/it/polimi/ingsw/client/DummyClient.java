package it.polimi.ingsw.client;

import it.polimi.ingsw.communication.VirtualSocket;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.litemodel.LiteModelUpdater;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DummyClient implements Runnable{
    public final VirtualSocket socket;
    private ClientState state;

    private final ExecutorService executor = Executors.newCachedThreadPool();

    public static int port = 4444;
    public static String address = "127.0.0.1";

    public LiteModel model = new LiteModel();

    public DummyClient() throws IOException {
        this.socket = new VirtualSocket(new Socket(address, port));
        this.executor.submit(this.socket);
        this.state = new InitialClientState();

        this.executor.submit(new LiteModelUpdater(this.socket, this.model));
    }

    public static void main(String[] args) {
        try {
            Thread t = new Thread(new DummyClient());
            t.setDaemon(true);
            t.start();
            t.join();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
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
        while (true) {
            this.state.start(this);
        }
    }
}
