package it.polimi.ingsw.model.player.personalBoard.warehouse;

import it.polimi.ingsw.model.player.personalBoard.Production;
import it.polimi.ingsw.model.player.personalBoard.ProductionID;
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
     * This attribute associates the corresponding Depot with the DepotSlot
     */
    private Map<DepotSlot, Depot> depots;

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
    }

    /**
     * This method add extra Depot into the Warehouse after the special ability of LeaderCard is activated
     */
    public void addDepot(Depot depot) {
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
     * @param productionID is the selected production
     */
    public void selectProduction(ProductionID productionID){
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
}
