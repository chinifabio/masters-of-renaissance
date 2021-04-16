package it.polimi.ingsw.model.player.personalBoard.warehouse;

import it.polimi.ingsw.model.exceptions.faithtrack.IllegalMovesException;
import it.polimi.ingsw.model.exceptions.productionException.UnknownUnspecifiedException;
import it.polimi.ingsw.model.exceptions.warehouse.*;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalNormalProduction;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalTypeInProduction;
import it.polimi.ingsw.model.player.PlayerReactEffect;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.*;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.NormalProduction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.*;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;


import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

/**
 * This class is the Warehouse that contains Depots
 */
public class Warehouse {

    /**
     * This attribute is the list of the available productions that the Player could activates
     */
    private Map<ProductionID, Production> availableProductions;

    /**
     * This attribute associates the Depot with its DepotSlot
     */
    private final Map<DepotSlot, Depot> depots;

    /**
     * This attribute is a list of constraints that the Depots must respect
     */
    private List<Predicate<MoveResource>> constraint;

    /**
     * This attribute is a list of all the movements done to activate the Productions, it wil be used to restore the Depot
     * status if the Productions won't be activated
     */
    private LinkedList<ProductionRecord> movesCache;

    /**
     * This attribute is the Player that use this Warehouse
     */
    private PlayerReactEffect player;

    /**
     * This method is the constructor of the class
     */
    public Warehouse(PlayerReactEffect player) throws IllegalTypeInProduction {

        this.player = player;

        //Initializing Production
        List<Resource> req = new ArrayList<>();
        List<Resource> output = new ArrayList<>();
        req.add(ResourceBuilder.buildUnknown());
        req.add(ResourceBuilder.buildUnknown());
        output.add(ResourceBuilder.buildUnknown());
        this.availableProductions = new EnumMap<>(ProductionID.class);
        availableProductions.put(ProductionID.BASIC, new UnknownProduction(req,output));
        this.movesCache = new LinkedList<>();

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

        //TODO Fare la parte di Buffer per ricevere le risorse dal mercato

    }

    public void addConstraint(Predicate<MoveResource> constraint){
        this.constraint.add(constraint);
    }

    /**
     * This method add extra depots into the Warehouse when the SpecialAbility of LeaderCards are activated
     * @param newDepot is the new Depot that will be added into the Warehouse
     * @return true if the Depot is correctly made and there aren't other extraDepot with the same resources
     * @throws ExtraDepotsException when the LeaderCard adds more than the number of extra depots that the warehouse can creates
     */
    public boolean addDepot(Depot newDepot) throws ExtraDepotsException {

        for (Map.Entry<DepotSlot, Depot> entry : depots.entrySet()) {
            //Find the first empty space to create extra Depot
            if (entry.getValue() == null) {
                for (Map.Entry<DepotSlot, Depot> newEntry : depots.entrySet()) {
                    //Check if there is already another ExtraDepot with the same resources
                    if (newEntry.getValue() != null) {
                        if(newEntry.getKey() != DepotSlot.BUFFER && newEntry.getKey() != DepotSlot.STRONGBOX && !newEntry.getValue().checkTypeDepot()){
                            if (newEntry.getValue().viewResources().equalsType(newDepot.viewResources())){
                                return false;
                            }
                        }
                    }
                }
                entry.setValue(newDepot);
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
    public boolean moveBetweenDepot(DepotSlot from, DepotSlot dest, Resource resource) throws NegativeResourcesDepotException, WrongDepotException, UnobtainableResourceException, WrongPointsException, IllegalMovesException {
        MoveResource moveResource = new MoveResource(from, dest, resource);
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


    public boolean moveInProduction(DepotSlot from, ProductionID dest, Resource resource) throws NegativeResourcesDepotException, UnobtainableResourceException, UnknownUnspecifiedException, WrongPointsException, IllegalMovesException {
        ProductionRecord temp = new ProductionRecord(from, dest, resource);
        if (removeFromDepot(from, resource)){
            if (availableProductions.get(dest).insertResource(resource)){
                this.movesCache.add(temp);
                return true;
            } else {
                insertInDepot(from, resource);
                return false;
            }
        }
        return false;
        //TODO DA RICONTROLLARE


    }

    /**
     * This method activates the productions selected by the player
     */
    public boolean activateProductions() throws UnobtainableResourceException, WrongPointsException, IllegalMovesException {

        for (Map.Entry<ProductionID, Production> entry : availableProductions.entrySet()) {
            if (entry.getValue().isSelected() && !entry.getValue().activate()){
                clearProduction();
                return false;
            }
        }

        for (Map.Entry<ProductionID, Production> entry : availableProductions.entrySet()){
            if(entry.getValue().isActivated()){
                for (Resource res : entry.getValue().getOutput()){
                    insertInDepot(DepotSlot.STRONGBOX, res);
                }
            }
            entry.getValue().reset();
        }
        return true;
        //TODO Da ricontrollare
        //Per ogni produzione selezionata prendo le risorse di output e le metto nello strongbox
    }

    /**
     * This method inserts resources into the Depot
     * @param type is the type of Depot
     * @param resource is the resource to insert into the Depot
     * @return true if the resources are correctly inserted
     */
    public boolean insertInDepot(DepotSlot type, Resource resource) throws UnobtainableResourceException, WrongPointsException, IllegalMovesException {
        resource.onObtain(player);
        if (resource.isStorable()) {
            if (!depots.get(type).checkTypeDepot()) {
                return depots.get(type).insert(resource);
            }
            for (Map.Entry<DepotSlot, Depot> entry : depots.entrySet()) {
                if ((entry.getValue()) != null && (entry.getValue().checkTypeDepot())) {
                    if ((entry.getKey() != type) && entry.getValue().viewResources().equalsType(resource)) {
                        return false;
                    }
                }
            }
            return depots.get(type).insert(resource);
        }
        return false;
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
    public Resource viewResourcesInDepot(DepotSlot slot) throws NullPointerException{
        return depots.get(slot).viewResources();
    }

    /**
     * This methods returns a list of resources inside the Strongbox
     * @return the list of all the Resources inside the Strongbox
     */
    public List<Resource> viewResourcesInStrongbox(){
        return depots.get(DepotSlot.STRONGBOX).viewAllResources();
    }

    public void clearProduction() throws UnobtainableResourceException, WrongPointsException, IllegalMovesException {
        restoreProductions();
        for (Map.Entry<ProductionID, Production> entry : availableProductions.entrySet()) {
            entry.getValue().reset();
        }
    }

    public boolean setNormalProduction(ProductionID productionID, NormalProduction normalProduction) throws IllegalNormalProduction {
        try {
            availableProductions.get(productionID).setNormalProduction(normalProduction);
            return true;
        } catch (IllegalNormalProduction e) {
            throw new IllegalNormalProduction(normalProduction, "The method to set this production to normal failed");
        }
    }

    public boolean restoreProductions() throws UnobtainableResourceException, WrongPointsException, IllegalMovesException {
        for (ProductionRecord record : movesCache){
            if(!insertInDepot(record.getFrom(), record.getResources())) return false;
        }
        return true;
    }

    //maybe only for testing
    public void addProduction(ProductionID key, Production value){
        this.availableProductions.put(key, value);
    }

    public Map<ProductionID, Production> getProduction(){
        return this.availableProductions;
    }
}
