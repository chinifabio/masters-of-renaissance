package it.polimi.ingsw.communication.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.communication.VirtualSocket;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.Command;
import it.polimi.ingsw.model.Model;

/**
 * The controller of MVC pattern for handling client command on the model
 */
public class ClientController implements Runnable{

    /**
     * The nickname the player sets
     */
    public final String nickname;

    /**
     * The model reference
     */
    private Model model;

    /**
     * The socket for connection
     */
    private final VirtualSocket socket;

    /**
     * Create a controller for handling client command on the model
     * @param socket
     * @param nickname
     */
    public ClientController(VirtualSocket socket, String nickname) {
        this.socket = socket;
        this.nickname = nickname;

        // System.out.println(TextColors.colorText(TextColors.BLUE_BRIGHT, nickname) + " enter the server");
    }

    /**
     * Accept operation by the client and response to it with a packet created by the model
     */
    @Override
    public void run() {
        boolean quit = false;
        while (!quit) {
            Packet packet = this.socket.pollPacketFrom(ChannelTypes.PLAYER_ACTIONS);
            Packet response;

            try {
                response = this.model.handleClientCommand(this, new ObjectMapper().readerFor(Command.class).readValue(packet.body));
            } catch (JsonProcessingException e) {
                response = new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, "error while deserializing command in controller: " + e.getMessage());
                quit = true;
            }

            this.socket.send(response);
        }
    }

    /**
     * Set the link for the model
     * @param model the model reference
     */
    public void linkModel(Model model) {
        this.model = model;
    }
}
