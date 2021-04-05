package it.polimi.ingsw.model.resource.builder;

/**
 * class that contains all the builders for all the resource types
 */
public class ResourceDirector {
    /**
     * coin builder
     */
    private static CoinBuilder coin = new CoinBuilder();
    /**
     * servant builder
     */
    private static ServantBuilder servant = new ServantBuilder();
    /**
     * shield builder
     */
    private static ShieldBuilder shield = new ShieldBuilder();
    /**
     * stone builder
     */
    private static StoneBuilder stone = new StoneBuilder();
    /**
     * faithpoint builder
     */
    private static FaithPointBuilder faithPoint = new FaithPointBuilder();
    /**
     * unknown builder
     */
    private static UnknownBuilder unknown = new UnknownBuilder();

    /**
     * build a coin resource whit amount = 1
     * @return the resource created
     */
    public static Resource buildCoin() {
        return coin.build();
    }

    /**
     * build a coin resource whit custom amount
     * @param a amount of the resource
     * @return the resource created
     */
    public static Resource buildCoin(int a) {
        return coin.build(a);
    }

    /**
     * build a servant resource whit amount = 1
     * @return the resource created
     */
    public static Resource buildServant() {
        return  servant.build();
    }

    /**
     * build a servant resource whit custom amount
     * @param a amount of the resource
     * @return the resource created
     */
    public static Resource buildServant(int a) {
        return  servant.build(a);
    }

    /**
     * build a shield resource whit amount = 1
     * @return the resource created
     */
    public static Resource buildShield() {
        return  shield.build();
    }

    /**
     * build a shield resource whit custom amount
     * @param a amount of the resource
     * @return the resource created
     */
    public static Resource buildShield(int a) {
        return  shield.build(a);
    }

    /**
     * build a stone resource whit amount = 1
     * @return the resource created
     */
    public static Resource buildStone() {
        return stone.build();
    }

    /**
     * build a stone resource whit custom amount
     * @param a amount of the resource
     * @return the resource created
     */
    public static Resource buildStone(int a) {
        return stone.build(a);
    }

    /**
     * build a faithpoint resource whit amount = 1
     * @return the resource created
     */
    public static Resource buildFaithPoint() {
        return faithPoint.build();
    }

    /**
     * build a faithpoint resource whit custom amount
     * @param a amount of the resource
     * @return the resource created
     */
    public static Resource buildFaithPoint(int a) {
        return faithPoint.build(a);
    }

    /**
     * build a unknown resource whit amount = 1
     * @return the resource created
     */
    public static Resource buildUnknown() {
        return unknown.build();
    }

    /**
     * build a unknown resource whit custom amount
     * @param a amount of the resource
     * @return the resource created
     */
    public static Resource buildUnknown(int a) {
        return unknown.build(a);
    }
}
