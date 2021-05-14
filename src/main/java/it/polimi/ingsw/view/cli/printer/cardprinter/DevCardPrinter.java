package it.polimi.ingsw.view.cli.printer.cardprinter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.litemodel.litecards.LiteDevCard;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DevCardPrinter {
    private static final int HEIGHT = 7; //rows.
    private static final int WIDTH = 12; //cols.

    private final List<LiteDevCard> devCards = new ArrayList<>();

    List<List<LiteDevCard>> devCardsList = new ArrayList<>();

    public DevCardPrinter() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            devCardsList = objectMapper.readValue(
                    new File("src/resources/DevCards.json"),
                    new TypeReference<List<List<LiteDevCard>>>(){});
        }catch (IOException e) {
            e.printStackTrace();
        }
        for (List<LiteDevCard> list : this.devCardsList){
            devCards.addAll(list);
        }
    }

    public void createDevCard(String[][] devCard, String ID){
        String colorCard;
        LiteDevCard toPrint = null;
        for (LiteDevCard card: devCards){
            if (card.getId().equals(ID)){
                toPrint = card;
            }
        }
        assert toPrint != null;
        colorCard = toPrint.getColor().getDevCardColor();

        devCard[0][0] = TextColors.colorText(colorCard,"╔");
        devCard[HEIGHT -1][0] = TextColors.colorText(colorCard,"╚");
        for (int i = 1; i< WIDTH -1; i++){
            devCard[0][i] = TextColors.colorText(colorCard,"═");
            devCard[HEIGHT -1][i] = TextColors.colorText(colorCard,"═");
        }
        for (int i = 4; i <8; i++){
            devCard[0][i] = "";
        }
        devCard[0][4] = TextColors.colorText(colorCard,toPrint.getId());
        if (toPrint.getId().length() < 4) {
            devCard[0][5] = TextColors.colorText(colorCard,"═");
        }

        devCard[0][WIDTH -1] = TextColors.colorText(colorCard,"╗");
        for (int r = 1; r < HEIGHT -1; r++){
            devCard[r][0] = (TextColors.colorText(colorCard,"║"));
            for (int c = 1; c < WIDTH -1; c++){
                devCard[r][c] = " ";
            }
            devCard[r][WIDTH - 1] = TextColors.colorText(colorCard,"║");
        }
        devCard[HEIGHT -6][WIDTH -7] = TextColors.colorText(TextColors.PURPLE_BRIGHT,String.valueOf(toPrint.getVictoryPoint()));
        if (toPrint.getVictoryPoint() > 9){
            devCard[HEIGHT -6][WIDTH -6] = "";
        }
        devCard[HEIGHT -1][WIDTH -1] = TextColors.colorText(colorCard,"╝");


        devCard[HEIGHT-1][3] = TextColors.colorText(colorCard,"LEVEL");
        devCard[HEIGHT-1][4] = TextColors.colorText(colorCard, String.valueOf(toPrint.getLevel().getLevelCard()));
        for (int i = 5; i< WIDTH-3; i++){
            devCard[HEIGHT-1][i] = "";
        }
        toPrint.getEffect().getPrinter().printEffect(devCard);
        for (int i = 1; i < devCard[3].length - 1; i++) {
            devCard[2][i] = TextColors.colorText(colorCard,"-");
        }

    }


    public void printDevCard(String devCardId){
        String[][] devCard = new String[HEIGHT][WIDTH];

        createDevCard(devCard, devCardId);
        for (int r = 0; r < (HEIGHT); r++) {
            System.out.println();
            for (int c = 0; c < (WIDTH); c++) {
                System.out.print(devCard[r][c]);
            }
        }

    }

    public static void main(String[] args) throws IOException {
        DevCardPrinter printer = new DevCardPrinter();

        for (int i = 1; i < 49; i++) {
            printer.printDevCard("DC" + i);
        }

    }

}
