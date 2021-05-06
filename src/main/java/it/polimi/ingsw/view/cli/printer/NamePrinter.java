package it.polimi.ingsw.view.cli.printer;

import it.polimi.ingsw.TextColors;


import java.util.ArrayList;
import java.util.List;

public class NamePrinter {
    
    

    public void titleName(){
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

    public void faithTrackName(){
        List<String> track = new ArrayList<>();

        track.add(TextColors.casualColorRed("                                             " + "      _______         __  __    __      _______                     __    \n"));
        track.add(TextColors.casualColorRed("                                            " + "     |    ___|.---.-.|__||  |_ |  |--. |_     _|.----..---.-..----.|  |--.\n"));
        track.add(TextColors.casualColorRed("                                            " + "     |    ___||  _  ||  ||   _||     |   |   |  |   _||  _  ||  __||    < \n"));
        track.add(TextColors.casualColorRed("                                            " + "     |___|    |___._||__||____||__|__|   |___|  |__|  |___._||____||__|__|\n"));

        System.out.println(track);
    }
}
