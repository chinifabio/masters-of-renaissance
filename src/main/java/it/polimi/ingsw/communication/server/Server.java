package it.polimi.ingsw.communication.server;

import it.polimi.ingsw.communication.VirtualSocket;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalTypeInProduction;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable{

    private static int port = 4444;

    public static void main(String[] args) {
        // todo ugly but works
        if (args.length > 0 && args[0].equals("--port")) Server.port = Integer.parseInt(args[1]);

        Thread serverWorker = new Thread(new Server());
        serverWorker.setDaemon(true);
        serverWorker.start();
        try {
            serverWorker.join();
        } catch (InterruptedException e) {
            System.out.println("interrupted");
        }
    }

    public void run() {
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }

        Model model = new Model();

        System.out.println("Server ready");

        while(true)  {
            try {
                VirtualSocket sock = new VirtualSocket(serverSocket.accept());
                new Thread(sock).start();

                System.out.println("connected :" + sock.realSocket().getInetAddress());

                String nickname = "";

                boolean nick = false;
                while (!nick) {
                    Packet packet = sock.pollPacketFrom(ChannelTypes.PLAYER_ACTIONS);

                    if (packet.header == HeaderTypes.HELLO) {
                        nickname = packet.body;
                        nick = true;
                        // todo it will set the first player message
                        sock.send(new Packet(HeaderTypes.OK, ChannelTypes.PLAYER_ACTIONS, "welcome"));
                    } else sock.send(new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, "give me nick"));
                }

                System.out.println("nickname set: " + nickname);

                ClientController t = new ClientController(sock, nickname);

                if (model.availableSeats() >= 0) {
                    model.connectController(t);
                } else {
                    model.start(t);
                }

                executor.submit(t);
            } catch(IOException e) {
                System.out.println("error in connection");
            } catch (IllegalTypeInProduction e) {
                System.out.println("error in match start");
            }
        }
    }
}
