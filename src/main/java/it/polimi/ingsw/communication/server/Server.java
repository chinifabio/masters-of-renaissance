package it.polimi.ingsw.communication.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable{

    private static int port = 4444;

    public static void main(String[] args) {
        // todo ugly but works
        if (args[0].equals("--port")) Server.port = Integer.parseInt(args[1]);

        Thread serverWorker = new Thread(new Server());
        serverWorker.setDaemon(true);
        serverWorker.start();
    }

    public void run() {
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage()); // Porta non disponibile
            return;
        }

        System.out.println("Server ready");

        /*
        for (int i = 0; i < 2; i++) {
            try {
                Socket socket = serverSocket.accept();

                System.out.println("connected :" + socket.getInetAddress());
                writeToClient(socket, "give me your username");

                ClientHandler t = new ClientHandler(socket, waitResp(socket), controller);
                controller.playerJoin(t);

                executor.submit(t);
            } catch(IOException e) {
                System.out.println("error in connection");
            }
        }
        */

        executor.shutdown();
    }
}
