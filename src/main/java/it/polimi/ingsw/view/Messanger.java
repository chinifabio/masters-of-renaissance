package it.polimi.ingsw.view;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;

public class Messanger implements Runnable {
    public final View view;

    public Messanger(View view) {
        this.view = view;
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
        while(true) {
            Packet received = view.getSocket().pollPacketFrom(ChannelTypes.MESSENGER);
            if (received.header == HeaderTypes.TIMEOUT) return;

            view.notifyPlayer(received.body);
        }
    }
}
