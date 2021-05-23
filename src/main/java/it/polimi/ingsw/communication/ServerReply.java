package it.polimi.ingsw.communication;

import it.polimi.ingsw.TextColors;

public class ServerReply {

    private final String message;
    public final ReplyType type;


    public ServerReply(String message, ReplyType type) {
        this.message = message;
        this.type = type;
    }

    public String obtainMessage() {
        return "server: " + TextColors.colorText(type.color, message);
    }
}
