package it.polimi.ingsw.model.player.personalBoard.warehouse.depot;

import it.polimi.ingsw.model.exceptions.warehouse.NegativeResourcesDepotException;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

public class Strongbox implements Depot {

    /**
     * This attribute is the type and the number of resources into the depot
     */
    private final List<Resource> resources;

    /**
     * This attribute is a constraint that verifies that the resources within the depot are of the same type
     */
    private final List<BiPredicate<Resource, Resource>> constraints;

    /**
     * This method is the constructor of the class
     */
    public Strongbox() {
        this.resources = ResourceBuilder.buildListOfStorable();

        this.constraints = new ArrayList<>();
    }

    /**
     * This method accept a lambda function predicate with two parameters: first one is always referred to the input resource,
     * second one is the resource already contained in the depot
     * @param constraint is the constraint that the depot checks before adding resources
     */
    @Override
    public void addConstraint(BiPredicate<Resource, Resource> constraint) {
        constraints.add(constraint);
    }

    /**
     * This method inserts the resources inside the Strongbox, it merges the resources with the ones inside the Strongbox
     * @param input is the resource that will be inserted
     * @return true if the resources are correctly inserted
     */
    @Override
    public boolean insert(Resource input) {
        if(input.isStorable()) return resources.stream().filter(x->x.equalsType(input)).reduce(null, (x, y) -> y).merge(input);
        else return false;
    }

    /**
     * This method removes the resources inside the Strongbox, it search the resources of the same type of the resources
     * to be withdraw and then checks if the Strongbox have enough resources to remove, otherwise it throws an exception
     * @param output is the resources that will be withdrawn
     * @return true if the resources are correctly withdrawn
     * @throws NegativeResourcesDepotException if the Strongbox doesn't have enough resources to remove
     */
    @Override
    public boolean withdraw(Resource output) throws NegativeResourcesDepotException{
        if (resources.size() != 0){
            for (Resource resource : resources) {
                if (resource.type() == output.type()) {
                    if (resource.amount() - output.amount() >= 0) {
                        resource.reduce(output);
                        return true;
                    } else {
                        throw new NegativeResourcesDepotException("exception: The Strongbox does not have enough resources to withdraw");
                    }
                }
            }

        } return false;
    }

    /**
     * This methods return null because the Strongbox doesn't have only one type of resources
     * @return null
     */
    @Override
    public Resource viewResources() {
        return null;
    }

    /**
     * This method returns a list of all the resources inside the Strongbox
     * @return a list of all the resources inside the Strongbox
     */
    public List<Resource> viewAllResources() {
        List<Resource> clone = new ArrayList<>(this.resources.size());
        clone.addAll(this.resources);
        return clone;
    }

    /**
     * This method checks if this Depot must have a control on the type of the Resources on this Depot
     *
     * @return false
     */
    @Override
    public boolean checkTypeDepot() {
        return false;
    }
}
