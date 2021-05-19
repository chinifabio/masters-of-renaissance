package it.polimi.ingsw.view.cli.printer;

import it.polimi.ingsw.TextColors;


import java.util.ArrayList;
import java.util.List;

public class NamePrinter {

    public static void titleName(){
        String title = "\n" +
                TextColors.casualColorYellow("                                                   ███╗   ███╗ █████╗ ███████╗████████╗███████╗██████╗ ███████╗               \n") +
                TextColors.casualColorYellow("                                                   ████╗ ████║██╔══██╗██╔════╝╚══██╔══╝██╔════╝██╔══██╗██╔════╝               \n") +
                TextColors.casualColorYellow("                                                   ██╔████╔██║███████║███████╗   ██║   █████╗  ██████╔╝███████╗               \n") +
                TextColors.casualColorYellow("                                                   ██║╚██╔╝██║██╔══██║╚════██║   ██║   ██╔══╝  ██╔══██╗╚════██║               \n") +
                TextColors.casualColorYellow("                                                   ██║ ╚═╝ ██║██║  ██║███████║   ██║   ███████╗██║  ██║███████║               \n") +
                TextColors.casualColorYellow("                                                   ╚═╝     ╚═╝╚═╝  ╚═╝╚══════╝   ╚═╝   ╚══════╝╚═╝  ╚═╝╚══════╝               \n") +
                TextColors.casualColorYellow("                                                                                                                              \n") +
                TextColors.casualColorYellow("                                                                            ██████╗ ███████╗                                  \n") +
                TextColors.casualColorYellow("                                                                           ██╔═══██╗██╔════╝                                  \n") +
                TextColors.casualColorYellow("                                                                           ██║   ██║█████╗                                    \n") +
                TextColors.casualColorYellow("                                                                           ██║   ██║██╔══╝                                    \n") +
                TextColors.casualColorYellow("                                                                           ╚██████╔╝██║                                       \n") +
                TextColors.casualColorYellow("                                                                            ╚═════╝ ╚═╝                                       \n") +
                TextColors.casualColorYellow("                                                                                                                              \n") +
                TextColors.casualColorYellow("                                       ██████╗ ███████╗███╗   ██╗ █████╗ ██╗███████╗███████╗ █████╗ ███╗   ██╗ ██████╗███████╗\n") +
                TextColors.casualColorYellow("                                       ██╔══██╗██╔════╝████╗  ██║██╔══██╗██║██╔════╝██╔════╝██╔══██╗████╗  ██║██╔════╝██╔════╝\n") +
                TextColors.casualColorYellow("                                       ██████╔╝█████╗  ██╔██╗ ██║███████║██║███████╗███████╗███████║██╔██╗ ██║██║     █████╗  \n") +
                TextColors.casualColorYellow("                                       ██╔══██╗██╔══╝  ██║╚██╗██║██╔══██║██║╚════██║╚════██║██╔══██║██║╚██╗██║██║     ██╔══╝  \n") +
                TextColors.casualColorYellow("                                       ██║  ██║███████╗██║ ╚████║██║  ██║██║███████║███████║██║  ██║██║ ╚████║╚██████╗███████╗\n") +
                TextColors.casualColorYellow("                                       ╚═╝  ╚═╝╚══════╝╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝╚══════╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═══╝ ╚═════╝╚══════╝\n") +
                "                                                                                                                              ";
        System.out.println(title);
    }

}
