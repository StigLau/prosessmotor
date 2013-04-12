package no.lau.prosessmotor.limbo;

import org.joda.time.DateTime;

public class History {
    public final String stepName;
    public final String state;
    public final DateTime dateTime;

    public History(String stepName, String state, DateTime dateTime) {
        this.stepName = stepName;
        this.state = state;
        this.dateTime = dateTime;
    }
    public String toString() {
        return dateTime.toString("dd.MM.yyyy HH:mm:ss") + " " + stepName + " " + state;
    }
}
