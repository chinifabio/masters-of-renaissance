package it.polimi.ingsw.model.player.personalBoard.warehouse.depot;

import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceType;

/**
 * This class builds different types of Depot
 */
public class DepotBuilder {

    /**
     * This method builds the Bottom Depot that can contains at maximum of 3 resources of the same type
     * @return the newly built Depot
     */
    public static Depot buildBottomDepot(){
        Depot temp = new NormalDepot();
        temp.addConstraint((x,y) -> y.type() == ResourceType.EMPTY || x.type() == y.type());
        temp.addConstraint((x,y) -> (x.amount() + y.amount())<= 3);
        return temp;
    }

    /**
     * This method builds the Middle Depot that can contains at maximum of 2 resources of the same type
     * @return the newly built Depot
     */
    public static Depot buildMiddleDepot(){
        Depot temp = new NormalDepot();
        temp.addConstraint((x,y) -> y.type() == ResourceType.EMPTY || x.type() == y.type());
        temp.addConstraint((x,y) -> (x.amount() + y.amount())<= 2);
        return temp;
    }

    /**
     * This method builds the Top Depot that can contains at maximum of 2 resources of the same type
     * @return the newly built Depot
     */
    public static Depot buildTopDepot(){
        Depot temp = new NormalDepot();
        temp.addConstraint((x,y) -> y.type() == ResourceType.EMPTY || x.type() == y.type());
        temp.addConstraint((x,y) -> (x.amount() + y.amount())<= 1);
        return temp;
    }

    /**
     * This method builds the Special Depot when the SpecialAbility of the LeaderCards is activated,
     * the Depot can contains only 2 resources of the same type of res
     * @param res is the type of resources that the Depot can contains
     * @return the newly built Depot
     */
    public static Depot buildSpecialDepot(Resource res){
        Depot temp = new SpecialDepot(res);
        temp.addConstraint((x,y) -> y.type() == ResourceType.EMPTY || x.type() == y.type());
        temp.addConstraint((x,y) -> x.type() == res.type());
        temp.addConstraint((x,y) -> (x.amount() + y.amount())<= 2);
        return temp;
    }

    /**
     * This method builds the Strongbox that doesn't have any constraint
     * @return the newly built Strongbox
     */
    public static Depot buildStrongBoxDepot(){
        return new Strongbox();
    }
}
