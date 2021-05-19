package it.polimi.ingsw.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Tuple<A, B> {
    public final A a;
    public final B b;

    @JsonCreator
    public Tuple(@JsonProperty("a") A a, @JsonProperty("b") B b) {
        this.a = a;
        this.b = b;
    }
}
