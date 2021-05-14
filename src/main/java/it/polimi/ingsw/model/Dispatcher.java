package it.polimi.ingsw.model;

import it.polimi.ingsw.communication.VirtualSocket;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.updates.Updater;
import it.polimi.ingsw.server.Controller;

import java.util.ArrayList;
import java.util.List;

public class Dispatcher {
    private final List<VirtualSocket> listeners = new ArrayList<>();
    private final List<Updater> history = new ArrayList<>();
    private boolean saveHistory = true;

    public void subscribe(Controller client) {
        for(Updater u : history) {

            client.socket.send(new Packet(HeaderTypes.NOTIFY, ChannelTypes.NOTIFY_VIEW, u.jsonfy()));
        }
        this.listeners.add(client.socket);
    }

    public void publish(Updater updater) {
        if (saveHistory) this.history.add(updater);
        for (VirtualSocket x : this.listeners) x.send(new Packet(HeaderTypes.NOTIFY, ChannelTypes.NOTIFY_VIEW, updater.jsonfy()));
    }

    public void enableHistory() {
        this.saveHistory = true;
    }

    public void disableHistory() {
        this.history.clear();
        this.saveHistory = false;
    }
}
