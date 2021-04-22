package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class AppTest
{
    /**
     * Rigorous test
     */
    @Test
    public void test(){
        App app = new App();
        assertEquals(5, app.sample);
    }
}
