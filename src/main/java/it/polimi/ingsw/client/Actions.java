package it.polimi.ingsw.client;

public enum Actions {
    USE_MARKET_TRAY("usemarket"),

    PAINT_MARBLE_IN_TRAY("paintmarble"),
    BUY_DEV_CARD("buydevcard"),
    ACTIVATE_PRODUCTION("activateproduction"),
    MOVE_IN_PRODUCTION("moveinproduction"),
    SET_NORMAL_PRODUCTION("normalizeproduction"),
    MOVE_BETWEEN_DEPOT("moveindepot"),
    ACTIVATE_LEADER_CARD("activateleader"),
    DISCARD_LEADER_CARD("discardleader"),
    CHOOSE_RESOURCE("chooseresource"),
    END_TURN("done"),

    VIEW_MARKET_TRAY("market"),
    VIEW_DEV_SETUP("cardmarket"),
    VIEW_FAITH_TRACK("track"),
    VIEW_PERSONAL_BOARD("personalboard"),
    VIEW_LEADER_CARD("leadercards"),
    VIEW_PRODUCTIONS("productions"),
    VIEW_WAREHOUSE("wawrehosue"),
    VIEW_PLAYERS("players"),

    RETURN_HOME("home"),
    HELP("help"),
    NULL("");

    public final String commandString;

    Actions(String c) {
        this.commandString = c;
    }

    public static Actions fromString(String input) {
        for (Actions a : Actions.values()) {
            if (a.commandString.equalsIgnoreCase(input)) return a;
        }
        return NULL;
    }
}
