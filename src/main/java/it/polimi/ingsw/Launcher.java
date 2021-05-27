package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.view.cli.CLI;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Hello world!
 *
 */
public class Launcher
{
    public static void main(String[] arg){
        List<String> arguments = Arrays.asList(arg);
        Thread main;

        try {
            switch (arguments.get(0)) {
                case "cli":
                    main = new Thread(new Client(new CLI()));
                    break;
                case "server":
                    main = new Thread(new Server());
                    break;
                case "gui":
                    main = new Thread(()-> System.out.println("work in progress"));
                    break;
                case "--help":
                case "-h":
                    main = new Thread(()-> System.out.println("work in progress"));
                    break;
                default:
                    System.out.println(arguments.get(0) + " is not a valid argument");
                    return;
            }
        } catch (IOException e) {
            System.out.println("fail launching " + arguments.get(0));
            return;
        } catch (NullPointerException nul) {
            System.out.println("you missed some arguments... use --help to see all available accepted arguments");
            return;
        }

        main.setDaemon(true);
        main.start();
        try {
            main.join(); // i don't know how to not terminate jvm at the end of this static main so the daemon continue to run
        } catch (InterruptedException e) {
            System.out.println("join failed");
        }
    }
}
