package it.polimi.ingsw.model.resource.builder;

/**
 * conatins the methods to build a resource
 */
public interface ResourceBuilder {
    /**
     * build the resource whit default amount = 1
     * @return the new resource
     */
    Resource build();

    /**
     * build the resource whit the custom amount passed as parameter
     * @param amount amount of the resource created
     * @return the new resource
     */
    Resource build(int amount);
}
