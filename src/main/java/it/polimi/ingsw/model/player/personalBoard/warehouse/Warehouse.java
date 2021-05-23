package it.polimi.ingsw.model.player.personalBoard.warehouse;

import it.polimi.ingsw.communication.packet.updates.DepotUpdater;
import it.polimi.ingsw.communication.packet.updates.ProductionUpdater;
import it.polimi.ingsw.model.exceptions.ExtraProductionException;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.exceptions.warehouse.production.UnknownUnspecifiedException;
import it.polimi.ingsw.model.exceptions.warehouse.*;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalNormalProduction;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalTypeInProduction;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.*;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.NormalProduction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.*;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.*;


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
    private final Map<ProductionID, Production> availableProductions;

    /**
     * This attribute associates the Depot with its DepotSlot
     */
    private final Map<DepotSlot, Depot> depots;

    /**
     * This attribute is a list of constraints that the Depots must respect
     */
    private final List<Predicate<MoveResource>> constraint;

    /**
     * This attribute is a list of all the movements done to activate the Productions, it wil be used to restore the Depot
     * status if the Productions won't be activated
     */
    private final LinkedList<ProductionRecord> movesCache;

    /**
     * The owner of the warehouse
     */
    private final Player player;

    /**
     * This method is the constructor of the class
     * @throws IllegalTypeInProduction if the Basic Production has IllegalResources
     */
    public Warehouse(Player player) throws IllegalTypeInProduction {
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
        this.addConstraint(x -> x.getFrom() != DepotSlot.STRONGBOX || x.getDest() == DepotSlot.BUFFER);
        this.addConstraint(x-> x.getDest() != DepotSlot.STRONGBOX || x.getFrom() == DepotSlot.BUFFER);

        this.player = player;

      for (DepotSlot slot : DepotSlot.values()){
          if (depots.get(slot) != null){
              updateDepot(slot);
          }
      }
      updateProduction(ProductionID.BASIC);
    }

    /**
     * This method add the Constraint for the Depots when the Player wants to move resources between Depots
     * @param constraint is the constraint added to Depots
     */
    public void addConstraint(Predicate<MoveResource> constraint){
        this.constraint.add(constraint);
    }

    /**
     * This method add extra depots into the Warehouse when the SpecialAbility of LeaderCards are activated
     * @param depot is the new Depot that will be added into the Warehouse
     * @throws ExtraDepotsException when the LeaderCard adds more than the number of extra depots that the warehouse can creates
     */
    public void addDepot(Depot depot) throws ExtraDepotsException {

        for (Map.Entry<DepotSlot, Depot> entry : depots.entrySet()) {
            //Find the first empty space to create extra Depot
            if (entry.getValue() == null) {
                entry.setValue(depot);
                updateDepot(entry.getKey());
                return;
            }
        }
        throw new ExtraDepotsException();
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
        MoveResource moveResource = new MoveResource(from, dest, resource);
        AtomicBoolean testResult = new AtomicBoolean(true);
        this.constraint.forEach(x -> {
            if (!x.test(moveResource)) testResult.set(false);
        });

        if (testResult.get()) {

            if ((from != DepotSlot.BUFFER && from != DepotSlot.STRONGBOX) && (viewResourcesInDepot(from).get(0).amount() == 0 || !viewResourcesInDepot(from).get(0).equalsType(resource))) {
                throw new WrongDepotException();
            }

            if (removeFromDepot(from, resource)) {
                if (insertInDepot(dest, resource)) {
                    updateDepot(dest);
                    updateDepot(from);
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
     * This method moves resources to the Production
     * @param from is the DepotSlot where the Resources are taken from
     * @param dest is the Production that will uses the resources
     * @param resource is the resources to move
     * @return true if the resources are correctly moved
     * @throws NegativeResourcesDepotException if the Depot hasn't enough resources
     * @throws UnknownUnspecifiedException if the Production is unspecified
     * @throws WrongDepotException is the Player can't take resources from that Depot
     */
    public boolean moveInProduction(DepotSlot from, ProductionID dest, Resource resource) throws NegativeResourcesDepotException, UnknownUnspecifiedException, WrongDepotException {
        ProductionRecord temp = new ProductionRecord(from, dest, resource);
        if (removeFromDepot(from, resource)){
            if (availableProductions.get(dest).insertResource(resource)){
                this.movesCache.add(temp);
                updateDepot(from);
                updateProduction(dest);
                return true;
            } else {
                insertInDepot(from, resource);
                return false;
            }
        }
        return false;
    }

    /**
     * This method activates the Productions selected by the Player
     * @return true if the resources are correctly activated
     * @throws UnobtainableResourceException if the Player can't obtain that resources
     * @throws EndGameException if the Player can't do this action
     * @throws WrongDepotException if the Resources can't be stored when a production is activated
     */
    public boolean activateProductions() throws UnobtainableResourceException, EndGameException, WrongDepotException {

        for (Map.Entry<ProductionID, Production> entry : availableProductions.entrySet()) {
            if (entry.getValue().isSelected() && !entry.getValue().activate()){
                clearProduction();
                return false;
            }
        }

        for (Map.Entry<ProductionID, Production> entry : availableProductions.entrySet()){
            if(entry.getValue().isActivated()){
                for (Resource res : entry.getValue().getOutput()){
                    player.obtainResource(DepotSlot.STRONGBOX,res);
                    //insertInDepot(DepotSlot.STRONGBOX, res);
                }
            }
            entry.getValue().reset();
            updateProduction(entry.getKey());
        }

        updateDepot(DepotSlot.BUFFER);
        return true;
    }

    /**
     * This method inserts resources into the Depot
     * @param type is the type of Depot
     * @param resource is the resource to insert into the Depot
     * @return true if the resources are correctly inserted
     * @throws WrongDepotException if the Resources can't be stored in that Depot
     */
    public boolean insertInDepot(DepotSlot type, Resource resource) throws WrongDepotException {
        if (!depots.get(type).checkTypeDepot()) {
            if (depots.get(type).insert(resource)){
                updateDepot(type);
                return true;
            } return false;
        }

        for (Map.Entry<DepotSlot, Depot> entry : depots.entrySet()) {
            if ((entry.getValue()) != null && (entry.getValue().checkTypeDepot())) {
                if ((entry.getKey() != type) && entry.getValue().viewResources().get(0).equalsType(resource)) {
                    return false;
                }
            }
        }

        if (depots.get(type).insert(resource)) {
            updateDepot(type);
            return true;
        } return false;
    }

    /**
     * This method remove resources from the Depot
     * @param type is the type of Depot
     * @param resource is the resource to insert into the Depot
     * @return true if the resources are correctly removed
     * @throws NegativeResourcesDepotException if the Depot doesn't have enough resources
     */
    public boolean removeFromDepot(DepotSlot type, Resource resource) throws NegativeResourcesDepotException {
        if (depots.get(type).withdraw(resource)) {
            updateDepot(type);
            return true;
        }
        return false;
    }

    /**
     * This methods returns the resources inside the Depot of the warehouse
     * @param slot is the Depot from which the Resources are taken
     * @return the resources inside the Depot
     */
    public List<Resource> viewResourcesInDepot(DepotSlot slot) throws NullPointerException{
        return depots.get(slot).viewResources();
    }


    /**
     * This method reset the Productions
     */
    public void clearProduction(){
        try {
            restoreProductions();
        } catch (WrongDepotException e) {
            e.printStackTrace();
        }
        for (Map.Entry<ProductionID, Production> entry : availableProductions.entrySet()) {
            entry.getValue().reset();
            updateProduction(entry.getKey());
        }
    }

    /**
     * This method set the UnknownProduction to a NormalProduction
     * @param id is the Production to convert
     * @param normalProduction is the new Normal Production
     * @return true if the Production is correctly converted
     * @throws IllegalNormalProduction if the production isn't an UnknownProduction
     */
    public boolean setNormalProduction(ProductionID id, NormalProduction normalProduction) throws IllegalNormalProduction {
        try {
            availableProductions.get(id).setNormalProduction(normalProduction);
            updateProduction(id);
            return true;
        } catch (IllegalNormalProduction e) {
            throw new IllegalNormalProduction(normalProduction, "The method to set this production to normal failed");
        }
    }

    /**
     * This method restore the Resources taken to activate the Production if it failed
     * @throws WrongDepotException if the Resources can be restored
     */
    public void restoreProductions() throws WrongDepotException {
        for (ProductionRecord record : movesCache){
            insertInDepot(record.getFrom(), record.getResources());
        }
    }

    /**
     * This method set the new production of the develop cards
     * @param key the id of the production
     * @param value the new production
     */
    public void addProduction(ProductionID key, Production value){
        this.availableProductions.put(key, value);
        updateProduction(key);
    }

    /**
     * This method add end extra production in the available production
     * @param prod the new production
     * @throws ExtraProductionException error thrown when an error occur during this operation
     */
    public void addExtraProduction(Production prod) throws ExtraProductionException {
        for (ProductionID id : ProductionID.special()){
            if (availableProductions.get(id) == null) {
                availableProductions.put(id, prod);
                updateProduction(id);
                return;
            }
        }
        throw new ExtraProductionException();
    }

    /**
     * This method counts the total number of resources inside the Warehouse and divides it to 5 to obtain the VictoryPoints
     * @return the value of VictoryPoints of the Warehouse
     */
    public int countPointsWarehouse(){
        int total = 0;
        for (Map.Entry<DepotSlot, Depot> entry : depots.entrySet()){
            if (!(entry.getValue() == null || entry.getKey() == DepotSlot.STRONGBOX || entry.getKey()==DepotSlot.BUFFER)){
                total = total + entry.getValue().viewResources().get(0).amount();
            }
        }
        for (Resource res : viewResourcesInDepot(DepotSlot.STRONGBOX)){
            total = total + res.amount();
        }

        return total/5;
    }

    /**
     * This method counts the amount of each type of resources in the Warehouse
     * @return a list with all resources and the corresponding amount
     */
    public List<Resource> getTotalResources(){
        List<Resource> totalResource = ResourceBuilder.buildListOfStorable();
        for (Map.Entry<DepotSlot, Depot> entry : depots.entrySet()){
            if (!(entry.getValue() == null || entry.getKey() == DepotSlot.STRONGBOX || entry.getKey() == DepotSlot.BUFFER)){
                for (Resource resource : totalResource){
                    if (resource.equalsType(entry.getValue().viewResources().get(0))){
                        resource.merge(entry.getValue().viewResources().get(0));
                    }
                }
            }
        }
        for (Resource res : viewResourcesInDepot(DepotSlot.STRONGBOX)){
            for (Resource res2 : totalResource){
                res2.merge(res);
            }
        }

        return totalResource;
    }

    /**
     * This method flush the buffer depot by replacing it whit a new one empty
     */
    public void flushBufferDepot() {
        depots.put(DepotSlot.BUFFER, DepotBuilder.buildStrongBoxDepot());
        updateDepot(DepotSlot.BUFFER);
    }

    /**
     * This method return the Map of possible production that the Player could activates
     * @return the Map of ProductionID - Production
     */
    public Map<ProductionID, Production> getProduction(){
        Map<ProductionID, Production> temp = new EnumMap<>(ProductionID.class);
        temp.putAll(this.availableProductions);
        return temp;
    }

    protected void updateDepot(DepotSlot depot) {
        this.player.view.publish(new DepotUpdater(this.player.getNickname(), this.depots.get(depot).liteVersion(), depot));
    }

    protected void updateProduction(ProductionID prod) {
        this.player.view.publish(new ProductionUpdater(this.player.getNickname(), this.availableProductions.get(prod).liteVersion(), prod));
    }

    // for testing
    public Map<DepotSlot, Depot> getDepot() {
        return this.depots;
    }
    // for testing
    public Map<ProductionID, Production> test_getProduction() {
        return this.availableProductions;
    }


}
