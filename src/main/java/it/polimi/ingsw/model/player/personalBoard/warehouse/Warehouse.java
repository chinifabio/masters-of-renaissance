package it.polimi.ingsw.model.player.personalBoard.warehouse;

import it.polimi.ingsw.model.exceptions.ExtraDepotsException;
import it.polimi.ingsw.model.player.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.player.personalBoard.Production;
import it.polimi.ingsw.model.player.personalBoard.ProductionID;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.*;
import it.polimi.ingsw.model.requisite.ResourceRequisite;
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
     * This attribute associates the corresponding Depot with the DepotSlot
     */
    private Map<DepotSlot, Depot> depots;

    /**
     * This attribute is a list of constraints that the Depots must respect
     */
    private List<Predicate<MoveResource>> constraint;

    private Exception ExtraDepotsException;
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
     * @throws ExtraDepotsException when the LeaderCard adds more than two extra depots
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
     * @param howMany is the amount of resources that will be moved
     */
    public void moveBetweenDepot(DepotSlot from, DepotSlot dest, int howMany){

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

    public boolean insertInDepot(DepotSlot type, Resource resource){
        return depots.get(type).insert(resource);
    }

    public Resource viewResourcesInDepot(DepotSlot slot){
        return depots.get(slot).viewResources();
    }

    public List<Resource> viewResourcesInStrongbox(DepotSlot slot){
        return depots.get(slot).viewAllResources();
    }
}
