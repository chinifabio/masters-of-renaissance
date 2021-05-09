package it.polimi.ingsw.view.cli.printer;

import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.view.litemodel.LiteFaithTrack;
import it.polimi.ingsw.view.litemodel.LiteModel;

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
    private final LiteFaithTrack track;

    /**
     * This method is the constructor of the class
     */
    public FaithTrackPrinter(LiteModel model) {
        this.track = model.getTrack();
    }

    public void createTrack(LiteFaithTrack track, int players, String[][] faithTrack) {
        int number = 0;
        int riga = 0;
        int colonna = 0;
        String color = TextColors.RESET;

        while (colonna < (MAX_HORIZ_CELL * track.getSizeTrack())) {
            int col = colonna;
            if (track.isVaticanSpace(number)) {
                color = TextColors.YELLOW;
            }
            if (track.isPopeSpace(number)){
                color = TextColors.CYAN;
            }
            faithTrack[riga][col] = TextColors.colorText(color, "╔");
            for (int i = col + 1; i < col + (MAX_HORIZ_CELL - 1); i++) {
                if (i == (col + 2)) {
                    faithTrack[riga][i] = TextColors.colorText(color, String.valueOf(number));
                } else {
                    faithTrack[riga][i] = TextColors.colorText(color, "═");
                }
            }
            if (number > 9 && number < 100) {
                faithTrack[riga][col + (MAX_HORIZ_CELL - 2)] = TextColors.colorText(color, "╗");
                faithTrack[riga][col + (MAX_HORIZ_CELL - 1)] = "";
            } else if (number > 99){
                faithTrack[riga][col + (MAX_HORIZ_CELL - 3)] = TextColors.colorText(color, "╗");
                faithTrack[riga][col + (MAX_HORIZ_CELL - 2)] = "";
                faithTrack[riga][col + (MAX_HORIZ_CELL - 1)] = "";
            } else{
                faithTrack[riga][col + (MAX_HORIZ_CELL - 1)] = TextColors.colorText(color, "╗");
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

        colonna = colonna + MAX_HORIZ_CELL;
        number++;
        color = TextColors.RESET;
        }
        insertFaithPoints(track, faithTrack);
    }

    private void insertFaithPoints(LiteFaithTrack track, String[][] faithTrack){
        int row = 1;
        int col = 2;
        for (int i = 0; i < track.getSizeTrack(); i++){
            if (track.getVictoryPointCell(i) != 0){
                faithTrack[row][col] = TextColors.colorText(TextColors.PURPLE_BRIGHT,String.valueOf(track.getVictoryPointCell(i)));
            }
            if (track.getVictoryPointCell(i) > 9 && track.getVictoryPointCell(i) < 100){
                faithTrack[row][col+1] = "";
            } else if (track.getVictoryPointCell(i) >= 100){
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

    public void printTrack(List<String> nicknames, List<Integer> positions){

        String[][] faithTrack = new String[MAX_VERT+positions.size()][MAX_HORIZ_CELL*(this.track.getSizeTrack())];

        createTrack(this.track, positions.size(), faithTrack);
        insertPlayerPos(nicknames,positions, faithTrack);
        for (int r = 0; r < (MAX_VERT + positions.size()); r++){
            System.out.println();
            for (int c = 0; c < (MAX_HORIZ_CELL*this.track.getSizeTrack()); c++){
                System.out.print(faithTrack[r][c]);
            }
        }
        System.out.println();
    }

    public static void main(String[] args) throws IOException {

        LiteModel model = new LiteModel();
        LiteFaithTrack faith = new LiteFaithTrack();
        model.setTrack(faith);

        FaithTrackPrinter printer = new FaithTrackPrinter(model);
        Random numbers = new Random();
        List<String> names = new ArrayList<>();
        names.add("Vinz");
        names.add("Cini");
        names.add("LastBuddy");
        names.add("Test");

        List <Integer> position = new ArrayList<>();
        for (String name : names){
            position.add(numbers.nextInt(24));
        }

        printer.printTrack(names, position);

    }



}
