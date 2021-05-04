package it.polimi.ingsw.communication.packet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class is used to transport object between client and server
 */
public class Packet {
    /**
     * The header of the packet
     */
    public final HeaderTypes header;

    /**
     * The channel on which the packet is sent
     */
    public final ChannelTypes channel;

    /**
     * The message associated
     */
    public final String body;

    /**
     * Build a packet from new header, channel and body
     * @param header the header of the packet
     * @param channel the channel of the packet
     * @param body the message transported by the packet
     */
    // todo implements all the possible types of packet as classes that implements the inteface Packet
    @JsonCreator
    public Packet(
            @JsonProperty("Header") HeaderTypes header,
            @JsonProperty("Channel") ChannelTypes channel,
            @JsonProperty("Body") String body) {

        this.header = header;
        this.channel = channel;
        this.body = body;
    }

    // following there is method for jackson serialization

    @JsonGetter("Header")
    public HeaderTypes getHeader() {
        return header;
    }

    @JsonGetter("Channel")
    public ChannelTypes getChannel() {
        return channel;
    }

    @JsonGetter("Body")
    public String getBody() {
        return body;
    }
}
