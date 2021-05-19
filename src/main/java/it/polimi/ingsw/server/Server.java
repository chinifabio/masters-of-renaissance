package it.polimi.ingsw.server;

import it.polimi.ingsw.communication.VirtualSocket;
import it.polimi.ingsw.model.Model;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable{

    private static int port = 4444;
    private static String address = "127.0.0.1";

    public final ExecutorService executor = Executors.newCachedThreadPool();
    private final ServerSocket serverSocket;

    private final List<String> nicknames = new ArrayList<>();
    private Model lobby = new Model();

    private final Object modelQueue = new Object();

    public Server() throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public static void main(String[] args) {
        // todo ugly but works
        if (args.length > 0 && args[0].equals("--port")) port = Integer.parseInt(args[1]);

        Thread serverWorker;
        try {
            serverWorker = new Thread(new Server());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }
        serverWorker.setDaemon(true);
        serverWorker.start();
        try {
            serverWorker.join();
        } catch (InterruptedException e) {
            System.out.println("interrupted");
        }
    }

    public void run() {
        System.out.println("Server ready");

        while (true) {
            try {
                // accept the connection and start the virtual socket to sort the received packet
                VirtualSocket clientSocket = new VirtualSocket(this.serverSocket.accept());
                this.executor.submit(clientSocket);

                // start the controller to handle the client messages
                Controller controller = new Controller(clientSocket, this);
                this.executor.submit(controller);
            } catch (IOException e) {
                System.out.println("Error while accepting client: " + e.getMessage());
            }
        }
    }

    public boolean memorizePlayer(String nickname) {
        if (nickname.toLowerCase().contains("lorenzo") || nickname.length() > 15 || this.nicknames.contains(nickname) || nickname.length() <= 0) return false;
        this.nicknames.add(nickname);
        return true;
    }

    public boolean forgetPlayer(String nickname) {
        return this.nicknames.remove(nickname);
    }

    public Model obtainModel() throws InterruptedException {
        if (this.lobby.availableSeats() > 0) return this.lobby;
        if (this.lobby.availableSeats() == 0) this.lobby = new Model();
        return this.lobby;
    }
}