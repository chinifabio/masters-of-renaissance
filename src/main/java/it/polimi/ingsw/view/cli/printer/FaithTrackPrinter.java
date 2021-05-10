package it.polimi.ingsw.view.cli.printer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.model.player.personalBoard.faithTrack.VaticanSpace;
import it.polimi.ingsw.view.litemodel.LiteCell;
import it.polimi.ingsw.view.litemodel.LiteFaithTrack;
import it.polimi.ingsw.view.litemodel.LiteModel;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * This class prints the FaithTrack
 */
public class FaithTrackPrinter {

    private static final int MAX_VERT = 3; //rows.
    private static final int MAX_HORIZ_CELL = 6; //cols.

    /**
     * This attribute is the FaithTrack to print
     */
    private final LiteModel model;

    /**
     * This track is the sample for all players
     */
    private final List<LiteCell> track;

    /**
     * This method is the constructor of the class
     */
    public FaithTrackPrinter(LiteModel model) throws IOException {
        this.model = model;
        ObjectMapper objectMapper = new ObjectMapper();

        this.track = objectMapper.readValue(
                new File("src/resources/FaithTrack.json"),
                new TypeReference<List<LiteCell>>(){});
    }

    public void createTrack(int players, String[][] faithTrack) {
        int number = 0;
        int row = 0;
        int column = 0;
        String color = TextColors.RESET;

        while (column < (MAX_HORIZ_CELL * this.track.size())) {
            int col = column;
            if (this.isVaticanSpace(number)) {
                color = TextColors.YELLOW;
            }
            if (this.isPopeSpace(number)){
                color = TextColors.CYAN;
            }
            faithTrack[row][col] = TextColors.colorText(color, "╔");
            for (int i = col + 1; i < col + (MAX_HORIZ_CELL - 1); i++) {
                if (i == (col + 2)) {
                    faithTrack[row][i] = TextColors.colorText(color, String.valueOf(number));
                } else {
                    faithTrack[row][i] = TextColors.colorText(color, "═");
                }
            }
            if (number > 9 && number < 100) {
                faithTrack[row][col + (MAX_HORIZ_CELL - 2)] = TextColors.colorText(color, "╗");
                faithTrack[row][col + (MAX_HORIZ_CELL - 1)] = "";
            } else if (number > 99){
                faithTrack[row][col + (MAX_HORIZ_CELL - 3)] = TextColors.colorText(color, "╗");
                faithTrack[row][col + (MAX_HORIZ_CELL - 2)] = "";
                faithTrack[row][col + (MAX_HORIZ_CELL - 1)] = "";
            } else{
                faithTrack[row][col + (MAX_HORIZ_CELL - 1)] = TextColors.colorText(color, "╗");
            }

            for (int r = 1; r < (MAX_VERT+players); r++) {
                faithTrack[r][col] = TextColors.colorText(color, "║");
                for (int c = col + 1; c < col + (MAX_HORIZ_CELL - 1); c++) {
                    faithTrack[r][c] = " ";
                }
                faithTrack[r][col + (MAX_HORIZ_CELL - 1)] = TextColors.colorText(color, "║");
            }

            faithTrack[(MAX_VERT+players) - 1][col] = TextColors.colorText(color, "╚");
            for (int c = col + 1; c < col + (MAX_HORIZ_CELL - 1); c++) {
                faithTrack[(MAX_VERT+players) - 1][c] = TextColors.colorText(color, "═");
            }

            faithTrack[(MAX_VERT+players) - 1][col + (MAX_HORIZ_CELL - 1)] = TextColors.colorText(color, "╝");

        column = column + MAX_HORIZ_CELL;
        number++;
        color = TextColors.RESET;
        }
        insertFaithPoints(faithTrack);
    }

    private void insertFaithPoints(String[][] faithTrack){
        int row = 1;
        int col = 2;
        for (int i = 0; i < this.track.size(); i++){
            if (this.getVictoryPointCell(i) != 0){
                faithTrack[row][col] = TextColors.colorText(TextColors.PURPLE_BRIGHT,String.valueOf(this.getVictoryPointCell(i)));
            }
            if (this.getVictoryPointCell(i) > 9 && this.getVictoryPointCell(i) < 100){
                faithTrack[row][col+1] = "";
            } else if (this.getVictoryPointCell(i) >= 100){
                faithTrack[row][col+1] = "";
                faithTrack[row][col+2] = "";
            }
            col = col + MAX_HORIZ_CELL;
        }
    }

    private String createLegend(List<String> nicknames){
        StringBuilder legend = new StringBuilder();
        String[] colors = {TextColors.RED_BRIGHT, TextColors.BLUE_BRIGHT, TextColors.YELLOW_BRIGHT, TextColors.GREEN_BRIGHT};
        int i = 0;

        for (String name : nicknames){
            legend.append(TextColors.colorText(colors[i%4], name)).append(": ").append(TextColors.colorText(colors[i%4],"┼" + name.charAt(0))).append("\n");
            i++;
        }
        return legend.toString();
    }

    private void insertPlayerPos(List<String> nicknames, List<Integer> positions, String[][] faithTrack){
        System.out.println(createLegend(nicknames));
        String[] colors = {TextColors.RED_BRIGHT, TextColors.BLUE_BRIGHT, TextColors.YELLOW_BRIGHT, TextColors.GREEN_BRIGHT};
        int row = 2;
        int i = 0;
        int ins;
        System.out.println();
        for (Integer pos: positions){
            ins = 2+(pos*MAX_HORIZ_CELL);
            faithTrack[row][ins] = TextColors.colorText(colors[i%4],"┼" + nicknames.get(i).charAt(0));
            faithTrack[row][ins+1] = "";
            row++;
            i++;
        }

    }

    public void printTrack(){
        Map<String, Integer> map = this.model.getPlayerPosition();
        List<String> nicknames = new ArrayList<>(map.keySet());
        List<Integer> positions = new ArrayList<>(map.values());

        String[][] faithTrack = new String[MAX_VERT+this.model.playersInGame()][MAX_HORIZ_CELL*(this.track.size())];

        createTrack(this.model.playersInGame(), faithTrack);
        insertPlayerPos(nicknames, positions, faithTrack);
        for (int r = 0; r < (MAX_VERT + this.model.playersInGame()); r++){
            System.out.println();
            for (int c = 0; c < (MAX_HORIZ_CELL*this.track.size()); c++){
                System.out.print(faithTrack[r][c]);
            }
        }
        System.out.println();
    }

    public static void main(String[] args) throws IOException {

        LiteModel model = new LiteModel();
        model.createPlayer("Vinz");
        model.createPlayer("Cini");
        model.createPlayer("LastBuddy");
        model.createPlayer("Dummy");

        FaithTrackPrinter printer = new FaithTrackPrinter(model);

        Random numbers = new Random();

        model.movePlayer("Vinz", numbers.nextInt(24));
        model.movePlayer("Cini", numbers.nextInt(24));
        model.movePlayer("LastBuddy", numbers.nextInt(24));
        model.movePlayer("Dummy", numbers.nextInt(24));

        printer.printTrack();

    }

    private boolean isVaticanSpace(int cell){
        return track.get(cell).getVaticanSpace() != VaticanSpace.NONE;
    }

    private boolean isPopeSpace(int cell){
        return track.get(cell).isPopeSpace().equals("PopeSpace");
    }

    private int getVictoryPointCell(int cell){
        return this.track.get(cell).getVictoryPoints();
    }


}
