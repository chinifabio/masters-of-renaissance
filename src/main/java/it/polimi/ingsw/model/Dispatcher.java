package it.polimi.ingsw.model;

import it.polimi.ingsw.communication.VirtualSocket;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.updates.ModelUpdater;
import it.polimi.ingsw.communication.packet.updates.Updater;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.view.View;

import java.util.HashMap;
import java.util.Map;

public class Dispatcher {
    private final Map<String, VirtualSocket> listeners = new HashMap<>();

    private final LiteModel model = new LiteModel();
    private final EmptyView view = new EmptyView();

    public void subscribe(String nickname, VirtualSocket socket) {
        socket.send(new Packet(HeaderTypes.NOTIFY, ChannelTypes.UPDATE_LITE_MODEL, new ModelUpdater(this.model).jsonfy()));
        this.listeners.put(nickname, socket);
    }

    public void publish(Updater updater) {
        updater.update(this.model, this.view);
        for (VirtualSocket x : this.listeners.values()) x.send(new Packet(HeaderTypes.NOTIFY, ChannelTypes.UPDATE_LITE_MODEL, updater.jsonfy()));
    }
}

class EmptyView implements View {

    /**
     * Tell something to the player
     *
     * @param message the message to show up to the player
     */
    @Override
    public void notifyPlayer(String message) {

    }

    /**
     * show an error to the player
     *
     * @param errorMessage the error message
     */
    @Override
    public void notifyPlayerError(String errorMessage) {

    }

    /**
     * notify a warning message to the player
     *
     * @param s the waring message
     */
    @Override
    public void notifyPlayerWarning(String s) {

    }

    /**
     * return the liteModel of the view
     *
     * @return the model of the view
     */
    @Override
    public LiteModel getModel() {
        return null;
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

    }
}
