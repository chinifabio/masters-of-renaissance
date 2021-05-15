package it.polimi.ingsw.view.cli.printer.cardprinter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.litemodel.litecards.LiteLeaderCard;
import it.polimi.ingsw.litemodel.litecards.literequirements.LiteRequisite;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LeaderCardPrinter {

    private static final int HEIGHT = 9; //rows.
    private static final int WIDTH = 12; //cols.


    public static void createLeaderCard(String[][] display, LiteLeaderCard toPrint, int x, int y){
        display[x][y] = TextColors.colorText(TextColors.CYAN,"╔");
        display[x + HEIGHT -1][y] = TextColors.colorText(TextColors.CYAN,"╚");
        for (int i = y+1; i< y + WIDTH -1; i++){
            display[x][i] = TextColors.colorText(TextColors.CYAN,"═");
            display[x + HEIGHT -1][i] = TextColors.colorText(TextColors.CYAN,"═");
        }
        for (int i = y + 4; i < y + 8; i++){
            display[x][i] = "";
        }
        display[x][y + 4] = TextColors.colorText(TextColors.CYAN, toPrint.getId());
        if (toPrint.getId().length() < 4) {
            display[x][y + 5] = TextColors.colorText(TextColors.CYAN, "═");
        }

        display[x][y + WIDTH -1] = TextColors.colorText(TextColors.CYAN,"╗");
        for (int r = x + 1; r < x + HEIGHT -1; r++){
            display[r][y] = TextColors.colorText(TextColors.CYAN,"║");
            for (int c = y + 1; c < y + WIDTH -1; c++){
                display[r][c] = " ";
            }
            display[r][y + WIDTH - 1] = TextColors.colorText(TextColors.CYAN,"║");
        }

        toPrint.getEffect().printEffect(display, x, y);
        display[x + HEIGHT - 2][y + WIDTH - 7] = TextColors.colorText(TextColors.PURPLE_BRIGHT,String.valueOf(toPrint.getVictoryPoints()));
        display[x + HEIGHT - 1][y + WIDTH - 1] = TextColors.colorText(TextColors.CYAN,"╝");
        for (int i = y + 1; i < y + WIDTH - 1; i++) {
            display[x + 2][i] = TextColors.colorText(TextColors.CYAN,"-");
            display[x + HEIGHT -3][i] = TextColors.colorText(TextColors.CYAN,"-");
        }

        int z = y;
        for (LiteRequisite requisite : toPrint.getRequirements()) {
            requisite.printRequisite(display, 0, z);
            z = z + 3;
        }
    }

    public void printLeaderCard(LiteLeaderCard leaderToPrint){
        String[][] leaderCard = new String[HEIGHT][WIDTH];

        createLeaderCard(leaderCard, leaderToPrint, 0,0);
        for (int r = 0; r < (HEIGHT); r++) {
            System.out.println();
            for (int c = 0; c < (WIDTH); c++) {
                System.out.print(leaderCard[r][c]);
            }
        }

    }

    public static void main(String[] args) throws IOException {
        LeaderCardPrinter printer = new LeaderCardPrinter();
        List<LiteLeaderCard> leaderCards;

        ObjectMapper objectMapper = new ObjectMapper();

        leaderCards = objectMapper.readValue(
                new File("src/resources/LeaderCards.json"),
                new TypeReference<List<LiteLeaderCard>>(){});

        for (LiteLeaderCard card: leaderCards){
            printer.printLeaderCard(card);
        }

    }

}
