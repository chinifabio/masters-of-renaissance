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

    //Max player's name char: 20
    /**
     * This method prints the FaithTrack with the list of Player and their position inside the track
     * @param nicknames is the nickname of the players
     * @param positions is the value of the position of each player
     */
    public void printFaithTrack(List<String> nicknames, List<Integer> positions){
        System.out.println();
        printNormalTrack();
        printPlayerPos(nicknames, positions);
    }

    /**
     * This method prints the Top and Middle lines of the Track
     */
    private void printNormalTrack() {

        System.out.print("                      ");
        for (int i = 0; i < track.getSizeTrack(); i++) {
            printTopCell(i, track);
        }
        System.out.println();
        System.out.print("                      ");
        for (int i = 0; i < track.getSizeTrack(); i++) {
            printMiddleCell(track,i);
        }

    }

    /**
     * This method prints the middle part of the Track that has the FaithMarkers of each player
     * @param nicknames is the List of nicknames of the Players
     * @param positions is the List of positions of the Players
     */
    private void printPlayerPos(List<String> nicknames, List<Integer> positions) {
        String[] colors = {TextColors.RED_BRIGHT, TextColors.BLUE_BRIGHT, TextColors.YELLOW_BRIGHT, TextColors.GREEN_BRIGHT};
        System.out.println();

        int j = 0;

        for (String nickname : nicknames){

            StringBuilder temp = new StringBuilder();
            temp.append(TextColors.colorText(colors[j], nickname + ": "));
            while (temp.length() < 33){
                temp.append(" ");
            }

            for (int i = 0; i< track.getSizeTrack(); i++){
                if (track.isVaticanSpace(i)) {
                    if (track.isPopeSpace(i)){
                        if (i == positions.get(j)){
                            temp.append(TextColors.colorText(TextColors.CYAN, "║ "));
                            temp.append(TextColors.colorText(colors[j],"┼" + nickname.charAt(0) + " "));
                            temp.append(TextColors.colorText(TextColors.CYAN, "║"));
                        } else {
                            temp.append(TextColors.colorText(TextColors.CYAN, "║  " + "  ║"));
                        }
                    } else {
                        if (i == positions.get(j)) {
                            temp.append(TextColors.colorText(TextColors.YELLOW, "║ "));
                            temp.append(TextColors.colorText(colors[j], "┼" + nickname.charAt(0) + " "));
                            temp.append(TextColors.colorText(TextColors.YELLOW, "║"));
                        } else {
                            temp.append(TextColors.colorText(TextColors.YELLOW, "║  " + "  ║"));
                        }
                    }
                } else {
                    if (i == positions.get(j)){
                        temp.append( "║ ");
                        temp.append(TextColors.colorText(colors[j],"┼" + nickname.charAt(0) + " "));
                        temp.append("║");
                    } else {
                        temp.append("║    ║");
                    }
                }
            }

            System.out.println(temp);
            j++;
        }
        System.out.print("                      ");
        for (int i = 0; i < track.getSizeTrack(); i++) {
            printBottomCell(track, i);
        }
    }

    /**
     * This method prints the Top part of the cell
     * @param number is the value of the cell that indicates its position in the track
     * @param track is the track that contains the cell
     */
    private void printTopCell(int number, LiteFaithTrack track) {
        if (number <= 9) {
            if (track.isVaticanSpace(number)) {
                if (track.isPopeSpace(number)){
                    System.out.print(TextColors.colorText(TextColors.CYAN, "╔══" + (number) + "═╗"));
                } else{
                System.out.print(TextColors.colorText(TextColors.YELLOW, "╔══" + (number) + "═╗"));
                }
            } else {
                System.out.print("╔══" + (number) + "═╗");
            }
        } else {
            if (track.isVaticanSpace(number)) {
                if (track.isPopeSpace(number)){
                    System.out.print(TextColors.colorText(TextColors.CYAN, "╔═" + (number) + "═╗"));
                } else {
                System.out.print(TextColors.colorText(TextColors.YELLOW, "╔═" + (number) + "═╗"));}
            } else {
                System.out.print("╔═" + (number) + "═╗");
            }
        }
    }

    /**
     * This method prints the Middle part of the cell
     * @param track is the track that contains the cell
     * @param number is the index of the cell
     */
    private void printMiddleCell(LiteFaithTrack track, int number) {
        if (track.getVictoryPointCell(number) == 0) {
            if (track.isVaticanSpace(number)) {
                if (track.isPopeSpace(number)){
                    System.out.print(TextColors.colorText(TextColors.CYAN, "║  " + "  ║"));
                } else {
                System.out.print(TextColors.colorText(TextColors.YELLOW, "║  " + "  ║"));}
            } else {
                System.out.print("║  " + "  ║");
            }
        } else if (track.getVictoryPointCell(number) <= 9) {
            checkVaticanSpacePrint(track, number);
        } else {
            if (number <= 9) {
                checkVaticanSpacePrint(track, number);
            } else {
                if (track.isVaticanSpace(number)) {
                    if (track.isPopeSpace(number)){
                        System.out.print(TextColors.colorText(TextColors.CYAN, "║ ") + TextColors.colorText(TextColors.PURPLE_BRIGHT,String.valueOf(track.getVictoryPointCell(number))) + TextColors.colorText(TextColors.CYAN," ║"));
                }else{
                    System.out.print(TextColors.colorText(TextColors.YELLOW, "║ ") + TextColors.colorText(TextColors.PURPLE_BRIGHT,String.valueOf(track.getVictoryPointCell(number))) + TextColors.colorText(TextColors.YELLOW," ║"));}
                } else {
                    System.out.print("║ " + TextColors.colorText(TextColors.PURPLE_BRIGHT,String.valueOf(track.getVictoryPointCell(number))) + " ║") ;
                }
            }
        }
    }

    /**
     * This method checks if the cell is a VaticanSpace so change its color to YELLOW
     * @param track is the track that contains the cell
     * @param number is the index of the cell
     */
    private void checkVaticanSpacePrint(LiteFaithTrack track, int number) {
        if (track.isVaticanSpace(number)) {
            if (track.isPopeSpace(number)){
                System.out.print(TextColors.colorText(TextColors.CYAN, "║  ") + TextColors.colorText(TextColors.PURPLE_BRIGHT,String.valueOf(track.getVictoryPointCell(number))) + TextColors.colorText(TextColors.CYAN," ║"));
        }else{
            System.out.print(TextColors.colorText(TextColors.YELLOW, "║  ") + TextColors.colorText(TextColors.PURPLE_BRIGHT,String.valueOf(track.getVictoryPointCell(number))) + TextColors.colorText(TextColors.YELLOW," ║"));}
        } else {
            System.out.print("║  " + TextColors.colorText(TextColors.PURPLE_BRIGHT,String.valueOf(track.getVictoryPointCell(number))) + " ║");
        }
    }

    /**
     * This method prints the Bottom part of the cell
     * @param track is the track that contains the cell
     * @param i is the index of the cell
     */
    private void printBottomCell(LiteFaithTrack track, int i){
        if (track.isVaticanSpace(i)) {
            if (track.isPopeSpace(i)){
                System.out.print(TextColors.colorText(TextColors.CYAN, "╚════╝"));
            }else {
            System.out.print(TextColors.colorText(TextColors.YELLOW, "╚════╝"));}
        } else {
            System.out.print("╚════╝");
        }
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

        printer.printFaithTrack(names, position);
    }

}
