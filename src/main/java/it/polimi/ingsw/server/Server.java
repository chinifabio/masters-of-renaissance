package it.polimi.ingsw.server;

import it.polimi.ingsw.communication.VirtualSocket;
import it.polimi.ingsw.model.Model;

import javax.naming.ldap.Control;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable{

    private static int port = 4444;
    private static String address = "127.0.0.1";

    public final ExecutorService executor = Executors.newCachedThreadPool();
    private final ServerSocket serverSocket;

    private final Map<String, Controller> connectedClient = new HashMap<>();
    private final Map<String, Controller> disconnectedClient = new HashMap<>();

    private Model lobby = new Model();

    public final static int error = -1;
    public final static int reconnect = 0;
    public final static int newPlayer = 1;

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

    public synchronized int connect(String nickname, Controller controller) {
        // checking the legality of the nickname
        if (
                nickname.equalsIgnoreCase("lorenzo il magnifico") ||
                nickname.length() > 20 ||
                connectedClient.containsKey(nickname)
        ) return error;

        // checking if the client was disconnected
        if(disconnectedClient.containsKey(nickname)) {
            controller.model = disconnectedClient.get(nickname).model; // todo to change
            disconnectedClient.remove(nickname);
            connectedClient.put(nickname, controller);
            return reconnect;
        }

        this.connectedClient.put(nickname, controller);
        return newPlayer;
    }

    public synchronized void disconnect(String nickname, Controller controller) {
        connectedClient.remove(nickname);
        disconnectedClient.put(nickname, controller);
    }

    public Model obtainModel() throws InterruptedException {
        if (this.lobby.availableSeats() > 0) return this.lobby;
        if (this.lobby.availableSeats() == 0) this.lobby = new Model();
        return this.lobby;
    }
}