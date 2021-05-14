package it.polimi.ingsw.view.cli.printer.cardprinter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.litemodel.litecards.LiteLeaderCard;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LeaderCardPrinter {

    private static final int HEIGHT = 7; //rows.
    private static final int WIDTH = 12; //cols.

    private final List<LiteLeaderCard> leaderCards;

    public LeaderCardPrinter() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        this.leaderCards = objectMapper.readValue(
                new File("src/resources/LeaderCards.json"),
                new TypeReference<List<LiteLeaderCard>>(){});
    }

    public void createLeaderCard(String[][] leaderCard, String ID){
        LiteLeaderCard toPrint = null;
        for (LiteLeaderCard card: leaderCards){
            if (card.getId().equals(ID)){
                toPrint = card;
            }
        }
        assert toPrint != null;

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

    public void printLeaderCard(String Leaderid){
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

        for (int i = 1; i < 17; i++) {
            printer.printLeaderCard("LC" + i);
        }

    }

}
