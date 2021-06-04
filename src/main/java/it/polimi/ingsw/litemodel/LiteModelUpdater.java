package it.polimi.ingsw.litemodel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.communication.VirtualSocket;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.updates.Updater;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.cli.Colors;

public class LiteModelUpdater implements Runnable{
    private final VirtualSocket socket;
    private final LiteModel model;

    public LiteModelUpdater(VirtualSocket socket, LiteModel model) {
        this.socket = socket;
        this.model = model;
    }

    /**
     * Wait for notify view packet and update the lite model for the view
     * @see Thread#run()
     */
    @Override
    public void run() {
        Packet received;
        while (true) {
            received = socket.pollPacketFrom(ChannelTypes.UPDATE_LITE_MODEL);
            switch (received.header) {
                case TIMEOUT: return;
                case LOCK: socket.send(new Packet(HeaderTypes.UNLOCK, ChannelTypes.UPDATE_LITE_MODEL, "ok")); break;
                case NOTIFY: useUpdater(received.body); break;
                default: System.out.println("[model updater] invalid packet received: " + received); break;
            }
        }
    }

    private void useUpdater(String json) {
        try {
            Updater up = new ObjectMapper().readerFor(Updater.class).readValue(json);
            up.update(this.model);
        } catch (JsonProcessingException e) {
            System.out.println(Colors.color(Colors.RED, "update view error: ") + e.getMessage());
        }
    }
}
