package it.polimi.ingsw.model.resource;

import it.polimi.ingsw.litemodel.LiteResource;
import it.polimi.ingsw.model.resource.strategy.DoNothingBehavior;
import it.polimi.ingsw.model.resource.strategy.GiveFaithPointBehavior;
import it.polimi.ingsw.model.resource.strategy.LaunchExceptionBehavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * class that contains all the builders for all the resource types
 */
public class ResourceBuilder {

    /**
     * build a coin resource whit amount = 1
     * @return the resource created
     */
    public static Resource buildCoin() {
        return new Resource(true, ResourceType.COIN, new DoNothingBehavior());
    }

    /**
     * build a coin resource whit custom amount
     * @param amount amount of the resource
     * @return the resource created
     */
    public static Resource buildCoin(int amount) {
        return new Resource(true, ResourceType.COIN, new DoNothingBehavior(), amount);
    }

    /**
     * build a servant resource whit amount = 1
     * @return the resource created
     */
    public static Resource buildServant() {
        return new Resource(true, ResourceType.SERVANT, new DoNothingBehavior());
    }

    /**
     * build a servant resource whit custom amount
     * @param amount amount of the resource
     * @return the resource created
     */
    public static Resource buildServant(int amount) {
        return new Resource(true, ResourceType.SERVANT, new DoNothingBehavior(), amount);
    }

    /**
     * build a shield resource whit amount = 1
     * @return the resource created
     */
    public static Resource buildShield() {
        return new Resource(true, ResourceType.SHIELD, new DoNothingBehavior());
    }

    /**
     * build a shield resource whit custom amount
     * @param amount amount of the resource
     * @return the resource created
     */
    public static Resource buildShield(int amount) {
        return new Resource(true, ResourceType.SHIELD, new DoNothingBehavior(), amount);
    }

    /**
     * build a stone resource whit amount = 1
     * @return the resource created
     */
    public static Resource buildStone() {
        return new Resource(true, ResourceType.STONE, new DoNothingBehavior());
    }

    /**
     * build a stone resource whit custom amount
     * @param amount amount of the resource
     * @return the resource created
     */
    public static Resource buildStone(int amount) {
        return new Resource(true, ResourceType.STONE, new DoNothingBehavior(), amount);
    }

    /**
     * build a faithpoint resource whit amount = 1
     * @return the resource created
     */
    public static Resource buildFaithPoint() {
        return new Resource(false, ResourceType.FAITHPOINT, new GiveFaithPointBehavior(1));
    }

    /**
     * build a faithpoint resource whit custom amount
     * @param amount amount of the resource
     * @return the resource created
     */
    public static Resource buildFaithPoint(int amount) {
        return new Resource(false, ResourceType.FAITHPOINT, new GiveFaithPointBehavior(amount), amount);
    }

    /**
     * build an unknown resource whit amount = 1
     * @return the resource created
     */
    public static Resource buildUnknown() {
        return new Resource(false, ResourceType.UNKNOWN, new LaunchExceptionBehavior(), 1);
    }

    /**
     * build an empty resource whit amount = 0
     * @return the resource created
     */
    public static Resource buildEmpty() {
        return new Resource(false, ResourceType.EMPTY, new DoNothingBehavior(), 0);
    }

    /**
     * build a new resource starting from a passed resource type
     * @param type type of the new Resource
     * @param amount number of Resource to create
     * @return  the new Resource
     */
    public static Resource buildFromType(ResourceType type, int amount){
        // create each type of resource
        List<Resource> allTypes = new ArrayList<>();
        allTypes.add(buildStone());
        allTypes.add(buildServant());
        allTypes.add(buildShield());
        allTypes.add(buildCoin());
        allTypes.add(buildEmpty());
        allTypes.add(buildUnknown());
        allTypes.add(buildFaithPoint());


        return allTypes.stream().reduce(buildEmpty(), (r, res) -> {
            if(res.type() == type) return res.buildNewOne(amount);
            else return r;
        });
    }

    /**
     * This method return a list of resources with amount zero for each storable resource type
     * @return list of storable resource
     */
    public static List<Resource> buildListOfStorable() {
        List<Resource> result = new ArrayList<>();
        for(ResourceType type : ResourceType.values()) {
            Resource resource = ResourceBuilder.buildFromType(type, 0);
            if(resource.isStorable()) result.add(resource);
        }
        return result;
    }

    /**
     * build a list of all resources
     * @return the list of all resources
     */
    public static List<Resource> buildListOfResource() {
        List<Resource> result = new ArrayList<>();
        for(ResourceType type : ResourceType.values()) result.add(buildFromType(type, 0));
        return result;
    }

    /**
     * create a list of resources and merge all the resources of the raw list in input
     * @param raw raw list of resource to arrange
     * @return an arranged list of resource
     */
    public static List<Resource> rearrangeResourceList(List<Resource> raw) {
        List<Resource> arranged = ResourceBuilder.buildListOfResource();
        for(Resource res : raw) Objects.requireNonNull(arranged.stream().filter(x -> x.equalsType(res)).findAny().orElse(null)).merge(res);
        return arranged;
    }

    public static List<LiteResource> mapResource(List<Resource> in) {
        return in.stream().map(x -> new LiteResource(x.type(), x.amount())).collect(Collectors.toList());
    }
}
