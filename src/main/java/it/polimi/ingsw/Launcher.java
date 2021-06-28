package it.polimi.ingsw;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.gui.GUI;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class Launcher
{
    public static void main(String[] arg){

        List<String> arguments = Arrays.asList(arg);
        View view;

        try {
            switch (arguments.get(0)) {
                case "cli" -> {
                    if (System.getProperty("os.name").contains("Windows")) {
                        System.out.println("You can't run this JAR on windows... To fix you can try to install WSL");
                        return;
                    }
                    new CLI("localhost", 4444).start();
                }
                case "server" -> {
                    if (System.getProperty("os.name").contains("Windows")) {
                        System.out.println("You can't run this JAR on windows... To fix you can try to install WSL");
                        return;
                    }
                    new Server(4444).start();
                }
                case "gui" -> new GUI("localhost", 4444).start();
                case "--help", "-h" -> System.out.println("work in progress");
                default -> System.out.println(arguments.get(0) + " is not a valid argument");
            }
        } catch (IOException e) {
            System.out.println("fail launching " + arguments.get(0));
            System.out.println(e.getMessage());
        } catch (NullPointerException nul) {
            System.out.println("you missed some arguments... use --help to see all available accepted arguments");
            System.out.println(nul.getMessage());
        } catch (HeadlessException he) {
            System.out.println("To use the gui you need to enable the forwarding x11 display in your client");
            System.out.println(he.getMessage());
        }
    }
}
