package it.polimi.ingsw.view.cli.printer.cardprinter;
import it.polimi.ingsw.litemodel.litecards.LiteDevCard;
import it.polimi.ingsw.litemodel.litecards.literequirements.LiteRequisite;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.view.cli.Colors;

/**
 * This class is the Printer of the Development Card
 */
public class DevCardPrinter {

    /**
     * This attribute is the height of the card
     */
    private static final int HEIGHT = 9; //rows.

    /**
     * This attribute is the width of the card
     */
    private static final int WIDTH = 12; //cols.

    /**
     * This metod create the devCard to show in the Display
     * @param display is where the devCard will be created
     * @param toPrint is the DevCard to create
     * @param x is the horizontal coordinate
     * @param y is the vertical coordinate
     */
    public static void createDevCard(String[][] display, LiteDevCard toPrint, int x, int y){
        boolean notEmpty = toPrint.getLevel() != LevelDevCard.NOLEVEL || toPrint.getColor() != ColorDevCard.NOCOLOR;
        String colorCard;
        colorCard = toPrint.getColor().getDevCardColor();

        display[x][y] = Colors.color(colorCard,"╔");
        display[x + HEIGHT -1][y] = Colors.color(colorCard,"╚");
        for (int i = y + 1; i< y + WIDTH -1; i++){
            display[x][i] = Colors.color(colorCard,"═");
            display[x + HEIGHT -1][i] = Colors.color(colorCard,"═");
        }
        for (int i = y + 4; i < y + 8; i++){
            display[x][i] = "";
        }
        display[x][y + 4] = Colors.color(colorCard,toPrint.getId());
        if (toPrint.getId().length() < 4) {
            display[x][y + 5] = Colors.color(colorCard,"═");
        } else if (toPrint.getId().length() > 4){
            display[x][y + 3] = "";
        }

        display[x][y + WIDTH -1] = Colors.color(colorCard,"╗");
        for (int r = x + 1; r < x + HEIGHT -1; r++){
            display[r][y] = (Colors.color(colorCard,"║"));
            for (int c = y + 1; c < y + WIDTH -1; c++){
                display[r][c] = " ";
            }
            display[r][y + WIDTH - 1] = Colors.color(colorCard,"║");
        }
        if (notEmpty) {
            display[x + HEIGHT - 2][y + WIDTH - 7] = Colors.color(Colors.PURPLE_BRIGHT, String.valueOf(toPrint.getVictoryPoint()));
            if (toPrint.getVictoryPoint() > 9) {
                display[x + HEIGHT - 2][y + WIDTH - 6] = "";
            }
        }
        display[x + HEIGHT -1][y + WIDTH -1] = Colors.color(colorCard,"╝");


        display[x + HEIGHT-1][y + 3] = Colors.color(colorCard,"LEVEL");
        if (notEmpty) {
            display[x + HEIGHT - 1][y + 4] = Colors.color(colorCard, String.valueOf(toPrint.getLevel().getLevelCard()));
        }
        for (int i = y + 5; i< y + WIDTH-3; i++){
            display[x + HEIGHT-1][i] = "";
        }

        if (notEmpty) {
            toPrint.getEffect().printEffect(display, x, y);
            for (int i = y + 1; i < y + WIDTH - 1; i++) {
                display[x + 2][i] = Colors.color(Colors.CYAN, "-");
                display[x + HEIGHT - 3][i] = Colors.color(Colors.CYAN, "-");
            }

            int z = y;
            for (LiteRequisite requisite : toPrint.getCost()) {
                requisite.printRequisite(display, x, z);
                z = z + 3;
            }
        }


    }


    /*
    public static void main(String[] args) throws IOException {
        DevCardPrinter printer = new DevCardPrinter();
        List<LiteDevCard> devCards = new ArrayList<>();
        List<List<LiteDevCard>> devCardsList = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            devCardsList = objectMapper.readValue(
                    DevCardPrinter.class.getResourceAsStream("/json/DevCards.json"),
                    new TypeReference<List<List<LiteDevCard>>>(){});
        }catch (IOException e) {
            e.printStackTrace();
        }
        for (List<LiteDevCard> list : devCardsList){
            devCards.addAll(list);
        }

        for (LiteDevCard card : devCards){
            printer.printDevCard(card);
        }

    }

     */

}
