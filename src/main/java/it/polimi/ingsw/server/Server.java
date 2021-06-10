package it.polimi.ingsw.server;

import it.polimi.ingsw.communication.VirtualSocket;
import it.polimi.ingsw.model.Model;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable{

    public final ExecutorService executor = Executors.newCachedThreadPool();
    private final ServerSocket serverSocket;

    private final Map<String, Controller> connectedClient = new HashMap<>();
    private final Map<String, Controller> disconnectedClient = new HashMap<>();

    private Model model = null;
    private boolean creatorAssigned = false;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public static void main(String[] args) {
        Thread serverWorker;
        try {
            serverWorker = new Thread(new Server(4444));
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

    public synchronized boolean connect(String nickname, Controller controller) {
        // checking the legality of the nickname
        if (
                nickname.equalsIgnoreCase("lorenzo il magnifico") ||
                nickname.length() > 20 || nickname.length() <= 0 ||
                connectedClient.containsKey(nickname)
        ) return false;

        connectedClient.put(nickname, controller);
        return true;
    }

    public synchronized boolean reconnect(String nickname, Controller newController) {
        if(disconnectedClient.containsKey(nickname)) {
            newController.model = disconnectedClient.remove(nickname).model;
            connectedClient.put(nickname, newController);
            return true;
        }
        return false;
    }

    public synchronized boolean isModelAvailable() {
        if (model == null || model.availableSeat() == 0) {
            model = null;
            return false;
        }

        return true;
    }

    public synchronized Model obtainModel() {
        return model;
    }

    public synchronized boolean needACreator() {
        return !creatorAssigned && (creatorAssigned = true);
    }

    public synchronized void cannotCreateModel() {
        creatorAssigned = false;
    }

    public synchronized void hereSTheModel(Model newModel) {
        if (newModel.availableSeat() == 0) return;
        model = newModel;
        creatorAssigned = false;
    }

    public synchronized void disconnect(String nickname, Controller controller) {
        connectedClient.remove(nickname);
        disconnectedClient.put(nickname, controller);
    }

    public synchronized void removeController(String nickname) {
        this.connectedClient.remove(nickname);
    }
}