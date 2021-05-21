package it.polimi.ingsw.view.cli.printer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.model.player.personalBoard.faithTrack.VaticanSpace;
import it.polimi.ingsw.litemodel.litefaithtrack.LiteCell;
import it.polimi.ingsw.litemodel.LiteModel;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * This class prints the FaithTrack
 */
public class FaithTrackPrinter {

    /**
     * This attribute is the initial vertical size of a cell
     */
    private static final int MAX_VERT = 3;

    /**
     * This attribute is the horizontal size of a cell
     */
    private static final int MAX_HORIZ_CELL = 6;

    /**
     * This attribute is an array of colors that will indicates different players
     */
    private final String[] colors = {TextColors.RED_BRIGHT, TextColors.BLUE_BRIGHT, TextColors.YELLOW_BRIGHT, TextColors.GREEN_BRIGHT};

    /**
     * This attribute is the FaithTrack to print
     */
    private final LiteModel model;

    /**
     * This track is the sample for all players
     */
    private final List<LiteCell> track;

    /**
     * This attribute indicates the victoryPoint of the PopeTiles
     */
    private final Map<VaticanSpace, Integer> popeTilesPoints;

    /**
     * This method is the constructor of the class
     */
    public FaithTrackPrinter(LiteModel model) throws IOException {
        this.model = model;
        ObjectMapper objectMapper = new ObjectMapper();

        this.track = objectMapper.readValue(
                new File("src/resources/FaithTrack.json"),
                new TypeReference<List<LiteCell>>(){});

        this.popeTilesPoints = new HashMap<>();
        popeTilesPoints.put(VaticanSpace.FIRST, 2);
        popeTilesPoints.put(VaticanSpace.SECOND,3);
        popeTilesPoints.put(VaticanSpace.THIRD, 4);

    }

    /**
     * This method create the FaithTrack to print
     * @param players is the attribute that indicates the number of players
     * @param faithTrack is the String[][] to print
     */
    private void createTrack(int players, String[][] faithTrack) {
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

            for (int r = MAX_VERT+players; r < (MAX_VERT+players+3); r++) {
                faithTrack[r][col] = " ";
                for (int c = col + 1; c < col + (MAX_HORIZ_CELL - 1); c++) {
                    faithTrack[r][c] = " ";
                }
                faithTrack[r][col + (MAX_HORIZ_CELL - 1)] = " ";
            }

        column = column + MAX_HORIZ_CELL;
        number++;
        color = TextColors.RESET;
        }
        insertFaithPoints(faithTrack);

        //Creating the PopeTiles
        int r = MAX_VERT+players;
        int col = 0;
        for (int i = 0; i < this.track.size(); i++){
            if (this.isPopeSpace(i)){
                faithTrack[r][col] = TextColors.colorText(TextColors.CYAN_BRIGHT,"╔");
                faithTrack[r+1][col] = TextColors.colorText(TextColors.CYAN_BRIGHT,"║");
                faithTrack[r+2][col] = TextColors.colorText(TextColors.CYAN_BRIGHT,"╚");
                for (int j = 1; j<5; j++){
                    faithTrack[r][col+j] = TextColors.colorText(TextColors.CYAN_BRIGHT,"═");
                    faithTrack[r+2][col+j] = TextColors.colorText(TextColors.CYAN_BRIGHT,"═");
                }
                int tilePoints = getPopeTilesPoint(getVaticanSpaceCell(i));
                if (tilePoints < 10) {
                    faithTrack[r + 1][col + 2] = TextColors.colorText(TextColors.PURPLE_BRIGHT, String.valueOf(tilePoints));
                } else {
                    faithTrack[r + 1][col + 2] = TextColors.colorText(TextColors.PURPLE_BRIGHT, String.valueOf(tilePoints));
                    faithTrack[r + 1][col + 3] = "";
                }

                faithTrack[r][col+5] = TextColors.colorText(TextColors.CYAN_BRIGHT,"╗");
                faithTrack[r+1][col+5] = TextColors.colorText(TextColors.CYAN_BRIGHT,"║");
                faithTrack[r+2][col+5] = TextColors.colorText(TextColors.CYAN_BRIGHT,"╝");
            }
            col = col + MAX_HORIZ_CELL;
        }


