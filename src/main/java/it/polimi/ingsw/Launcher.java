package it.polimi.ingsw;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.gui.GUI;

import java.awt.*;
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
                case "cli" -> {
                    if (System.getProperty("os.name").contains("Windows")) {
                        System.out.println("You can't run this JAR on windows... To fix you can try to install WSL");
                        return;
                    }
                    main = new Thread(new CLI("localhost", 4444));
                }
                case "server" -> {
                    if (System.getProperty("os.name").contains("Windows")) {
                        System.out.println("You can't run this JAR on windows... To fix you can try to install WSL");
                        return;
                    }
                    main = new Thread(new Server(4444));
                }
                case "gui" -> main = new Thread(new GUI("localhost", 4444));
                case "--help", "-h" -> main = new Thread(() -> System.out.println("work in progress"));
                default -> {
                    System.out.println(arguments.get(0) + " is not a valid argument");
                    return;
                }
            }
        } catch (IOException e) {
            System.out.println("fail launching " + arguments.get(0));
            System.out.println(e.getMessage());
            return;
        } catch (NullPointerException nul) {
            System.out.println("you missed some arguments... use --help to see all available accepted arguments");
            System.out.println(nul.getMessage());
            return;
        } catch (HeadlessException he) {
            System.out.println("To use the gui you need to enable the forwarding x11 display in your client");
            System.out.println(he.getMessage());
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
