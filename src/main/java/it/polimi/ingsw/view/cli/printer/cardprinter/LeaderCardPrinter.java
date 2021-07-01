package it.polimi.ingsw.view.cli.printer.cardprinter;
import it.polimi.ingsw.litemodel.litecards.LiteLeaderCard;
import it.polimi.ingsw.litemodel.litecards.literequirements.LiteRequisite;
import it.polimi.ingsw.view.cli.Colors;

/**
 * This class is the Printer of the LeaderCards
 */
public class LeaderCardPrinter {

    /**
     * This attribute is the height of the Leader Card
     */
    private static final int HEIGHT = 9; //rows.

    /**
     * This attribute is the width of the Leader Card
     */
    private static final int WIDTH = 12; //cols.

    /**
     * This method creates the LeaderCard to show in CLI mode
     * @param display is where the LeaderCard will be created
     * @param toPrint is the LeaderCard to show
     * @param x is the horizontal coordinate
     * @param y is the vertical coordinate
     * @param activated indicates if the LeaderCard is activated or not
     */
    public static void createLeaderCard(String[][] display, LiteLeaderCard toPrint, int x, int y, boolean activated){
        String color = Colors.CYAN;
        if(activated) color = Colors.GREEN_BRIGHT;
        
        display[x][y] = Colors.color(color,"╔");
        display[x + HEIGHT -1][y] = Colors.color(color,"╚");
        for (int i = y+1; i< y + WIDTH -1; i++){
            display[x][i] = Colors.color(color,"═");
            display[x + HEIGHT -1][i] = Colors.color(color,"═");
        }
        for (int i = y + 4; i < y + 8; i++){
            display[x][i] = "";
        }
        display[x][y + 4] = Colors.color(color, toPrint.getId());
        if (toPrint.getId().length() < 4) {
            display[x][y + 5] = Colors.color(color, "═");
        }

        display[x][y + WIDTH -1] = Colors.color(color,"╗");
        for (int r = x + 1; r < x + HEIGHT -1; r++){
            display[r][y] = Colors.color(color,"║");
            for (int c = y + 1; c < y + WIDTH -1; c++){
                display[r][c] = " ";
            }
            display[r][y + WIDTH - 1] = Colors.color(color,"║");
        }

        toPrint.getEffect().printEffect(display, x, y);
        display[x + HEIGHT - 2][y + WIDTH - 7] = Colors.color(Colors.PURPLE_BRIGHT,String.valueOf(toPrint.getVictoryPoints()));
        display[x + HEIGHT - 1][y + WIDTH - 1] = Colors.color(color,"╝");
        for (int i = y + 1; i < y + WIDTH - 1; i++) {
            display[x + 2][i] = Colors.color(color,"-");
            display[x + HEIGHT -3][i] = Colors.color(color,"-");
        }

        int z = y;
        for (LiteRequisite requisite : toPrint.getRequirements()) {
            requisite.printRequisite(display, x, z);
            z = z + 3;
        }
    }

}
