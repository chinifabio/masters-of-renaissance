package it.polimi.ingsw.view.cli.printer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.litemodel.litecards.LiteDevCard;
import it.polimi.ingsw.litemodel.litecards.LiteDevSetup;
import it.polimi.ingsw.model.exceptions.card.EmptyDeckException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DevSetupPrinter {

    List<List<LiteDevCard>> devSetup = new ArrayList<>();;

    public DevSetupPrinter() {
        //TESTING
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            devSetup = objectMapper.readValue(
                    new File("src/resources/DevCards.json"),
                    new TypeReference<List<List<LiteDevCard>>>(){});
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws EmptyDeckException {
        //TESTING
        DevSetupPrinter printer = new DevSetupPrinter();
        LiteDevSetup setup = new LiteDevSetup();

        for (List<LiteDevCard> list : printer.devSetup){
            for (LiteDevCard card: list){
                setup.setDevCard(card);
            }
        }
    }
}
