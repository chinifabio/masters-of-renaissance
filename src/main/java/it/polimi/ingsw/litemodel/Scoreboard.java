package it.polimi.ingsw.litemodel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * The scoreboard class is used to store into the lite model the result of the game
 */
public class Scoreboard {

    /**
     * The array that contains all the entry of the leaderboard
     */
    private final List<BoardEntry> board = new ArrayList<>();

    /**
     * A sentence to notify before the leaderboard
     */
    private final String sentence;

    /**
     * Create a scoreboard with a sentence
     * @param s sentence to notify to the player
     */
    @JsonCreator
    public Scoreboard(@JsonProperty("sentence") String s){
        sentence = s;
    }

    /**
     * Create an entry of the leaderboard and then sort the leaderboard to respect the correct order
     * @param nickname the nickname of the player
     * @param score the victory point of the player
     * @param res the number of resource of the player
     */
    public void addPlayerScore(String nickname, int score, int res) {
        board.add(new BoardEntry(nickname, score, res));
        board.sort((o1, o2) -> {
            int temp = -1;
            for (int i = 0; temp != 0; i++) {
                switch (i) {
                    case 0 -> temp = o1.score - o2.score;
                    case 1 -> temp = o1.resources - o2.resources;
                    case 2 -> temp = o1.nickname.compareTo(o2.nickname);
                    default -> temp = 1;
                }
            }
            return temp;
        });
    }

    /**
     * Return the board copied into a new array
     * @return the leaderboard entry array
     */
    public List<BoardEntry> getBoard() {
        return new ArrayList<>(board);
    }

    /**
     * This class represent a entry of the leaderboard
     */
    public static class BoardEntry {

        /**
         * The nickname of the player
         */
        private final String nickname;

        /**
         * the victory point of the player
         */
        private final int score;

        /**
         * the number of resources of the player
         */
        private final int resources;

        @JsonCreator
        BoardEntry(@JsonProperty("nickname") String nickname, @JsonProperty("score") int score, @JsonProperty("resources") int resources) {
            this.nickname = nickname;
            this.score = score;
            this.resources = resources;
        }

        /**
         * Return the nickname of the player
         * @return nickname
         */
        public String getNickname(){
            return this.nickname;
        }

        /**
         * return the score of the player
         * @return score
         */
        public int getScore() {
            return score;
        }

        /**
         * return the number of resources
         * @return number of resources
         */
        public int getResources() {
            return resources;
        }
    }
}


