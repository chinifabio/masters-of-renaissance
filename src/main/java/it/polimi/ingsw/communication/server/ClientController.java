package it.polimi.ingsw.communication.server;

import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.communication.VirtualSocket;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.Command;
import it.polimi.ingsw.model.Model;

public class ClientController implements Runnable{

    public final String nickname;

    private Model model;

    private final VirtualSocket socket;

    public ClientController(VirtualSocket socket, String nickname) {
        this.socket = socket;
        this.nickname = nickname;

        // System.out.println(TextColors.colorText(TextColors.BLUE_BRIGHT, nickname) + " joined the party");
    }

    public void linkModel(Model model) {
        this.model = model;
    }

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        boolean quit = false;
        Packet packet = null;
        while (!quit) {

            while (packet == null) {
                synchronized (this.socket.getChannelsList().stream().filter(x->x.equals(ChannelTypes.PLAYER_ACTIONS)).findAny().get()) {
                    packet = this.socket.pollPacketFrom(ChannelTypes.CONNECTION_STATUS);
                }
            }

            this.socket.send(this.model.handleClientCommand(this, (Command) packet.body));
        }
    }
}
