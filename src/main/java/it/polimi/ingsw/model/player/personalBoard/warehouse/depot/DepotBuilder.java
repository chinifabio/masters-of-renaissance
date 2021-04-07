package it.polimi.ingsw.model.player.personalBoard.warehouse.depot;

import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceType;

public class DepotBuilder {

    public static Depot buildBottomDepot(){
        Depot temp = new NormalDepot();
        temp.addConstraint((x,y) -> y.type() == ResourceType.EMPTY || x.type() == y.type());
        temp.addConstraint((x,y) -> (x.amount() + y.amount())<= 3);
        return temp;
    }

    public static Depot buildMiddleDepot(){
        Depot temp = new NormalDepot();
        temp.addConstraint((x,y) -> y.type() == ResourceType.EMPTY || x.type() == y.type());
        temp.addConstraint((x,y) -> (x.amount() + y.amount())<= 2);
        return temp;
    }

    public static Depot buildTopDepot(){
        Depot temp = new NormalDepot();
        temp.addConstraint((x,y) -> y.type() == ResourceType.EMPTY || x.type() == y.type());
        temp.addConstraint((x,y) -> (x.amount() + y.amount())<= 1);
        return temp;
    }

    public static Depot buildSpecialDepot(Resource res){
        Depot temp = new SpecialDepot(res);
        temp.addConstraint((x,y) -> y.type() == ResourceType.EMPTY || x.type() == y.type());
        temp.addConstraint((x,y) -> x.type() == res.type());
        temp.addConstraint((x,y) -> (x.amount() + y.amount())<= 2);
        return temp;
    }

    public static Depot buildStrongBoxDepot(){
        return new Strongbox();
    }
}
