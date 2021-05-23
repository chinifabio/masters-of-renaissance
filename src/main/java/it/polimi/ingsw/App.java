package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.Server;

import java.util.Arrays;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] arg){
        List<String> arguments = Arrays.asList(arg);

        if (arguments.contains("cli")) {
            Client.main(arg);
            return;
        }

        if (arguments.contains("server")) {
            Server.main(arg);
            return;
        }

        System.out.println("no use case passed, quitting...");
    }
}
