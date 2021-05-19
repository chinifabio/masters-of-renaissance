package it.polimi.ingsw.model;

import it.polimi.ingsw.communication.VirtualSocket;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.updates.ModelUpdater;
import it.polimi.ingsw.communication.packet.updates.Updater;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.server.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dispatcher {
    private final Map<String, VirtualSocket> listeners = new HashMap<>();

    private final LiteModel model = new LiteModel();

    public void subscribe(String nickname, VirtualSocket socket) {
        socket.send(new Packet(HeaderTypes.NOTIFY, ChannelTypes.NOTIFY_VIEW, new ModelUpdater(this.model).jsonfy()));
        this.listeners.put(nickname, socket);
    }

    public void publish(Updater updater) {
        updater.update(this.model);
        for (VirtualSocket x : this.listeners.values()) x.send(new Packet(HeaderTypes.NOTIFY, ChannelTypes.NOTIFY_VIEW, updater.jsonfy()));
    }
}
