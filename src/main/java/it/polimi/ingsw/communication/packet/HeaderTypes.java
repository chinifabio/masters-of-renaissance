package it.polimi.ingsw.communication.packet;

public enum HeaderTypes {
    HELLO,
    JOIN_LOBBY,
    CREATE_LOBBY,
    SET_PLAYERS_NUMBER,
    WAIT_FOR_START,
    GAME_STARTED,
    START_TURN,
    DO_ACTION,
    END_TURN,
    WAIT,
    END_GAME,
    OK,
    INVALID,
    NOTIFY,
    PING,
    PONG
}
