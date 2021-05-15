package it.polimi.ingsw.litemodel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.communication.VirtualSocket;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.updates.Updater;

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
        while (true) {
            Packet packet = this.socket.pollPacketFrom(ChannelTypes.NOTIFY_VIEW);


            try {
                Updater received = new ObjectMapper().readerFor(Updater.class).readValue(packet.body);
                received.update(this.model);
            } catch (JsonProcessingException e) {
                System.out.println(TextColors.colorText(TextColors.RED, "update view error: ") + e.getMessage());
                System.out.println(packet.body);
            }
        }
    }
}
