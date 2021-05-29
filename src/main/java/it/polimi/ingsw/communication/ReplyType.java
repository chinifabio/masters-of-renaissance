package it.polimi.ingsw.communication;

import it.polimi.ingsw.view.cli.Colors;

public enum ReplyType {
    OK(Colors.GREEN_BRIGHT), ERROR(Colors.RED_BRIGHT), UPDATE(Colors.CYAN_BRIGHT);

    public final String color;

    ReplyType(String color) {
        this.color = color;
    }
}
