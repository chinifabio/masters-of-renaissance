package it.polimi.ingsw.model.resource;

import it.polimi.ingsw.model.exceptions.NegativeResourceException;
import it.polimi.ingsw.model.resource.strategy.DoNothingBehavior;
import it.polimi.ingsw.model.resource.strategy.GiveFaithPointBehavior;
import it.polimi.ingsw.model.resource.strategy.LaunchExceptionBehavior;

/**
 * class that contains all the builders for all the resource types
 */
public class ResourceBuilder {

    /**
     * build a coin resource whit amount = 1
     * @return the resource created
     */
    public static Resource buildCoin() {
        return new Resource(ResourceType.COIN, new DoNothingBehavior());
    }

    /**
     * build a coin resource whit custom amount
     * @param amount amount of the resource
     * @return the resource created
     */
    public static Resource buildCoin(int amount) {
        return new Resource(ResourceType.COIN, new DoNothingBehavior(), amount);
    }

    /**
     * build a servant resource whit amount = 1
     * @return the resource created
     */
    public static Resource buildServant() {
        return new Resource(ResourceType.SERVANT, new DoNothingBehavior());
    }

    /**
     * build a servant resource whit custom amount
     * @param amount amount of the resource
     * @return the resource created
     */
    public static Resource buildServant(int amount) {
        return new Resource(ResourceType.SERVANT, new DoNothingBehavior(), amount);
    }

    /**
     * build a shield resource whit amount = 1
     * @return the resource created
     */
    public static Resource buildShield() {
        return new Resource(ResourceType.SHIELD, new DoNothingBehavior());
    }

    /**
     * build a shield resource whit custom amount
     * @param amount amount of the resource
     * @return the resource created
     */
    public static Resource buildShield(int amount) {
        return new Resource(ResourceType.SHIELD, new DoNothingBehavior(), amount);
    }

    /**
     * build a stone resource whit amount = 1
     * @return the resource created
     */
    public static Resource buildStone() {
        return new Resource(ResourceType.STONE, new DoNothingBehavior());
    }

    /**
     * build a stone resource whit custom amount
     * @param amount amount of the resource
     * @return the resource created
     */
    public static Resource buildStone(int amount) {
        return new Resource(ResourceType.STONE, new DoNothingBehavior(), amount);
    }

    /**
     * build a faithpoint resource whit amount = 1
     * @return the resource created
     */
    public static Resource buildFaithPoint() {
        return new Resource(ResourceType.FAITHPOINT, new GiveFaithPointBehavior(1));
    }

    /**
     * build a faithpoint resource whit custom amount
     * @param amount amount of the resource
     * @return the resource created
     */
    public static Resource buildFaithPoint(int amount) {
        return new Resource(ResourceType.FAITHPOINT, new GiveFaithPointBehavior(amount), amount);
    }

    /**
     * build a unknown resource whit amount = 1
     * @return the resource created
     */
    public static Resource buildUnknown() {
        return new Resource(ResourceType.UNKNOWN, new LaunchExceptionBehavior());
    }

    /**
     * build a unknown resource whit custom amount
     * @param amount amount of the resource
     * @return the resource created
     */
    public static Resource buildUnknown(int amount) {
        return new Resource(ResourceType.UNKNOWN, new LaunchExceptionBehavior(), amount);
    }
}
