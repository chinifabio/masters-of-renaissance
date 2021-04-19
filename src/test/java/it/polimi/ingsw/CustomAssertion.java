package it.polimi.ingsw;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.fail;

// hope its is legal for testing call to method that must thrown an exception, without doing try/catch
public class CustomAssertion {
    public static void assertThrown(Executable executable) {
        try {
            executable.execute();
            fail();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Test
    public void throwable() {
        assertThrown(()-> {
            throw new Exception();
        });
    }
}
