package it.polimi.ingsw.communication;

import it.polimi.ingsw.TextColors;

public enum ReplyType {
    OK(TextColors.GREEN_BRIGHT), ERROR(TextColors.RED_BRIGHT), UPDATE(TextColors.CYAN_BRIGHT);

    public final String color;

    ReplyType(String color) {
        this.color = color;
    }
}
