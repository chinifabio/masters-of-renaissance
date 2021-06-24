package it.polimi.ingsw.server;

import it.polimi.ingsw.model.Model;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final ServerSocket serverSocket;

    private final Map<String, Controller> connectedClient = new HashMap<>();
    private final Map<String, Controller> disconnectedClient = new HashMap<>();

    private Model model = null;
    private boolean creatorAssigned = false;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public static void main(String[] args) {
        try {
            new Server(4444).start();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void start() {
        System.out.println("Server ready");
        while (true) {
            try {
                Controller controller = new Controller(serverSocket.accept(), this);
                this.executor.submit(controller);
            } catch (IOException e) {
                System.out.println("Error while accepting client: " + e.getMessage());
            }
        }
    }

    public void executeRunnable(Runnable t) {
        executor.submit(t);
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
            newController.transferStatus(disconnectedClient.remove(nickname));
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