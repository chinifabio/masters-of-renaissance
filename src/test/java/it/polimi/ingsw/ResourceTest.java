package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.resource.*;
import org.junit.jupiter.api.Test;

public class ResourceTest {
    /**
     * test the Coin class, passing an amount and test if its implementation is correct
     */
    @Test
    public void coin(){
        int amount = 2;

        Resource res = new Coin(amount);
        boolean result = false;

        if(res.isStorable() && res.amount() == amount && res.type() == ResourceType.COIN) result = true;

        assertTrue(result);
    }


    /**
     * test the Empty class, passing an amount and test if its implementation is correct
     */
    @Test
    public void empty(){
        int amount = 2;

        Resource res = new Empty(amount);
        boolean result = false;

        if(res.isStorable() && res.amount() == amount && res.type() == ResourceType.EMPTY) result = true;

        assertTrue(result);
    }


    /**
     * test the Faith Point class, passing an amount and test if its implementation is correct
     */
    @Test
    public void faithpoint(){
        int amount = 2;

        Resource res = new FaithPoint(amount);
        boolean result = false;

        if(!res.isStorable() && res.amount() == amount && res.type() == ResourceType.FAITHPOINT) result = true;

        assertTrue(result);
    }


    /**
     * test the Servant class, passing an amount and test if its implementation is correct
     */
    @Test
    public void servant(){
        int amount = 2;

        Resource res = new Servant(amount);
        boolean result = false;

        if(res.isStorable() && res.amount() == amount && res.type() == ResourceType.SERVANT) result = true;

        assertTrue(result);
    }


    /**
     * test the Shield class, passing an amount and test if its implementation is correct
     */
    @Test
    public void shield(){
        int amount = 2;

        Resource res = new Shield(amount);
        boolean result = false;

        if(res.isStorable() && res.amount() == amount && res.type() == ResourceType.SHIELD) result = true;

        assertTrue(result);
    }


    /**
     * test the Stone class, passing an amount and test if its implementation is correct
     */
    @Test
    public void stone(){
        int amount = 2;

        Resource res = new Stone(amount);
        boolean result = false;

        if(res.isStorable() && res.amount() == amount && res.type() == ResourceType.STONE) result = true;

        assertTrue(result);
    }


    /**
     * test the Unknown class, passing an amount and test if its implementation is correct
     */
    @Test
    public void unknown(){
        Resource res = new Unknown();
        boolean result = false;

        if(!res.isStorable() && res.amount() == 0 && res.type() == ResourceType.UNKNOWN) result = true;

        assertTrue(result);
    }
}
