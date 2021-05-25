package it.polimi.ingsw.communication.packet;

public enum HeaderTypes {
    HELLO,
    JOIN_LOBBY,
    CREATE_LOBBY,
    SET_PLAYERS_NUMBER,
    WAIT_FOR_START,
    DO_ACTION,
    END_GAME,
    OK,
    INVALID,
    NOTIFY,
    PING,
    PONG,
    TIMEOUT
}
