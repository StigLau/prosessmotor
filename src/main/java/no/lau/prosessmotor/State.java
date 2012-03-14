package no.lau.prosessmotor;

import no.lau.prosessmotor.stev.Stev;

public class State {

    public final String name;
    public static final State OK = new State("ok");
    public static final State FAILED = new State("failed");

    public State(String name) {
        this.name = name;
    }

    public State(Stev stev) {
        this.name = stev.toString();
    }

    public String toString() {
        return name;
    }
}
