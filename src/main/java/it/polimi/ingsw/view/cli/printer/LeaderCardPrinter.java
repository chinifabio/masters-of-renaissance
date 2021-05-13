package it.polimi.ingsw.view.cli.printer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.litemodel.litecards.LiteLeaderCard;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LeaderCardPrinter {

    private static final int MAX_VERT = 7; //rows.
    private static final int MAX_HORIZ = 12; //cols.

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

        leaderCard[0][0] = "╔";
        leaderCard[MAX_VERT-1][0] = "╚";
        for (int i = 1; i< MAX_HORIZ -1; i++){
            leaderCard[0][i] = "═";
            leaderCard[MAX_VERT-1][i] = "═";
        }
        for (int i = 4; i <8; i++){
            leaderCard[0][i] = "";
        }
        if (toPrint.getId().length() < 4) {
            leaderCard[0][4] = toPrint.getId();
            leaderCard[0][5] = "═";
        } else{
            leaderCard[0][4] = toPrint.getId();
        }

        leaderCard[0][MAX_HORIZ-1] = "╗";
        for (int r = 1; r < MAX_VERT-1; r++){
           leaderCard[r][0] = ("║");
            for (int c = 1; c < MAX_HORIZ -1; c++){
                leaderCard[r][c] = " ";
            }
            leaderCard[r][MAX_HORIZ - 1] = "║";
        }

        toPrint.getEffect().getPrinter().printEffect(leaderCard);
        leaderCard[MAX_VERT-5][MAX_HORIZ-7] = String.valueOf(toPrint.getVictoryPoints());
        leaderCard[MAX_VERT-1][MAX_HORIZ-1] = "╝";
    }

    public void printLeaderCard(String Leaderid){
        String[][] leaderCard = new String[MAX_VERT][MAX_HORIZ];

        createLeaderCard(leaderCard, Leaderid);
        for (int r = 0; r < (MAX_VERT); r++) {
            System.out.println();
            for (int c = 0; c < (MAX_HORIZ); c++) {
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
