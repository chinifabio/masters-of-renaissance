package it.polimi.ingsw.communication.packet;

/**
 * This class identifies all the header types of the packets
 */
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
    TIMEOUT,
    TOKEN
}
