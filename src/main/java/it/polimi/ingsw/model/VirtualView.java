package it.polimi.ingsw.model;

import it.polimi.ingsw.communication.SocketListener;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.clientexecutable.LorenzoPopUp;
import it.polimi.ingsw.communication.packet.clientexecutable.PlayerError;
import it.polimi.ingsw.communication.packet.clientexecutable.PlayerMessage;
import it.polimi.ingsw.communication.packet.clientexecutable.ModelUpdater;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.model.cards.SoloActionToken;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * The virtual view is used to send the the client packet of messenger and update lite model channels
 */
public class VirtualView {
    /**
     * This collection maps a nickname to his socket listener used to send the packet
     */
    private final Map<String, SocketListener> listeners = new HashMap<>();

    /**
     * This instance of the lite model is used to send all data to a subscribing player
     */
    private final LiteModel model = new LiteModel();

    /**
     * This attribute is only used for test purpose.
     */
    private HeaderTypes test = HeaderTypes.OK;

    /**
     * Subscribe a socket listener, associated to a nickname of the player, to all the packet traffic
     * @param nickname the nickname of the owner of the socket listener
     * @param socket the socket on which send packets
     */
    public void subscribe(String nickname, SocketListener socket) {
        socket.send(new Packet(HeaderTypes.NOTIFY, ChannelTypes.UPDATE_LITE_MODEL, new ModelUpdater(model).jsonfy()));
        listeners.put(nickname, socket);
    }

    /**
     * Remove the socket associated to the passed nickname from the listeners
     * @param nickname the key of the socket to remove
     */
    public void unsubscribe(String nickname) {
        listeners.remove(nickname);
    }

    /**
     * Send a message to all the subscribed client
     * @param message the message to send
     */
    public void sendMessage(String message) {
        String exe = new PlayerMessage(message).jsonfy();
        for (SocketListener x : listeners.values()) x.send(
                new Packet(HeaderTypes.NOTIFY, ChannelTypes.MESSENGER, exe)
        );
    }

    /**
     * Send a message error to all the subscribed clients
     * @param message the error message
     */
    public void sendError(String message) {
        String exe = new PlayerError(message).jsonfy();
        for (SocketListener x : listeners.values()) x.send(
                new Packet(HeaderTypes.INVALID, ChannelTypes.MESSENGER, exe)
        );
    }

    /**
     * Send a message to the player identified by the passed nickname
     * @param nickname the nickname of the client to send the message
     * @param message the message that need to be send
     */
    public void sendPlayerError(String nickname, String message) {
        this.test = HeaderTypes.INVALID;
        listeners
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().equals(nickname))
                .forEach(entry -> entry.getValue().send(
                        new Packet(HeaderTypes.INVALID, ChannelTypes.MESSENGER, new PlayerError(message).jsonfy())
                ));
    }

    /**
     * Send an error message to the player identified by the passed nickname
     * @param nickname the nickname of the client to send the error message
     * @param message the error message that need to be send
     */
    public void sendPlayerMessage(String nickname, String message) {
        listeners
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().equals(nickname))
                .forEach(entry -> entry.getValue().send(
                        new Packet(HeaderTypes.NOTIFY, ChannelTypes.MESSENGER, new PlayerMessage(message).jsonfy())
                ));
    }

    /**
     * Send a lite model updater packet to all the player before applying it to the local lite model so it is updated
     * @param updater the lite model update
     */
    public void publish(Consumer<LiteModel> updater) {
        updater.accept(model);
    }

    /**
     * Send a copy of the lite model to all the subscribed clients
     */
    public void updateClients() {
        for (SocketListener sock : listeners.values())
            sock.send(new Packet(HeaderTypes.NOTIFY, ChannelTypes.UPDATE_LITE_MODEL, new ModelUpdater(model).jsonfy()));
    }

    /**
     * Send the used solo token to the client
     * @param token the token to send into the lorenzo popup packet
     */
    public void sendToken(SoloActionToken token) {
        for (SocketListener socketListener : listeners.values())
            socketListener.send(new Packet(HeaderTypes.NOTIFY, ChannelTypes.UPDATE_LITE_MODEL, new LorenzoPopUp(token.liteVersion()).jsonfy()));
    }


    /**
     * This method is a getter for the test attribute. It resets to OK.
     * @return the HeaderTypes of test
     */
    public HeaderTypes getTest(){
        HeaderTypes temp = this.test;
        this.test = HeaderTypes.OK;
        return temp;
    }
}