        insertFaithPoints(faithTrack);
    }

    /**
     * This method inserts the FaithPoints in the FaithTrack
     * @param faithTrack is the String[][] to update with the victoryPoints
     */
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

    /**
     * This attribute create the Legend that will appear before the FaithTrack, it indicates the marker and the
     * PopeTiles of the Players
     * @param nicknames is the List of players' nicknames
     * @param tiles is the List of popeTiles of each player
     * @return the String of the Legend that will be printed
     */
    private String createLegend(List<String> nicknames, List<Map<String, Boolean>> tiles){
        StringBuilder popeTiles = new StringBuilder();
        int i = 0;
        int lenght;

        for (String name : nicknames){
            lenght = 0;
            popeTiles.append(TextColors.colorText(colors[i % 4], name)).append(": ");
            lenght = lenght + name.length();
            while (lenght < 20){
                popeTiles.append(" ");
                lenght++;
            }

            popeTiles.append(TextColors.colorText(colors[i%4],"┼" + name.charAt(0))).append( " - PopeTiles: " );
            //for (Map.Entry<String, Boolean> entry : tiles.get(i).entrySet()){
            List<VaticanSpace> loop = new ArrayList<>(Arrays.asList(VaticanSpace.values()));
            loop.remove(VaticanSpace.NONE);
            for (VaticanSpace vs : loop){
                popeTiles.append(TextColors.colorText(TextColors.CYAN_BRIGHT,"["));
                if (tiles.get(i).get(vs.name())){
                    popeTiles.append(TextColors.colorText(TextColors.GREEN,"X"));
                } else {
                    popeTiles.append(" ");
                }
                popeTiles.append(TextColors.colorText(TextColors.CYAN_BRIGHT,"]"));
            }
            popeTiles.append("\n");
            i++;
        }

        return popeTiles.toString();
    }

    /**
     * This attribute inserts the Players' marker inside the FaithTrack
     * @param nicknames is the List of Players
     * @param positions is the List of position of each Player
     * @param faithTrack is the String[][] to update with the Player's markers
     */
    private void insertPlayerPos(List<String> nicknames, List<Integer> positions, String[][] faithTrack){

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

    /**
     * This method prints the complete FaithTrack with all the infos
     */
    public void printTrack(){
        Map<String, Integer> players = this.model.getPlayerPosition();
        Map<String, Map<String, Boolean>> popetiles = this.model.getPopeTilesPlayer();

        List<String> nicknames = new ArrayList<>(players.keySet());
        List<Integer> positions = new ArrayList<>(players.values());
        List<Map<String, Boolean>> popeTiles = new ArrayList<>(popetiles.values());


        int vert = MAX_VERT+this.model.playersInGame()+3;
        String[][] faithTrack = new String[vert][MAX_HORIZ_CELL*(this.track.size())];

        System.out.println(createLegend(nicknames, popeTiles));
        createTrack(this.model.playersInGame(), faithTrack);
        insertPlayerPos(nicknames,positions, faithTrack);
        for (int r = 0; r < (vert); r++){
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
        model.createPlayer("Lorenzo il Magnifico");
        model.createPlayer("LastBuddy");

        model.flipPopeTile("Vinz", "FIRST");
        model.flipPopeTile("Vinz", "SECOND");

        model.flipPopeTile("Cini", "THIRD");

        model.flipPopeTile("LastBuddy", "SECOND");
        model.flipPopeTile("LastBuddy", "THIRD");

        model.flipPopeTile("Lorenzo il Magnifico", "THIRD");


        FaithTrackPrinter printer = new FaithTrackPrinter(model);

        Random numbers = new Random();

        model.movePlayer("Vinz", numbers.nextInt(24));
        model.movePlayer("Cini", numbers.nextInt(24));
        model.movePlayer("LastBuddy", numbers.nextInt(24));
        model.movePlayer("Lorenzo il Magnifico", numbers.nextInt(24));

        printer.printTrack();

    }

    /**
     * This method indicates if the Cell is in a VaticanSpace
     * @param cell is the cell to check
     * @return true if the cell is in the VaticanSpace
     */
    private boolean isVaticanSpace(int cell){
        return track.get(cell).getVaticanSpace() != VaticanSpace.NONE;
    }

    /**
     * This method indicates if the Cell is a PopeSpace
     * @param cell is the cell to check
     * @return true if the cell is a PopeSpace
     */
    private boolean isPopeSpace(int cell){
        return track.get(cell).isPopeSpace().equals("PopeSpace");
    }

    /**
     * This method indicates the value of victoryPoint of the cell
     * @param cell is the cell that has the victoryPoint
     * @return the value of the victoryPoint of the cell
     */
    private int getVictoryPointCell(int cell){
        return this.track.get(cell).getVictoryPoints();
    }

    /**
     * This method indicates in which VaticanSpace the cell is located
     * @param cell is the cell to check
     * @return the VaticanSpace of the cell
     */
    private VaticanSpace getVaticanSpaceCell(int cell){
        return this.track.get(cell).getVaticanSpace();
    }

    /**
     * This method indicates the value of victoryPoint of the PopeTile of the VaticanSpace
     * @param vaticanSpace is the VaticanSpace of the PopeTile
     * @return the value of victoryPoint of the PopeTile
     */
    private int getPopeTilesPoint(VaticanSpace vaticanSpace){
        return this.popeTilesPoints.get(vaticanSpace);
    }



}
