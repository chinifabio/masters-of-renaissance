package it.polimi.ingsw.model.player;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;

public class SetPlayersNumberPlayerState extends PlayerState {

    /**
     * the constructor take the two final attribute of the state that are the personal board and the context.
     * the player holdings are passed to not share it out of the player states and do information hiding
     *
     * @param context the context
     */
    protected SetPlayersNumberPlayerState(Player context) {
        super(context, "please set the player number, match does not exist!");
    }

    /**
     * can the player do staff?
     *
     * @return true yes, false no
     */
    @Override
    public boolean doStuff() {
        return true;
    }

    /**
     * Create a game with the passed number of player
     *
     * @param number the number of player of the match to be create
     * @return the succeed of the operation
     */
    @Override
    public Packet setPlayerNumber(int number) {
        if (number <= 4 && number > 0){
            this.context.setState(new PendingMatchStartPlayerState(this.context));
            this.context.model.createMatchOf(number);
            return new Packet(HeaderTypes.OK, ChannelTypes.PLAYER_ACTIONS, "match created, now wait other players!");

        } else return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, "invalid players number :(");
    }
}
