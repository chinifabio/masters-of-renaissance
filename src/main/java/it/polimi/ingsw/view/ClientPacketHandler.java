package it.polimi.ingsw.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.communication.SocketListener;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.rendering.Lighter;
import it.polimi.ingsw.communication.packet.updates.TokenUpdater;
import it.polimi.ingsw.communication.packet.updates.Updater;

/**
 * This class manages the packets of the Client
 */
public class ClientPacketHandler {

    /**
     * This attribute is the View
     */
    private final View view;

    /**
     * This attribute is the SocketListener that manage the packets
     */
    private final SocketListener socket;

    /**
     * This attribute indicates if the client is connected
     */
    private boolean activeClient = true;

    /**
     * This is the constructor of the class
     * @param view is the view
     * @param socket is the manager of packets
     */
    public ClientPacketHandler(View view, SocketListener socket) {
        this.view = view;
        this.socket = socket;
    }

    /**
     * While the client is connected read packets sent by the server and handle their behavior
     */
    public void start() {
        while (activeClient) {
            Packet received = socket.pollPacket();
            switch (received.channel) {
                case MESSENGER -> {
                    if (received.header == HeaderTypes.NOTIFY) view.notifyPlayer(received.body);
                    if (received.header == HeaderTypes.INVALID) view.notifyPlayerError(received.body);
                }

                case UPDATE_LITE_MODEL -> {
                    Updater up;
                    try {
                        up = new ObjectMapper().readerFor(Updater.class).readValue(received.body);
                        up.update(view.getModel());
                        view.refresh();
                    } catch (JsonProcessingException e) {
                        view.emergencyExit("Client and server are not synchronized, you will be disconnect.");
                        return;
                    }

                    if (up instanceof TokenUpdater) {
                        view.popUpLorenzoMoves();
                    }
                }

                case RENDER_CANNON -> {
                    try {
                        Lighter ammo = new ObjectMapper().readerFor(Lighter.class).readValue(received.body);
                        ammo.fire(view);
                    } catch (JsonProcessingException e) {
                        view.emergencyExit("Client and server are not synchronized, you will be disconnect.");
                    }
                }

                case CONNECTION_STATUS -> {
                    if (received.header == HeaderTypes.TIMEOUT) {
                        view.emergencyExit("You lost the connection to the server, quitting.");
                        activeClient = false;
                    }
                }

                default -> {}
            }
        }
    }
}
