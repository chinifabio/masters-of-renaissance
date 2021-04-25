package it.polimi.ingsw.util;

/**
 * This class is used to contains a pair of a parametrized type T object.
 * Objects are public and final so we don't need setter and getter method
 * @param <T> the type of the pair
 */
public class Pair<T> {
    /**
     * first instance of the pair
     */
    public final T one;

    /**
     * the second instance of the pair
     */
    public final T two;

    /**
     * This method create a pair of two T Object
     * @param one the first instance
     * @param two the second instance
     */
    public Pair (T one, T two) {
        this.one = one; this.two = two;
    }
}
