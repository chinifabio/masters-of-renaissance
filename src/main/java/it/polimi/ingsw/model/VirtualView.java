package it.polimi.ingsw.model;

import it.polimi.ingsw.communication.VirtualSocket;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.updates.Publisher;
import it.polimi.ingsw.server.Controller;

import java.util.ArrayList;
import java.util.List;

public class VirtualView {
    private final List<VirtualSocket> listeners = new ArrayList<>();

    public void attachListener(Controller client) {
        this.listeners.add(client.socket);
    }

    public void sendPublisher(Publisher publisher) {
        for (VirtualSocket x : this.listeners) x.send(new Packet(HeaderTypes.NOTIFY, ChannelTypes.NOTIFY_VIEW, publisher.jsonfy()));
    }
}
