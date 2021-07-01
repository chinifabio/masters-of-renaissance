package it.polimi.ingsw;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.gui.GUI;

import java.io.IOException;
import java.util.*;
import java.util.List;

public class Launcher {
    public static void main(String[] arg){

        List<String> arguments = new ArrayList<>(Arrays.asList(arg));

        if (!arguments.isEmpty() && arguments.get(0).equals("help")) {
            System.out.println("Read the README file to known how parameters works.\n-> https://github.com/chinifabio/ingswAM2021-Chini-Colabene-Curreri/blob/master/README.md");
            return;
        }

        String executable = "nul";
        String address = "nul";
        int port = -1;

        arguments.remove(executable);
        for (String string : arguments) {
            List<String> data = Arrays.asList(string.split(":"));
            try {
                switch (data.get(0)) {
                    case "address" -> address = data.get(1);
                    case "port" -> port = Integer.parseInt(data.get(1));
                    case "core" -> executable = data.get(1);

                    default -> {
                        System.out.println("Unknown parameter " + data.get(0) + ". Use help too see available arguments");
                        return;
                    }
                }
            } catch (NullPointerException | IndexOutOfBoundsException e) {
                System.out.println("You missed some parameter " + data);
                return;
            } catch (NumberFormatException n) {
                System.out.println("You insert an invalid number: " + data.get(1));
            }
        }

        if (executable.equals("nul")) {
            System.out.print("Type what you want to execute: ");
            executable = new Scanner(System.in).nextLine().toLowerCase();
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

                default -> System.out.println(executable + " is an invalid executable parameter. Valid parameters are {\"cli\", \"gui\", \"server\"");
            }
        } catch (IOException e) {
            System.out.println("fail launching " + executable);
        } catch (java.awt.AWTError sad) {
            System.out.println("You need to install a X11 server to use you monitor");
        }
    }
}
