package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.view.litemodel.LiteFaithTrack;

import java.io.IOException;
import java.util.*;

public class FaithTrackPrinter {

    private final LiteFaithTrack track;

    public FaithTrackPrinter(LiteFaithTrack track) {
        this.track = track;
    }

    //Max player's name char: 20
    public void printFaithTrack(List<String> nicknames, List<Integer> positions){
        System.out.println();
        printNormalTrack();
        printPlayerPos(nicknames, positions);
    }


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
                            temp.append(TextColors.colorText(TextColors.PURPLE_BRIGHT, "║ "));
                            temp.append(TextColors.colorText(colors[j]," ┼ "));
                            temp.append(TextColors.colorText(TextColors.PURPLE_BRIGHT, " ║"));
                        } else {
                            temp.append(TextColors.colorText(TextColors.PURPLE_BRIGHT, "║  " + "   ║"));
                        }
                    } else {
                        if (i == positions.get(j)) {
                            temp.append(TextColors.colorText(TextColors.YELLOW_BRIGHT, "║ "));
                            temp.append(TextColors.colorText(colors[j], " ┼ "));
                            temp.append(TextColors.colorText(TextColors.YELLOW_BRIGHT, " ║"));
                        } else {
                            temp.append(TextColors.colorText(TextColors.YELLOW_BRIGHT, "║  " + "   ║"));
                        }
                    }
                } else {
                    if (i == positions.get(j)){
                        temp.append( "║ ");
                        temp.append(TextColors.colorText(colors[j]," ┼ "));
                        temp.append(" ║");
                    } else {
                        temp.append("║     ║");
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

    private void printTopCell(int number, LiteFaithTrack track) {
        if (number <= 9) {
            if (track.isVaticanSpace(number)) {
                if (track.isPopeSpace(number)){
                    System.out.print(TextColors.colorText(TextColors.PURPLE_BRIGHT, "╔══" + (number) + "══╗"));
                } else{
                System.out.print(TextColors.colorText(TextColors.YELLOW_BRIGHT, "╔══" + (number) + "══╗"));
                }
            } else {
                System.out.print("╔══" + (number) + "══╗");
            }
        } else {
            if (track.isVaticanSpace(number)) {
                if (track.isPopeSpace(number)){
                    System.out.print(TextColors.colorText(TextColors.PURPLE_BRIGHT, "╔═" + (number) + "══╗"));
                } else {
                System.out.print(TextColors.colorText(TextColors.YELLOW_BRIGHT, "╔═" + (number) + "══╗"));}
            } else {
                System.out.print("╔═" + (number) + "══╗");
            }
        }
    }

    private void printMiddleCell(LiteFaithTrack track, int number) {
        if (track.getVictoryPointCell(number) == 0) {
            if (track.isVaticanSpace(number)) {
                if (track.isPopeSpace(number)){
                    System.out.print(TextColors.colorText(TextColors.PURPLE_BRIGHT, "║  " + "   ║"));
                } else {
                System.out.print(TextColors.colorText(TextColors.YELLOW_BRIGHT, "║  " + "   ║"));}
            } else {
                System.out.print("║  " + "   ║");
            }
        } else if (track.getVictoryPointCell(number) <= 9) {
            checkVaticanSpacePrint(track, number);
        } else {
            if (number <= 9) {
                checkVaticanSpacePrint(track, number);
            } else {
                if (track.isVaticanSpace(number)) {
                    if (track.isPopeSpace(number)){
                        System.out.print(TextColors.colorText(TextColors.PURPLE_BRIGHT, "║ " + track.getVictoryPointCell(number) + "  ║"));
                }else{
                    System.out.print(TextColors.colorText(TextColors.YELLOW_BRIGHT, "║ " + track.getVictoryPointCell(number) + "  ║"));}
                } else {
                    System.out.print("║ " + track.getVictoryPointCell(number) + "  ║");
                }
            }
        }
    }

    private void checkVaticanSpacePrint(LiteFaithTrack track, int number) {
        if (track.isVaticanSpace(number)) {
            if (track.isPopeSpace(number)){
                System.out.print(TextColors.colorText(TextColors.PURPLE_BRIGHT, "║  " + track.getVictoryPointCell(number) + "  ║"));
        }else{
            System.out.print(TextColors.colorText(TextColors.YELLOW_BRIGHT, "║  " + track.getVictoryPointCell(number) + "  ║"));}
        } else {
            System.out.print("║  " + track.getVictoryPointCell(number) + "  ║");
        }
    }

    private void printBottomCell(LiteFaithTrack track, int i){
        if (track.isVaticanSpace(i)) {
            if (track.isPopeSpace(i)){
                System.out.print(TextColors.colorText(TextColors.PURPLE_BRIGHT, "╚═════╝"));
            }else {
            System.out.print(TextColors.colorText(TextColors.YELLOW_BRIGHT, "╚═════╝"));}
        } else {
            System.out.print("╚═════╝");
        }
    }

}
