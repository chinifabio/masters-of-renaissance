package it.polimi.ingsw.view.cli.printer.cardprinter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.litemodel.litecards.LiteLeaderCard;
import it.polimi.ingsw.model.cards.LeaderCard;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LeaderCardPrinter {

    private static final int HEIGHT = 7; //rows.
    private static final int WIDTH = 12; //cols.


    public LeaderCardPrinter() throws IOException {
    }

    public static void createLeaderCard(String[][] leaderCard, LiteLeaderCard toPrint){


        leaderCard[0][0] = TextColors.colorText(TextColors.CYAN,"╔");
        leaderCard[HEIGHT -1][0] = TextColors.colorText(TextColors.CYAN,"╚");
        for (int i = 1; i< WIDTH -1; i++){
            leaderCard[0][i] = TextColors.colorText(TextColors.CYAN,"═");
            leaderCard[HEIGHT -1][i] = TextColors.colorText(TextColors.CYAN,"═");
        }
        for (int i = 4; i <8; i++){
            leaderCard[0][i] = "";
        }
        leaderCard[0][4] = TextColors.colorText(TextColors.CYAN, toPrint.getId());
        if (toPrint.getId().length() < 4) {
            leaderCard[0][5] = TextColors.colorText(TextColors.CYAN, "═");
        }

        leaderCard[0][WIDTH -1] = TextColors.colorText(TextColors.CYAN,"╗");
        for (int r = 1; r < HEIGHT -1; r++){
           leaderCard[r][0] = TextColors.colorText(TextColors.CYAN,"║");
            for (int c = 1; c < WIDTH -1; c++){
                leaderCard[r][c] = " ";
            }
            leaderCard[r][WIDTH - 1] = TextColors.colorText(TextColors.CYAN,"║");
        }

        toPrint.getEffect().getPrinter().printEffect(leaderCard);
        leaderCard[HEIGHT -6][WIDTH -7] = TextColors.colorText(TextColors.PURPLE_BRIGHT,String.valueOf(toPrint.getVictoryPoints()));
        leaderCard[HEIGHT -1][WIDTH -1] = TextColors.colorText(TextColors.CYAN,"╝");
        for (int i = 1; i < leaderCard[3].length - 1; i++) {
            leaderCard[2][i] = TextColors.colorText(TextColors.CYAN,"-");
        }
    }

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

        toPrint.getEffect().getPrinter().printEffect(display, x, y);
        display[x + HEIGHT - 6][y + WIDTH - 7] = TextColors.colorText(TextColors.PURPLE_BRIGHT,String.valueOf(toPrint.getVictoryPoints()));
        display[x + HEIGHT - 1][y + WIDTH - 1] = TextColors.colorText(TextColors.CYAN,"╝");
        for (int i = y + 1; i < y + WIDTH - 1; i++) {
            display[x + 2][i] = TextColors.colorText(TextColors.CYAN,"-");
        }
    }

    public void printLeaderCard(LiteLeaderCard Leaderid){
        String[][] leaderCard = new String[HEIGHT][WIDTH];

        createLeaderCard(leaderCard, Leaderid);
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
