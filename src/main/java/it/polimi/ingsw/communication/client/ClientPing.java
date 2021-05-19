package it.polimi.ingsw.communication.client;

import it.polimi.ingsw.communication.VirtualSocket;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;

import java.time.LocalDate;
import java.time.LocalTime;

public class ClientPing implements Runnable{

    public final VirtualSocket socket;

    public Packet pong;

    private boolean timeCheck;

    public ClientPing(VirtualSocket socket) {
        this.socket = socket;
        this.timeCheck = false;
    }

    @Override
    public void run() {
        try {
            Thread pongReceiver = new Thread(new PongReceiver(socket,this));
            pongReceiver.setDaemon(true);
            pongReceiver.start();
            pongReceiver.join();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        Packet ping = new Packet(HeaderTypes.PING, ChannelTypes.CONNECTION_STATUS, "ping");
        int t;
        while(true){
            this.socket.send(ping);
            t = LocalTime.now().getSecond();
            while(true){
                synchronized (this.pong){
                    this.timeCheck = getPong();
                }
                if((LocalTime.now().getSecond() - t )  > 10 || !this.timeCheck){                             // il timer è sbagliato, prende i secondi da ore/minuti/secondi, non va bene
                    System.out.println("Connessione col server persa");
                    System.exit(-1);
                }
                if(getPong()){

                }
            }
        }
    }

    public void setPong(Packet pong){
        this.pong = pong;
    }

    public boolean getPong(){
        if(this.pong.header.equals(HeaderTypes.INVALID)) return  false;
        else if(this.pong.header.equals(HeaderTypes.PONG)) return true;
        System.out.println("Non mi è tornato un pong, errore!");
        System.exit(-1);
        return false;
    }
}
