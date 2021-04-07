package it.polimi.ingsw.model.player.personalBoard.warehouse.depot;

import it.polimi.ingsw.model.exceptions.NegativeResourcesDepotException;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
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
        this.resources = new ArrayList<>();
        this.resources.add(ResourceBuilder.buildShield(0));
        this.resources.add(ResourceBuilder.buildStone(0));
        this.resources.add(ResourceBuilder.buildServant(0));
        this.resources.add(ResourceBuilder.buildCoin(0));

        this.constraints = new ArrayList<>();
    }

    /**
     * This method accept a lambda function predicate with two parameters: first one is always referred to the input resource,
     * second one is the resource already contained in the depot
     *
     * @param constraint is the constraint that the depot checks before adding resources
     */
    @Override
    public void addConstraint(BiPredicate<Resource, Resource> constraint) {
        constraints.add(constraint);
    }

    /**
     * This method inserts the resources into the Depot
     *
     * @param input is the resource that will be inserted
     */
    @Override
    public boolean insert(Resource input) {
        AtomicBoolean storable = new AtomicBoolean(false);
        resources.stream().filter(x->x.equalsType(input)).forEach(x->{
            x.merge(input);
            storable.set(true);
        });
        return storable.get();
    }

    /**
     * This method removes the resources from the Depot
     *
     * @param output is the resource that will be withdrawn
     */
    @Override
    public boolean withdraw(Resource output) throws NegativeResourcesDepotException{
        if (resources.size() != 0){
            int i = 0;
            while (i < resources.size()){
                if (resources.get(i).type() == output.type()) {
                    if (this.resources.get(i).amount() - output.amount() > 0){
                        this.resources.get(i).reduce(output);
                        return true;
                    } else if (this.resources.get(i).amount() - output.amount() == 0){
                        this.resources.get(i).reduce(this.resources.get(i));
                        return true;
                    } else {
                        throw new NegativeResourcesDepotException("exception: The Strongbox does not have enough resources to withdraw");
                    }
                }
                i++;
            }

        } return false;
    }

    /**
     * This method returns the resources that are into the depot
     */
    @Override
    public Resource viewResources() {
        return null;
    }

    public List<Resource> viewAllResources() {
        return resources;
    }
}
