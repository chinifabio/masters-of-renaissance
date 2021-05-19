package it.polimi.ingsw.communication;

public interface Disconnectable {
    void handleDisconnection();
    VirtualSocket disconnectableSocket();
}
