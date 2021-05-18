package it.polimi.ingsw.model;

public interface MappableToLiteVersion {

    /**
     * Create a lite version of the class and serialize it in json
     * @return the json representation of the lite version of the class
     */
    Object liteVersion();
}
