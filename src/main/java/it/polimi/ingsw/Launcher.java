package it.polimi.ingsw;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.gui.GUI;

import java.io.IOException;
import java.util.*;
import java.util.List;

public class Launcher
{
    public static void main(String[] arg){

        List<String> arguments = new ArrayList<>(Arrays.asList(arg));

        String executable;
        String address = "nul";
        int port = -1;

        try {
            executable = arguments.get(0);
            if (!Arrays.asList("cli", "gui", "server").contains(executable)) throw new NullPointerException();
        } catch (NullPointerException | IndexOutOfBoundsException nul) {
            System.out.println("I don't known what to launch. Choose as first argument one of those {\"server\", \"cli\", \"gui\"}");
            return;
        }

        if (executable.equals("help")) {
            System.out.println(
                    "As first argument you can choose between:\n" +
                            "   cli\n" +
                            "   gui\n" +
                            "   server\n\n" + "" +
                            "Then to choose the address or the port you can type the argument, followed by : and than the value." +
                            " -> \"address:localhost\" or \"port:4444\"" +
                            "\n"
            );
            return;
        }

        arguments.remove(executable);
        for (String string : arguments) {
            List<String> data = Arrays.asList(string.split(":"));
            try {
                switch (data.get(0)) {
                    case "address" -> address = data.get(1);

                    case "port" -> port = Integer.parseInt(data.get(1));

                    default -> {
                        System.out.println("Unknown parameter " + data.get(0) + ". Use help too see available arguments");
                        return;
                    }
                }
            } catch (NullPointerException e) {
                System.out.println("You missed some parameter " + data);
                return;
            } catch (NumberFormatException n) {
                System.out.println("You insert a not valid number: " + data.get(1));
            }
        }

        if (address.equals("nul") && !executable.equals("server")) {
            System.out.print("Type the server address: ");
            address = new Scanner(System.in).nextLine();
        }

        if (port == -1) {
            System.out.print("Type the server port: ");
            try {
                port = Integer.parseInt(new Scanner(System.in).nextLine());
            } catch (NumberFormatException n) {
                System.out.println("Not a number: " + port);
                return;
            }
        }

        if (port < 1024) {
            System.out.println("Illegal server port " + port);
            return;
        }

        try {
            switch (executable) {
                case "cli" -> {
                    if (System.getProperty("os.name").contains("Windows")) {
                        System.out.println("You can't run this JAR on windows... To fix you can try to install WSL");
                        return;
                    }
                    new CLI(address, port).start();
                }
                case "server" -> {
                    if (System.getProperty("os.name").contains("Windows")) {
                        System.out.println("You can't run this JAR on windows... To fix you can try to install WSL");
                        return;
                    }
                    new Server(port).start();
                }
                case "gui" -> new GUI(address, port).start();
            }
        } catch (IOException e) {
            System.out.println("fail launching " + executable);
        } catch (java.awt.AWTError sad) {
            System.out.println("You need to install a X11 server to use you monitor");
        }
    }
}
