package it.polimi.ingsw.view.cli.printer;

import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.litemodel.LiteResource;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.MarbleColor;
import it.polimi.ingsw.view.cli.Colors;

import java.util.List;

/**
 * This class is the Printer of some properties
 */
public class CasualPrinter {

    /**
     * This attribute is an array of colors that will indicates different players
     */
    private static final String[] colors = {Colors.RED_BRIGHT, Colors.BLUE_BRIGHT, Colors.YELLOW_BRIGHT, Colors.GREEN_BRIGHT};


    /**
     * This method prints the Discounts of the Player
     * @param discounts is the List of resources of player's discounts
     */
    public static void printDiscounts(List<LiteResource> discounts){
        if(discounts.isEmpty()){
            System.out.println(Colors.color(Colors.RED,"You don't have any discount power!"));
            return;
        }
        System.out.println("Your discounted resources:");
        for(LiteResource resource : discounts) {
            System.out.println(Colors.color(resource.getType().toColor(), resource.toString()));
        }
    }

    /**
     * This method prints the MarbleConversion of the Player
     * @param conv is the list of Marble Colors of the Player's conversions
     */
    public static void printConversion(List<MarbleColor> conv){
        if(conv.isEmpty()){
            System.out.println(Colors.color(Colors.RED,"You don't have any conversion power!"));
            return;
        }
        System.out.println("Your conversion powers:");
        int i=0;
        for(MarbleColor color : conv){
            System.out.println(Colors.color(color.toColor(),i+": " + color.toString()));
            i++;
        }
    }

    /**
     * This method prints the list of players of the match
     * @param model is the model of the match
     */
    public static void printPlayers(LiteModel model) {
        int i=0;
        System.out.println("The player list is:");
        for(String name : model.getPlayers().keySet()){
            if(i!=0){
                System.out.println(",");
            }
            System.out.print(Colors.color(colors[i%4],name));
            i++;
        }
        System.out.println();
    }

    /*
    public static void main(String[] args) {
        LiteModel model = new LiteModel();

        model.createPlayer("Vinz");
        model.createPlayer("Cini");
        model.createPlayer("Lorenzo il Magnifico");
        model.createPlayer("LastBuddy");

        printPlayers(model);
        System.out.println("");

        List<MarbleColor> mb = new ArrayList<>();
        printConversion(mb);
        System.out.println("");
        mb.add(MarbleColor.YELLOW);
        mb.add(MarbleColor.GRAY);
        printConversion(mb);

        System.out.println("");

        List<LiteResource> lr = new ArrayList<>();
        printDiscounts(lr);
        System.out.println("");
        lr.add(new LiteResource(ResourceType.SHIELD,1));
        lr.add(new LiteResource(ResourceType.SERVANT,1));
        printDiscounts(lr);
    }

     */
}

