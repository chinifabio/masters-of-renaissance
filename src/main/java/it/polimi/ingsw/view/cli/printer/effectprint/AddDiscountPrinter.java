package it.polimi.ingsw.view.cli.printer.effectprint;

public class AddDiscountPrinter implements EffectPrinter{

    private final String[][] effect = new String[HEIGHT][WIDTH];

    @Override
    public String[][] printEffect() {
        createEffect(effect);
        return effect;
    }

    public void createEffect(String[][] effect){
        //TODO
    }

    public static void main(String[] args){
        AddDiscountPrinter printer = new AddDiscountPrinter();
        String[][] toPrint;

        toPrint = printer.printEffect();

        for (int i = 0; i<HEIGHT-1; i++){
            for (int j = 0; j<WIDTH-1; j++){
                System.out.print(toPrint[i][j]);
            }
            System.out.println();
        }

    }

}
