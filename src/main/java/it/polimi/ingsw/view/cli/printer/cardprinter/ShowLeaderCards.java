package it.polimi.ingsw.view.cli.printer.cardprinter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.litemodel.litecards.LiteLeaderCard;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShowLeaderCards {

    private static final int HEIGHT = 11; //rows.
    private static final int WIDTH = 60; //cols.

    public static void printLeaderCardsPlayer(List<LiteLeaderCard> toShow) throws IOException {
        String[][] leaderCards = new String[HEIGHT][WIDTH];
        for (int i = 0; i< HEIGHT-1; i++){
            for (int j = 0; j<WIDTH-1; j++){
                leaderCards[i][j] = " ";
            }
        }
        int index = 0;
        for (LiteLeaderCard card : toShow) {
            LeaderCardPrinter.createLeaderCard(leaderCards, card, 0, index);
            index = index + 14;
        }

        for (int i = 0; i< HEIGHT-1; i++){
            System.out.println();
            for (int j = 0; j<WIDTH-1; j++){
                System.out.print(leaderCards[i][j]);
            }
        }
        System.out.println();
    }

    public static void createLeaderCardsSlot(String[][] display, List<LiteLeaderCard> toShow, int x, int y){
        int index = y;
        for (LiteLeaderCard card : toShow) {
            LeaderCardPrinter.createLeaderCard(display, card, x, index);
            index = index + 14;
        }
    }

    public static void main(String[] args) throws IOException {
        List<LiteLeaderCard> names = new ArrayList<>();
        List<LiteLeaderCard> leaderCards;
        ObjectMapper objectMapper = new ObjectMapper();
        Random rd = new Random();

        leaderCards = objectMapper.readValue(
                new File("src/resources/LeaderCards.json"),
                new TypeReference<List<LiteLeaderCard>>(){});

        names.add(leaderCards.get(rd.nextInt(16)));
        names.add(leaderCards.get(rd.nextInt(16)));
        names.add(leaderCards.get(rd.nextInt(16)));
        names.add(leaderCards.get(rd.nextInt(16)));

        printLeaderCardsPlayer(names);
    }



}
