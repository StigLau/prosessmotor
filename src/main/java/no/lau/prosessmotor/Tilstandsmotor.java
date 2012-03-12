package no.lau.prosessmotor;

import no.lau.prosessmotor.limbo.Limbo;
import no.lau.prosessmotor.stev.Stev;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Tilstandsmotor {

    List<State> states = new ArrayList<State>();
    List<Prosess> processes = new ArrayList<Prosess>();
    private Limbo limbo;


    public State run(String prosessId) throws Exception {
        for (Prosess process : processes) {
            if(process.prosessId.equals(prosessId)) {
                return run(process, process.state);
            }
        }
        throw new Exception("Process with ID " + prosessId + " not found in repository");
    }

    private State run(Prosess process, State state) throws Exception {
        Map<String, String> contextAfterRunning = limbo.run(process.prosessId, state.toString());
        int currentStateIndex = states.indexOf(state);
        if(currentStateIndex + 1 >= states.size()) {
            return process.state;
        } else {
            return run(process, states.get(currentStateIndex + 1));
        }
    }

    public void createNewProsess(String prosessId) {
        processes.add(new Prosess(prosessId, states.get(0)));
    }

    public void addState(State state) {
        states.add(state);
    }

    public void observeLimbo(Limbo limbo) {
        this.limbo = limbo;
    }
}


class State {

    public final String name;

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

class Prosess {

    final public String prosessId;
    public State state;

    public Prosess(String prosessId, State state) {
        this.prosessId = prosessId;
        this.state = state;
    }
}