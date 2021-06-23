package it.polimi.ingsw.model;

import it.polimi.ingsw.communication.SocketListener;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.updates.ModelUpdater;
import it.polimi.ingsw.communication.packet.updates.Updater;
import it.polimi.ingsw.litemodel.LiteModel;

import java.util.HashMap;
import java.util.Map;

public class Dispatcher {
    private final Map<String, SocketListener> listeners = new HashMap<>();

    private final LiteModel model = new LiteModel();

    public void subscribe(String nickname, SocketListener socket) {
        socket.send(new Packet(HeaderTypes.NOTIFY, ChannelTypes.UPDATE_LITE_MODEL, new ModelUpdater(this.model).jsonfy()));
        this.listeners.put(nickname, socket);
    }

    public void sendMessage(String message) {
        for (SocketListener x : this.listeners.values()) x.send(new Packet(HeaderTypes.NOTIFY, ChannelTypes.MESSENGER, message));
    }

    public void sendError(String message) {
        for (SocketListener x : this.listeners.values()) x.send(new Packet(HeaderTypes.INVALID, ChannelTypes.MESSENGER, message));
    }

    public void publish(Updater updater) {
        updater.update(this.model);
        for (SocketListener x : this.listeners.values()) x.send(new Packet(HeaderTypes.NOTIFY, ChannelTypes.UPDATE_LITE_MODEL, updater.jsonfy()));
    }
}
