package no.lau.prosessmotor;

import no.lau.prosessmotor.stev.Stev;

public class Steg {

    public final String name;

    public Steg(String name) {
        this.name = name;
    }

    public Steg(Stev stev) {
        this.name = stev.toString();
    }

    public String toString() {
        return name;
    }
}
