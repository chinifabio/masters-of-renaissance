package it.polimi.ingsw.communication;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.*;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.match.markettray.RowCol;
import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class DummyClient implements Runnable{
    private static int port = 4444;
    private VirtualSocket socket;
    public static String address = "127.0.0.1";

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--address")) DummyClient.address = args[1];

        Thread client = new Thread(new DummyClient());
        client.setDaemon(true);
        client.start();
        try {
            client.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read from key board and send command to the server
     */
    @Override
    public void run() {
        boolean waitResp = true;

        try {
            this.socket = new VirtualSocket(new Socket(address, port));
        } catch (IOException e) {
            System.out.println("socket error");
            return;
        }
        new Thread(this.socket).start();


        Scanner scanner = new Scanner(System.in);

        System.out.print("insert nickname: ");
        this.socket.send(new Packet(HeaderTypes.HELLO, ChannelTypes.PLAYER_ACTIONS, scanner.nextLine()));

        System.out.println(this.socket.pollPacketFrom(ChannelTypes.PLAYER_ACTIONS).body);

        while(true) {
            System.out.println("give me action: ");
            switch (scanner.nextLine()) {
                case "buyCard":
                    this.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new BuyDevCardCommand(
                            LevelDevCard.valueOf(scanner.nextLine()),
                            ColorDevCard.valueOf(scanner.nextLine()),
                            DevCardSlot.valueOf(scanner.nextLine())
                    ).jsonfy()));
                    break;

                case "discardLeader":
                    this.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new DiscardLeaderCommand(scanner.nextLine()).jsonfy()));
                    break;

                case "paint":
                    this.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new PaintMarbleCommand(
                            Integer.parseInt(scanner.nextLine()),
                            Integer.parseInt(scanner.nextLine())
                    ).jsonfy()));
                    break;

                case "setNumber":
                    this.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new SetNumberCommand(Integer.parseInt(scanner.nextLine())).jsonfy()));
                    break;

                case "useTray":
                    this.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new UseMarketTrayCommand(
                            RowCol.valueOf(scanner.nextLine()),
                            Integer.parseInt(scanner.nextLine())
                    ).jsonfy()));
                    break;

                default: waitResp = false; break;
            }

            if (waitResp) {
                Packet packet = this.socket.pollPacketFrom(ChannelTypes.PLAYER_ACTIONS);
                System.out.println(packet.body);
            } else waitResp = true;
        }
    }
}
