package it.polimi.ingsw.view.cli.printer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.player.personalBoard.faithTrack.VaticanSpace;
import it.polimi.ingsw.litemodel.litefaithtrack.LiteCell;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.view.cli.Colors;

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
    private final String[] colors = {Colors.RED_BRIGHT, Colors.BLUE_BRIGHT, Colors.YELLOW_BRIGHT, Colors.GREEN_BRIGHT};

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
    public FaithTrackPrinter() throws IOException {
        this.track = new ObjectMapper().readValue(
                getClass().getResourceAsStream("/json/FaithTrack.json"),
                new TypeReference<>() {
                });

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
        String color = Colors.RESET;

        while (column < (MAX_HORIZ_CELL * this.track.size())) {
            int col = column;
            if (this.isVaticanSpace(number)) {
                color = Colors.YELLOW;
            }
            if (this.isPopeSpace(number)){
                color = Colors.CYAN;
            }
            faithTrack[row][col] = Colors.color(color, "╔");
            for (int i = col + 1; i < col + (MAX_HORIZ_CELL - 1); i++) {
                if (i == (col + 2)) {
                    faithTrack[row][i] = Colors.color(color, String.valueOf(number));
                } else {
                    faithTrack[row][i] = Colors.color(color, "═");
                }
            }
            if (number > 9 && number < 100) {
                faithTrack[row][col + (MAX_HORIZ_CELL - 2)] = Colors.color(color, "╗");
                faithTrack[row][col + (MAX_HORIZ_CELL - 1)] = "";
            } else if (number > 99){
                faithTrack[row][col + (MAX_HORIZ_CELL - 3)] = Colors.color(color, "╗");
                faithTrack[row][col + (MAX_HORIZ_CELL - 2)] = "";
                faithTrack[row][col + (MAX_HORIZ_CELL - 1)] = "";
            } else{
                faithTrack[row][col + (MAX_HORIZ_CELL - 1)] = Colors.color(color, "╗");
            }

            for (int r = 1; r < (MAX_VERT+players); r++) {
                faithTrack[r][col] = Colors.color(color, "║");
                for (int c = col + 1; c < col + (MAX_HORIZ_CELL - 1); c++) {
                    faithTrack[r][c] = " ";
                }
                faithTrack[r][col + (MAX_HORIZ_CELL - 1)] = Colors.color(color, "║");
            }

            faithTrack[(MAX_VERT+players) - 1][col] = Colors.color(color, "╚");
            for (int c = col + 1; c < col + (MAX_HORIZ_CELL - 1); c++) {
                faithTrack[(MAX_VERT+players) - 1][c] = Colors.color(color, "═");
            }

            faithTrack[(MAX_VERT+players) - 1][col + (MAX_HORIZ_CELL - 1)] = Colors.color(color, "╝");

            for (int r = MAX_VERT+players; r < (MAX_VERT+players+3); r++) {
                faithTrack[r][col] = " ";
                for (int c = col + 1; c < col + (MAX_HORIZ_CELL - 1); c++) {
                    faithTrack[r][c] = " ";
                }
                faithTrack[r][col + (MAX_HORIZ_CELL - 1)] = " ";
            }

        column = column + MAX_HORIZ_CELL;
        number++;
        color = Colors.RESET;
        }
        insertFaithPoints(faithTrack);

        //Creating the PopeTiles
        int r = MAX_VERT+players;
        int col = 0;
        for (int i = 0; i < this.track.size(); i++){
            if (this.isPopeSpace(i)){
                faithTrack[r][col] = Colors.color(Colors.CYAN_BRIGHT,"╔");
                faithTrack[r+1][col] = Colors.color(Colors.CYAN_BRIGHT,"║");
                faithTrack[r+2][col] = Colors.color(Colors.CYAN_BRIGHT,"╚");
                for (int j = 1; j<5; j++){
                    faithTrack[r][col+j] = Colors.color(Colors.CYAN_BRIGHT,"═");
                    faithTrack[r+2][col+j] = Colors.color(Colors.CYAN_BRIGHT,"═");
                }
                int tilePoints = getPopeTilesPoint(getVaticanSpaceCell(i));
                if (tilePoints < 10) {
                    faithTrack[r + 1][col + 2] = Colors.color(Colors.PURPLE_BRIGHT, String.valueOf(tilePoints));
                } else {
                    faithTrack[r + 1][col + 2] = Colors.color(Colors.PURPLE_BRIGHT, String.valueOf(tilePoints));
                    faithTrack[r + 1][col + 3] = "";
                }

                faithTrack[r][col+5] = Colors.color(Colors.CYAN_BRIGHT,"╗");
                faithTrack[r+1][col+5] = Colors.color(Colors.CYAN_BRIGHT,"║");
                faithTrack[r+2][col+5] = Colors.color(Colors.CYAN_BRIGHT,"╝");
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
                faithTrack[row][col] = Colors.color(Colors.PURPLE_BRIGHT,String.valueOf(this.getVictoryPointCell(i)));
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
     //* @param nicknames is the List of players' nicknames
     * @param tiles is the List of popeTiles of each player
     * @return the String of the Legend that will be printed
     */
    private String createLegend(Map<String, HashMap<String, Boolean>> tiles){
        StringBuilder popeTiles = new StringBuilder();
        int i = 0;
        int lenght;

        for (String name : tiles.keySet()){
            lenght = 0;
            popeTiles.append(Colors.color(colors[i % 4], name)).append(": ");
            lenght = lenght + name.length();
            while (lenght < 20){
                popeTiles.append(" ");
                lenght++;
            }

            popeTiles.append(Colors.color(colors[i%4],"┼" + name.charAt(0))).append( " - PopeTiles: " );

            List<VaticanSpace> loop = new ArrayList<>(Arrays.asList(VaticanSpace.values()));
            loop.remove(VaticanSpace.NONE);
            for (VaticanSpace vs : loop){
                popeTiles.append(Colors.color(Colors.CYAN_BRIGHT,"["));
                if (tiles.get(name).get(vs.name())){
                    popeTiles.append(Colors.color(Colors.GREEN,"V"));
                } else {
                    popeTiles.append(" ");
                }
                popeTiles.append(Colors.color(Colors.CYAN_BRIGHT,"]"));
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

        String[] colors = {Colors.RED_BRIGHT, Colors.BLUE_BRIGHT, Colors.YELLOW_BRIGHT, Colors.GREEN_BRIGHT};
        int row = 2;
        int i = 0;
        int ins;
        System.out.println();
        for (Integer pos: positions){
            ins = 2+(pos*MAX_HORIZ_CELL);
            faithTrack[row][ins] = Colors.color(colors[i%4],"┼" + nicknames.get(i).charAt(0));
            faithTrack[row][ins+1] = "";
            row++;
            i++;
        }

    }

    /**
     * This method prints the complete FaithTrack with all the infos
     */
    public void printTrack(LiteModel model){
        Map<String, Integer> players = model.getPlayerPosition();
        Map<String, HashMap<String, Boolean>> popetiles = model.getPopeTilesPlayer();

        List<String> nicknames = new ArrayList<>(players.keySet());
        List<Integer> positions = new ArrayList<>(players.values());

        int vert = MAX_VERT+model.playersInGame()+3;
        String[][] faithTrack = new String[vert][MAX_HORIZ_CELL*(this.track.size())];

        System.out.println(createLegend(popetiles));
        createTrack(model.playersInGame(), faithTrack);
        insertPlayerPos(nicknames, positions, faithTrack);
        for (int r = 0; r < (vert); r++){
            System.out.println();
            for (int c = 0; c < (MAX_HORIZ_CELL*this.track.size()); c++){
                System.out.print(faithTrack[r][c]);
            }
        }
        System.out.println();
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
