package it.polimi.ingsw.litemodel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Scoreboard {

    private final List<BoardEntry> board = new ArrayList<>();
    private final String sentence;

    @JsonCreator
    public Scoreboard(@JsonProperty("sentence") String s){
        sentence = s;
    }

    public void addPlayerScore(String nickname, int score) {
        board.add(new BoardEntry(nickname, score));
        board.sort(Comparator.comparingInt(o -> -o.score));
    }

    public List<BoardEntry> getBoard() {
        List<BoardEntry> temp = new ArrayList<>(board);
        return temp;
    }

    public static class BoardEntry {

        private final String nickname;
        private final int score;

        @JsonCreator
        BoardEntry(@JsonProperty("nickname") String nickname, @JsonProperty("score") int score) {
            this.nickname = nickname;
            this.score = score;
        }

        public String getNickname(){
            return this.nickname;
        }

        public int getScore() {
            return score;
        }
    }


}


