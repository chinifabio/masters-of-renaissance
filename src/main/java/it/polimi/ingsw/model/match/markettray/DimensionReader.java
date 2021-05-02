package it.polimi.ingsw.model.match.markettray;

/**
 * This class reads the dimension of the Market Tray by file
 */
public class DimensionReader {
    /**
     * This attribute is the value of the number of rows of the MarketTray
     */
    public final int row;

    /**
     * This attribute is the value of the number of columns of the MarketTray
     */
    public final int col;

    /**
     * This method is the constructor of the class
     * @param row is the new value of rows of the MarketTray
     * @param col is the new value of columns of the MarketTray
     */
    public DimensionReader(int row, int col){
        this.row = row;
        this.col = col;
    }
}
