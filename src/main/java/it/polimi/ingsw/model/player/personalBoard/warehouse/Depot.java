package it.polimi.ingsw.model.player.personalBoard.warehouse;


import it.polimi.ingsw.model.resource.builder.Resource;

import java.util.List;
import java.util.function.BiPredicate;


/**
 * This class is the Depot into the warehouse where the resources will be stored
 */
public class Depot {
    /**
     * This attribute is the type and the number of resources into the depot
     */
    private Resource resources;

    /**
     * This attribute is a constraint that verifies that the resources within the depot are of the same type
     */
    private List<BiPredicate<Resource, Resource>> constraints;

    /**
     * This method is the constructor of the calss
     * @param resources indicates the resources inside the Depot
     */
    public Depot(Resource resources){
        this.resources = resources;
    }

    /**
     * This method inserts the resources into the Depot
     * @param input is the resource that will be inserted
     */
    public void insert(Resource input){
        this.resources = input;
    }

    /**
     * This method removes the resources from the Depot
     * @param output is the resource that will be withdrawn
     */
    public void withdraw(Resource output){

    }

    /**
     * This method returns the resources that are into the depot
     */
    public Resource getResources;


}
