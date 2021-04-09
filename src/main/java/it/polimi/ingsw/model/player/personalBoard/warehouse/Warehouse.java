package it.polimi.ingsw.model.player.personalBoard.warehouse;

import it.polimi.ingsw.model.exceptions.ExtraDepotsException;
import it.polimi.ingsw.model.exceptions.NegativeResourcesDepotException;
import it.polimi.ingsw.model.exceptions.WrongDepotException;
import it.polimi.ingsw.model.player.personalBoard.Production;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.*;
import it.polimi.ingsw.model.resource.Resource;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
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
        depots.put(DepotSlot.STRONGBOX, DepotBuilder.buildStrongBoxDepot());
        depots.put(DepotSlot.BUFFER, DepotBuilder.buildStrongBoxDepot());
        depots.put(DepotSlot.SPECIAL1, null);
        depots.put(DepotSlot.SPECIAL2, null);
    }

    /**
     * This method add extra depots into the Warehouse when the SpecialAbility of LeaderCards are activated
     * @param resource is for the creation of extra depot that the LeaderCard adds
     * @throws ExtraDepotsException when the LeaderCard adds more than the number of extra depots that the warehouse can creates
     */
    public void addDepot(Resource resource) throws ExtraDepotsException {
        for(Map.Entry<DepotSlot,Depot> entry : depots.entrySet()){
            if (entry.getValue() == null){
                entry.setValue(DepotBuilder.buildSpecialDepot(resource));
                return;
            }
        }
        throw new ExtraDepotsException("exception: All the extra depots have already been built");
    }


    /**
     * This method moves resources between depots
     * @param from is the depot from which the resources are taken
     * @param dest is the depot where the resources will be deposited
     */
    public boolean moveBetweenDepot(DepotSlot from, DepotSlot dest) throws NegativeResourcesDepotException, WrongDepotException {
        if (dest == DepotSlot.STRONGBOX || from == DepotSlot.STRONGBOX || depots.get(from).viewResources().amount() == 0){
            throw new WrongDepotException("exception: You can't take resources from this Depot");
        }

        if (depots.get(dest).viewResources().amount() == 0) {
            if(depots.get(dest).insert(depots.get(from).viewResources())){
                depots.get(from).withdraw(depots.get(from).viewResources());
            }
            return true;
        }
        return false;
    }

    /**
     * This method adds productions to the list of available productions that the player could select
     * @param production is the production to add in the list
     */
    public void addProduction(Production production){

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
    public boolean insertInDepot(DepotSlot type, Resource resource){
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
