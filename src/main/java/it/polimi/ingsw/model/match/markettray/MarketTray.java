package it.polimi.ingsw.model.match.markettray;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.exceptions.OutOfBoundMarketTrayException;
import it.polimi.ingsw.model.exceptions.moves.MainActionDoneException;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.*;
import it.polimi.ingsw.model.player.PlayerReactEffect;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiPredicate;

/**
 * the market tray class represent the market of resources in the game.
 * it allows the player to obtain resources pushing the extra marble in a column or row
 */
public class MarketTray {
    /**
     * this matrix represent the marbles in the tray
     */
    private final Marble[][] marbles;

    /**
     * used to save the extra marble that stay in the slide
     */
    private Marble slideMarble;

    /**
     * row of the tray
     */
    private final int row;

    /**
     * column of the tray
     */
    private final int col;

    /**
     * exception to check if the passed value is valid to be used in the matrix
     * first parameter is the bound
     * second parameter is the value to check
     */
    BiPredicate<Integer, Integer> boundCheck = (Integer bound, Integer passedBound) -> (bound < passedBound || passedBound < 0);

    /**
     * the constructor read from a json file the size of the tray and instantiate the right amount and type of marble, saved in the json.
     * then it shuffle the order of the marbles and inster them all in the tray
     */
    public MarketTray() {
        Random rand = new Random();

        //Reading the Dimensions of the MarketTray
        DimensionReader dim;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson dimension = gsonBuilder.create();
        try {
            BufferedReader brD = new BufferedReader(new FileReader("src/resources/MarketTrayDimension.json"));
            dim = dimension.fromJson(brD, DimensionReader.class);
        } catch (FileNotFoundException ex){
            System.out.println("The JSON file to set the MarketTray dimensions was not found!");
            dim = new DimensionReader(0,0);
        }


        //Reading the Marbles
        List<Marble> marbleBuilder = new ArrayList<>();
        Gson gson = new Gson();
        try{
            BufferedReader br = new BufferedReader(new FileReader("src/resources/Marble.json"));
            marbleBuilder = gson.fromJson(br, new TypeToken<List<Marble>>(){}.getType());
        } catch (FileNotFoundException e){
            System.out.println("The JSON file to create Marbles was not found!");
        }


        row = dim.row;
        col = dim.col;

        if (marbleBuilder.size() != (((row)*(col))+1)){
            //TODO IMPLEMENTARE LA EXCEPTION
        }

        marbles = new Marble[row][col];

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                marbles[i][j] = marbleBuilder.remove(rand.nextInt(marbleBuilder.size()));
            }
        }

        slideMarble = marbleBuilder.get(0);
    }

    /**
     * for all the marble in the column it is given the right resource to the player
     * get an index of a column and shift the marbles in it until the last one falls into the slide
     * @param shiftCol index of the col
     * @param player player that uses the tray
     * @throws OutOfBoundMarketTrayException launched when shiftCol is out of bound
     */
    public void pushCol(int shiftCol, PlayerReactEffect player) throws OutOfBoundMarketTrayException, MainActionDoneException {
        if (boundCheck.test(this.col, shiftCol)) throw new OutOfBoundMarketTrayException();

        for (int i = 0; i < row; i++) player.obtainResource(marbles[i][shiftCol]);

        Marble temp = marbles[0][shiftCol];
        for (int i = 0; i < (row - 1); i++) {
            marbles[i][shiftCol] = marbles[i+1][shiftCol];
        }
        marbles[row-1][shiftCol] = slideMarble;

        slideMarble = temp;
    }

    /**
     * for all the marble in the row it is given the right resource to the player
     * get an index of a row and shift the marbles in it until the last one falls into the slide
     * @param shiftRow index of the row
     * @param player player that uses the tray
     * @throws OutOfBoundMarketTrayException launched when shiftRow is out of bound
     */
    public void pushRow(int shiftRow, PlayerReactEffect player) throws OutOfBoundMarketTrayException, MainActionDoneException {
        if (boundCheck.test(this.row, shiftRow)) throw new OutOfBoundMarketTrayException();

        for (int i = 0; i < col; i++) player.obtainResource(marbles[shiftRow][i]);

        Marble temp = marbles[shiftRow][0];
        if (col - 1 >= 0) System.arraycopy(marbles[shiftRow], 1, marbles[shiftRow], 0, col - 1);
        marbles[shiftRow][col-1] = slideMarble;

        slideMarble = temp;
    }

    /**
     * return a List of the marbles in the tray
     * @return a list of marbles
     */
    public List<Marble> showMarketTray() {
        List<Marble> toReturn = new ArrayList<>();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                toReturn.add(marbles[i][j].copy());
            }
        }
        return toReturn;
    }

    /**
     * return a copy of the marble in the slide
     * @return Marble
     */
    public Marble showSlideMarble() {
        return slideMarble.copy();
    }
}
