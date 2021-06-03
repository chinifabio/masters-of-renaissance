package it.polimi.ingsw.communication.packet;

public enum HeaderTypes {
    HELLO,
    RECONNECTED,
    GAME_INIT,
    GAME_START,
    SET_PLAYERS_NUMBER,
    DO_ACTION,
    END_GAME,
    OK,
    INVALID,
    NOTIFY,
    PING,
    PONG,
    TIMEOUT,
    LOCK,
    UNLOCK
}
