package it.polimi.ingsw.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.communication.SocketListener;
import it.polimi.ingsw.communication.packet.clientexecutable.ClientExecutable;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;

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
                case MESSENGER:
                case UPDATE_LITE_MODEL:
                case RENDER_CANNON:
                    try {
                        ClientExecutable ce = new ObjectMapper().readerFor(ClientExecutable.class).readValue(received.body);
                        ce.execute(view);
                    } catch (Exception e) {
                        view.emergencyExit("Client and server are not synchronized, you will be disconnect.");
                        return;
                    }
                    break;

                case CONNECTION_STATUS:
                    if (received.header == HeaderTypes.TIMEOUT) {
                        view.emergencyExit("You lost the connection to the server, quitting.");
                        activeClient = false;
                    }
                    break;
            }
        }
    }
}
