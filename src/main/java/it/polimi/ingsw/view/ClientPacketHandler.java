package it.polimi.ingsw.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.communication.SocketListener;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.rendering.Lighter;
import it.polimi.ingsw.communication.packet.updates.TokenUpdater;
import it.polimi.ingsw.communication.packet.updates.Updater;

public class ClientPacketHandler {
    private final View view;
    private final SocketListener socket;
    private boolean activeClient = true;

    public ClientPacketHandler(View view, SocketListener socket) {
        this.view = view;
        this.socket = socket;
    }

    /**
     *
     */
    public void start() {
        while (activeClient) {
            Packet received = socket.pollPacket();
            switch (received.channel) {
                case MESSENGER -> {
                    if (received.header == HeaderTypes.NOTIFY) view.notifyPlayer(received.body);
                    if (received.header == HeaderTypes.INVALID) view.notifyPlayerError(received.body);
                }

                case PLAYER_ACTIONS -> {
                    // todo send with OK header the new model
                    if (received.header != HeaderTypes.INVALID) {
                        view.notifyPlayer(received.body);
                        view.refresh();
                    } else
                        view.notifyPlayerError(received.body);
                }

                case UPDATE_LITE_MODEL -> {
                    Updater up;
                    try {
                        up = new ObjectMapper().readerFor(Updater.class).readValue(received.body);
                        up.update(view.getModel());
                        //updatePanel(); todo de comment when model will send all the changes in only one packet
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
                        view.emergencyExit("You lost the connection to server, quitting.");
                        return;
                    }
                }
            }
        }
    }
}
