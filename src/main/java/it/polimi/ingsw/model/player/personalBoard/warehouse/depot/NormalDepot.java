package it.polimi.ingsw.model.player.personalBoard.warehouse.depot;


import it.polimi.ingsw.model.exceptions.warehouse.NegativeResourcesDepotException;
import it.polimi.ingsw.model.exceptions.warehouse.WrongDepotException;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import it.polimi.ingsw.model.resource.ResourceType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiPredicate;


/**
 * This class is the Depot into the warehouse where the resources will be stored
 */
public class NormalDepot implements Depot {

    /**
     * This attribute is the type and the number of resources into the depot
     */
    private Resource resources;

    /**
     * This attribute is a constraint that verifies that the resources within the depot are of the same type
     */
    private final List<BiPredicate<Resource, Resource>> constraints;

    /**
     * This method is the constructor of the class
     */
    public NormalDepot() {
        resources = ResourceBuilder.buildEmpty();
        this.constraints = new ArrayList<>();

    }

    /**
     * This method accept a lambda function predicate with two parameters: first one is always referred to the input resource,
     * second one is the resource already contained in the depot
     * @param constraint is the constraint that the depot checks before adding resources
     */
    public void addConstraint(BiPredicate<Resource,Resource> constraint){
        constraints.add(constraint);
    }

    /**
     * This method inserts the resources into the Depot, first of all verifies that the constraints are respected, then
     * merges the resources that already are inside the depot with the input or inserts the new resources if the Depot is empty
     * @param input is the resource that will be inserted
     */
    public boolean insert(Resource input) {
        AtomicBoolean testResult = new AtomicBoolean(true);
        this.constraints.forEach(x->{
            if (!x.test(input,this.resources)) testResult.set(false);
        });

        if (testResult.get()){
            if (resources.type() != ResourceType.EMPTY){
                this.resources.merge(input);
            }
            else { this.resources = input;}
            return true;
        }
        return  false;
    }

    /**
     * This method removes the resources inside the Depot, first of all verifies that the Depot isn't empty, then removes
     * the resources from the Depot or throws an exception if the Depot doesn't contain enough resources to withdraw
     * @param output is the resource that will be withdrawn
     * @return true if the resources are correctly withdrawn
     * @throws NegativeResourcesDepotException if the Depot doesn't contain enough resources
     */
    public boolean withdraw(Resource output) throws NegativeResourcesDepotException {
        if (!(this.resources.equalsType(output)) && (this.resources.type() != ResourceType.EMPTY)){
            return false;
        }
        if (this.resources.amount() - output.amount() > 0){
            this.resources.reduce(output);
            return true;
        } else if (this.resources.amount() - output.amount() == 0){
            this.resources = ResourceBuilder.buildEmpty();
            return true;
        } else {
            throw new NegativeResourcesDepotException();
        }
    }


    /**
     * This method cannot be invoked because the NormalDepot can have only one type of resources each time
     * @throws WrongDepotException if it's invoked
     */
    @Override
    public List<Resource> viewResources(){
        List<Resource> temp = new ArrayList();
        temp.add(ResourceBuilder.buildFromType(resources.type(),resources.amount()));
        return temp;
    }


    /**
     * This method checks if this Depot must have a control on the type of the Resources on this Depot
     *
     * @return true
     */
    @Override
    public boolean checkTypeDepot() {
        return true;
    }
}
