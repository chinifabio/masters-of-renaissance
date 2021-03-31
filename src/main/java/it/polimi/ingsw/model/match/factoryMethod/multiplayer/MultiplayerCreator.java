package it.polimi.ingsw.model.match.factoryMethod.multiplayer;

import it.polimi.ingsw.model.match.factoryMethod.Match;
import it.polimi.ingsw.model.match.factoryMethod.multiplayer.MatchCreator;

/**
 * create an istance of a Multiplayer match, it is used in the factory method pattern
 */
public class MultiplayerCreator implements MatchCreator {
    /**
     * create an istance of a Multiplayer match, it is used in the factory method pattern
     * @return a multiplayer match
     */
    @Override
    public Match createMatch() {
        return null;
    }
}
