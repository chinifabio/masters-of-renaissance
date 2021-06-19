package it.polimi.ingsw.litemodel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
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
    }
}

class BoardEntry {
    public final String nickname;
    public final int score;

    @JsonCreator
    BoardEntry(@JsonProperty("nickname") String nickname, @JsonProperty("score") int score) {
        this.nickname = nickname;
        this.score = score;
    }
}
