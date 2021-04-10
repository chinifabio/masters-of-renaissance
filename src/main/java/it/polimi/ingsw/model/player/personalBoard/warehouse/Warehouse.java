package it.polimi.ingsw.model.player.personalBoard.warehouse;

import it.polimi.ingsw.model.exceptions.ExtraDepotsException;
import it.polimi.ingsw.model.exceptions.NegativeResourcesDepotException;
import it.polimi.ingsw.model.exceptions.WrongDepotException;
import it.polimi.ingsw.model.player.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.player.personalBoard.Production;
import it.polimi.ingsw.model.player.personalBoard.ProductionID;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.*;
import it.polimi.ingsw.model.resource.Resource;


import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

/**
 * This class is the Warehouse that contains Depots
 */
public class Warehouse {

    /**
     * This attribute associates the Depot with its DepotSlot
     */
    private final Map<DepotSlot, Depot> depots;

    /**
     * This attribute is a list of constraints that the Depots must respect
     */
    private List<Predicate<MoveResource>> constraint;

    /**
     * This method is the constructor of the class
     */
    public Warehouse() {
        //Initializing Depots
        depots = new EnumMap<>(DepotSlot.class);
        depots.put(DepotSlot.BOTTOM, DepotBuilder.buildBottomDepot());
        depots.put(DepotSlot.MIDDLE, DepotBuilder.buildMiddleDepot());
        depots.put(DepotSlot.TOP, DepotBuilder.buildTopDepot());
        depots.put(DepotSlot.SPECIAL1, null);
        depots.put(DepotSlot.SPECIAL2, null);
        depots.put(DepotSlot.STRONGBOX, DepotBuilder.buildStrongBoxDepot());
        depots.put(DepotSlot.BUFFER, DepotBuilder.buildStrongBoxDepot());

        this.constraint = new ArrayList<>();
        this.addConstraint(x -> x.getFrom() != DepotSlot.STRONGBOX);
        this.addConstraint(x-> x.getDest() != DepotSlot.STRONGBOX);
        this.addConstraint(x -> x.getFrom() != DepotSlot.BUFFER);
        this.addConstraint(x-> x.getDest() != DepotSlot.BUFFER);


    }

    public void addConstraint(Predicate<MoveResource> constraint){
        this.constraint.add(constraint);
    }

    /**
     * This method add extra depots into the Warehouse when the SpecialAbility of LeaderCards are activated
     * @param resource is for the creation of extra depot that the LeaderCard adds
     * @return true if the Depot is correctly made and there aren't other extraDepot with the same resources
     * @throws ExtraDepotsException when the LeaderCard adds more than the number of extra depots that the warehouse can creates
     */
    public boolean addDepot(Resource resource) throws ExtraDepotsException {

        for (Map.Entry<DepotSlot, Depot> entry : depots.entrySet()) {
            //Find the first empty space to create extra Depot
            if (entry.getValue() == null) {
                for (Map.Entry<DepotSlot, Depot> newEntry : depots.entrySet()) {
                    //Check if there is already another ExtraDepot with the same resources
                    if (newEntry.getValue() != null) {
                        if(newEntry.getKey() != DepotSlot.BUFFER && newEntry.getKey() != DepotSlot.STRONGBOX && !newEntry.getValue().checkTypeDepot()){
                            if (newEntry.getValue().viewResources().equalsType(resource)){
                                return false;
                            }
                        }
                    }
                }
                entry.setValue(DepotBuilder.buildSpecialDepot(resource));
                return true;
            }
        }
        throw new ExtraDepotsException("exception: All the extra depots have already been built");
    }


    /**
     * This method moves resources from a Depot to another one
     * @param from is the Depot where the resources are taken from
     * @param dest is the Depot where the resources will be stored
     * @param resource is the resource to move
     * @return true if the resources are correctly moved
     * @throws NegativeResourcesDepotException if the Depot "from" hasn't enough resources to move
     * @throws WrongDepotException if the Depot "from" is empty or doesn't have the same type of resources of "resource"
     */
    public boolean moveBetweenDepot(DepotSlot from, DepotSlot dest, Resource resource) throws NegativeResourcesDepotException, WrongDepotException {
        MoveResource moveResource = new MoveResource(from, dest);
        AtomicBoolean testResult = new AtomicBoolean(true);
        this.constraint.forEach(x -> {
            if (!x.test(moveResource)) testResult.set(false);
        });
        if (testResult.get()) {
            if (depots.get(from).viewResources().amount() == 0 || !depots.get(from).viewResources().equalsType(resource)) {
                throw new WrongDepotException("exception: You can't take resources from this Depot");
            }

            if (removeFromDepot(from, resource)) {
                if (insertInDepot(dest, resource)) {
                    return true;
                } else {
                    insertInDepot(from, resource);
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * This method adds productions to the list of available productions that the player could select
     * @param production is the production to add in the list
     */
    public void addProduction(ProductionID productionID, Production production, PersonalBoard personalBoard){
    }

    /**
     * this method allows the player to select the productions that will be activated
     * @param production is the selected production
     */
    public void selectProduction(Production production){
        //Dalla produzione passata prendo i requisite
        //Prendo dai vari depot i requisiti
        //Se ho tutto faccio production.select()
    }

    /**
     * This method activates the productions selected by the player
     */
    public void activateProductions(){
        //Per ogni produzione selezionata prendo le risorse di output e le metto nello strongbox
    }

    /**
     * This method inserts resources into the Depot
     * @param type is the type of Depot
     * @param resource is the resource to insert into the Depot
     * @return true if the resources are correctly inserted
     */
    public boolean insertInDepot(DepotSlot type, Resource resource) {
        if (!depots.get(type).checkTypeDepot()) {
            return depots.get(type).insert(resource);
        }
        for (Map.Entry<DepotSlot, Depot> entry : depots.entrySet()) {
            if ((entry.getValue()) != null && (entry.getValue().checkTypeDepot())) {
                if((entry.getKey() != type) && entry.getValue().viewResources().equalsType(resource)){
                    return false;
                }
            }
        }
        return depots.get(type).insert(resource);
    }

    /**
     * This method remove resources from the Depot
     * @param type is the type of Depot
     * @param resource is the resource to insert into the Depot
     * @return true if the resources are correctly removed
     * @throws NegativeResourcesDepotException if the Depot doesn't have enough resources
     */
    public boolean removeFromDepot(DepotSlot type, Resource resource) throws NegativeResourcesDepotException {
        return depots.get(type).withdraw(resource);
    }

    /**
     * This methods returns the resources inside the Depot of the warehouse
     * @param slot is the Depot from which the Resources are taken
     * @return the resources inside the Depot
     */
    public Resource viewResourcesInDepot(DepotSlot slot){
        return depots.get(slot).viewResources();
    }

    /**
     * This methods returns a list of resources inside the Strongbox
     * @param slot is the Strongbox from which the Resources are taken
     * @return the list of all the Resources inside the Strongbox
     */
    public List<Resource> viewResourcesInStrongbox(DepotSlot slot){
        return depots.get(slot).viewAllResources();
    }
}
