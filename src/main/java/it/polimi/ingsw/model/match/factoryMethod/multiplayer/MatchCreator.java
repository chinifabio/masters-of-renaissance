package it.polimi.ingsw.model.match.factoryMethod.multiplayer;

import it.polimi.ingsw.model.match.factoryMethod.Match;

/**
 * interface containing the method createMatch for the factory method of the Match class
 */
public interface MatchCreator {
    /**
     * create a match and return it
     * @return match type
     */
    Match createMatch();
}
