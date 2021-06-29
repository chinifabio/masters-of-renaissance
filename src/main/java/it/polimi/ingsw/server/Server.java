package it.polimi.ingsw.server;

import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.match.match.SingleplayerMatch;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The server class is designed to entry in a infinite loop in which
 * it will accept client connection and join it to the game
 */
public class Server {

    /**
     * This executor is used to run different processes on a limited number of threads
     */
    private final ExecutorService executor = Executors.newCachedThreadPool();

    /**
     * The server socket is the TCP socket used to accept connection with other sockets
     */
    private final ServerSocket serverSocket;

    /**
     * A map containing a nickname associated with his controller
     */
    private final Map<String, Controller> connectedClient = new HashMap<>();

    /**
     * A map containing a disconnected nickname associated with his controller
     */
    private final Map<String, Controller> disconnectedClient = new HashMap<>();

    /**
     * The model used to join player to the game. It is created by the controller and then passed to server to be shared
     */
    private Model model = null;

    /**
     * A flag to indicate if a creator is been assigned
     */
    private boolean creatorAssigned = false;

    /**
     * Used to set the activation of the server
     */
    private boolean active = true;

    /**
     * Create a server based on the port it will be listening to
     * @param port the port of the server
     * @throws IOException when java can't create the server socket
     */
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

    /**
     * This loop will accept connections and than create a socket, a controller
     * and run them on with the executor
     */
    public void start() {
        // server stopping mechanism
        new Thread(()-> {
            while (true) if (new Scanner(System.in).nextLine().equalsIgnoreCase("quit")) System.exit(0);
        }).start();

        print("Server ready. Type quit to stop the server.");
        while (active) {
            try {
                Controller controller = new Controller(serverSocket.accept(), this);
                this.executor.submit(controller);
            } catch (IOException e) {
                print("Error while accepting client: " + e.getMessage());
            }
        }
    }

    /**
     * Execute a thread with the server executor
     * @param t the thread to be ran
     */
    public void executeRunnable(Runnable t) {
        executor.submit(t);
    }

    /**
     * Check if the choose nickname is legal. If yes save the nickname
     * @param nickname the nickname of the client
     * @param controller the controller of the client
     * @return the succeed of the operation
     */
    public synchronized boolean connect(String nickname, Controller controller) {
        // checking the legality of the nickname
        if (
                nickname.equals(SingleplayerMatch.lorenzoNickname) ||
                nickname.length() > 20 || nickname.length() <= 0 ||
                connectedClient.containsKey(nickname.toLowerCase())
        ) return false;

        connectedClient.put(nickname, controller);
        return true;
    }

    /**
     * Check if the nickname chose by the client was a disconnected player
     * @param nickname the nickname to check
     * @param newController the new controller to associate to the nickname
     * @return the succeed of the operation
     */
    public synchronized boolean reconnect(String nickname, Controller newController) {
        if(disconnectedClient.containsKey(nickname)) {
            newController.transferStatus(disconnectedClient.remove(nickname));
            connectedClient.put(nickname, newController);
            return true;
        }
        return false;
    }

    /**
     * Used to known if a model is available
     * @return the availing of the model
     */
    public synchronized boolean isModelAvailable() {
        if (model == null || model.availableSeat() == 0) {
            model = null;
            return false;
        }

        return true;
    }

    /**
     * Return the model to join in
     * @return the model
     */
    public synchronized Model obtainModel() {
        return model;
    }

    /**
     * Used to known if the controller need to create a model or not
     * @return true if server need a model, false instead
     */
    public synchronized boolean needACreator() {
        return !creatorAssigned && (creatorAssigned = true);
    }

    /**
     * Communicate to server a failure during the choice of the number of player
     */
    public synchronized void cannotCreateModel() {
        creatorAssigned = false;
    }

    /**
     * Pass a new model to the server
     * @param newModel the model to share with the server
     */
    public synchronized void hereSTheModel(Model newModel) {
        if (newModel.availableSeat() == 0) return;
        model = newModel;
        creatorAssigned = false;
    }

    /**
     * Disconnect a player from his nickname and his controller
     * @param nickname the nickname of the disconnected player
     * @param controller the controller of the disconnected player
     */
    public synchronized void disconnect(String nickname, Controller controller) {
        connectedClient.remove(nickname);
        disconnectedClient.put(nickname, controller);
    }

    /**
     * Delete a controller from the saved ones
     * @param nickname the nickname associated to the controller
     */
    public synchronized void cleanNickname(String nickname) {
        connectedClient.remove(nickname);
        disconnectedClient.remove(nickname);
    }

    /**
     * Print a log message in output
     * @param s the log message
     */
    public synchronized void print(String s) {
        System.out.println(s);
    }
}