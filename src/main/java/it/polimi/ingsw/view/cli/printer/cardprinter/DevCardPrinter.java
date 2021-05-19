package it.polimi.ingsw.view.cli.printer.cardprinter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.litemodel.litecards.LiteDevCard;
import it.polimi.ingsw.litemodel.litecards.literequirements.LiteRequisite;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DevCardPrinter {
    private static final int HEIGHT = 9; //rows.
    private static final int WIDTH = 12; //cols.

    public static void createDevCard(String[][] display, LiteDevCard toPrint, int x, int y){
        boolean notEmpty = toPrint.getLevel() != LevelDevCard.NOLEVEL || toPrint.getColor() != ColorDevCard.NOCOLOR;
        String colorCard;
        colorCard = toPrint.getColor().getDevCardColor();

        display[x][y] = TextColors.colorText(colorCard,"╔");
        display[x + HEIGHT -1][y] = TextColors.colorText(colorCard,"╚");
        for (int i = y + 1; i< y + WIDTH -1; i++){
            display[x][i] = TextColors.colorText(colorCard,"═");
            display[x + HEIGHT -1][i] = TextColors.colorText(colorCard,"═");
        }
        for (int i = y + 4; i < y + 8; i++){
            display[x][i] = "";
        }
        display[x][y + 4] = TextColors.colorText(colorCard,toPrint.getId());
        if (toPrint.getId().length() < 4) {
            display[x][y + 5] = TextColors.colorText(colorCard,"═");
        } else if (toPrint.getId().length() > 4){
            display[x][y + 3] = "";
        }

        display[x][y + WIDTH -1] = TextColors.colorText(colorCard,"╗");
        for (int r = x + 1; r < x + HEIGHT -1; r++){
            display[r][y] = (TextColors.colorText(colorCard,"║"));
            for (int c = y + 1; c < y + WIDTH -1; c++){
                display[r][c] = " ";
            }
            display[r][y + WIDTH - 1] = TextColors.colorText(colorCard,"║");
        }
        if (notEmpty) {
            display[x + HEIGHT - 2][y + WIDTH - 7] = TextColors.colorText(TextColors.PURPLE_BRIGHT, String.valueOf(toPrint.getVictoryPoint()));
            if (toPrint.getVictoryPoint() > 9) {
                display[x + HEIGHT - 2][y + WIDTH - 6] = "";
            }
        }
        display[x + HEIGHT -1][y + WIDTH -1] = TextColors.colorText(colorCard,"╝");


        display[x + HEIGHT-1][y + 3] = TextColors.colorText(colorCard,"LEVEL");
        if (notEmpty) {
            display[x + HEIGHT - 1][y + 4] = TextColors.colorText(colorCard, String.valueOf(toPrint.getLevel().getLevelCard()));
        }
        for (int i = y + 5; i< y + WIDTH-3; i++){
            display[x + HEIGHT-1][i] = "";
        }

        if (notEmpty) {
            toPrint.getEffect().printEffect(display, x, y);
            for (int i = y + 1; i < y + WIDTH - 1; i++) {
                display[x + 2][i] = TextColors.colorText(TextColors.CYAN, "-");
                display[x + HEIGHT - 3][i] = TextColors.colorText(TextColors.CYAN, "-");
            }

            int z = y;
            for (LiteRequisite requisite : toPrint.getCost()) {
                requisite.printRequisite(display, x, z);
                z = z + 3;
            }
        }


    }


    public void printDevCard(LiteDevCard devCardToPrint){
        String[][] devCard = new String[HEIGHT][WIDTH];

        createDevCard(devCard, devCardToPrint, 0, 0);
        for (int r = 0; r < (HEIGHT); r++) {
            System.out.println();
            for (int c = 0; c < (WIDTH); c++) {
                System.out.print(devCard[r][c]);
            }
        }

    }

    public static void main(String[] args) throws IOException {
        DevCardPrinter printer = new DevCardPrinter();
        List<LiteDevCard> devCards = new ArrayList<>();
        List<List<LiteDevCard>> devCardsList = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            devCardsList = objectMapper.readValue(
                    new File("src/resources/DevCards.json"),
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

}
