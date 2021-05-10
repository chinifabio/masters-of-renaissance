package it.polimi.ingsw.view.cli.printer.effectprint;

public class AddDepotPrinter implements EffectPrinter{

    private final String[][] newDepot = new String[HEIGHT][WIDTH];

    @Override
    public String[][] printEffect() {
        return newDepot;
    }


}
