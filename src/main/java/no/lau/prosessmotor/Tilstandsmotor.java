package no.lau.prosessmotor;

import java.util.ArrayList;
import java.util.List;

public class Tilstandsmotor {

    public List<State> states = new ArrayList<State>();
    List<Prosess> processes = new ArrayList<Prosess>();


    public State run(String prosessId) throws Exception {
        for (Prosess process : processes) {
            if(process.prosessId.equals(prosessId)) {
                return process.state;
            }
        }
        throw new Exception("Process with ID " + prosessId + " not found in repository");
    }

    public void createNewProsess(String prosessId) {
        processes.add(new Prosess(prosessId, states.get(0)));
    }

    public void addState(State state) {
        states.add(state);
    }
}


class State {

    public final String name;

    public State(String name) {
        this.name = name;
    }
    public String toString() {
        return name;
    }
}

class Prosess {

    final public String prosessId;
    public State state;

    public Prosess(String prosessId, State state) {
        this.prosessId = prosessId;
        this.state = state;
    }
}