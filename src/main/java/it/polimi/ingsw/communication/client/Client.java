package it.polimi.ingsw.communication.client;

import it.polimi.ingsw.communication.DummyClient;
import it.polimi.ingsw.communication.VirtualSocket;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;

import java.io.IOException;
import java.net.Socket;

public class Client implements Runnable{

    private static int port = 4445;
    private VirtualSocket socket;
    public static String address = "127.0.0.2";

    private String nickname;

    private ClientState clientState;

    public Client() {
        this.clientState = new InitialCS(this);
    }

    private void setNickname(String nick){
        this.nickname = nick;
    }

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--address")) DummyClient.address = args[1];

        Thread client = new Thread(new Client());
        client.setDaemon(true);
        client.start();
        try{
            client.join();
        } catch (InterruptedException e){
            System.out.println("Interrupted");
        }
    }

    /**
     * Receive a state and set it as current state
     * @param newClientState
     */
    public void setState(ClientState newClientState){ this.clientState = newClientState; }

    @Override
    public void run() {
        boolean wait = true;

        try{
            this.socket = new VirtualSocket(new Socket(address,port));
        } catch (IOException e) {
            System.out.println("Socket error");
            return;
        }
        new Thread(this.socket).start();        // a che serve?

        nickname = "pino";

        Packet packCheck;
        Packet packIn;
        boolean cond = true;
        while (cond) {
            this.socket.send(this.clientState.setNickname());
            packIn = this.socket.pollPacketFrom(ChannelTypes.PLAYER_ACTIONS);
            if (!packIn.header.equals(HeaderTypes.INVALID)) {
                cond=false;
            }
        }
        cond=true;
        HeaderTypes header = this.socket.pollPacketFrom(ChannelTypes.PLAYER_ACTIONS).header;
        while (cond && header.equals(HeaderTypes.CREATE_LOBBY)) {
            this.setState(new SetPlayersNumberCS(this));
            this.socket.send(this.clientState.setNumberOfPlayers());
            if(this.socket.pollPacketFrom(ChannelTypes.PLAYER_ACTIONS).header.equals(HeaderTypes.OK)){
                cond=false;
            }
        }

        this.setState(new PendingMatchStartCS(this));

        while(true){
            // scegli azione:
            if(!wait){
                String action = "setNickname";      //gui/cli per scegliere azione
                switch (action){
                    case "setNickname":
                        packCheck = this.clientState.setNickname();
                        if(!packCheck.header.equals(HeaderTypes.INVALID)){
                            this.socket.send(packCheck);
                        } else {
                            // ? ? ? nick settato fuori dall'initialCS
                        }
                        break;
                    case "SetPlayersNumber":
                        packCheck = this.clientState.setNumberOfPlayers();
                        if(!packCheck.header.equals(HeaderTypes.INVALID)){
                            this.socket.send(packCheck);
                        } else {
                            // ? ? ? numero di giocatori settato fuori dal setPlayerNumberCS
                        }
                        // CREARE FUNZIONE PER IL CHECK?
                        // Qui va avanti per tutte le possibili funzioni, creare direttamente una funzione che prende la stringa e fa tutto?
                }
            }


            Packet packet = this.socket.pollPacketFrom(ChannelTypes.NOTIFY_VIEW);
            HeaderTypes packetHeader = packet.header;
            switch (packetHeader){
                case INVALID:
                    //cli/gui: non puoi farlo. //TODO invalid to return initialCS?
                    break;
                case CREATE_LOBBY:
                    this.setState(new SetPlayersNumberCS(this));
                    // cli/gui: inserisci numero di player
                    break;
                case OK:

                    break;
                case JOIN_LOBBY:
                    this.setState(new PendingMatchStartCS(this));
                    break;
                case GAME_STARTED:
                case START_TURN:
                    this.setState(new NoActionDoneCS(this));
                    break;
                case END_TURN:
                    this.setState(new WaitMyTurnCS(this));
                    break;
                case END_GAME:
                    this.setState(new EndGameCS(this));
                    break;
            }
        }
    }
}