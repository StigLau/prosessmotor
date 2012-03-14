package no.lau.prosessmotor;

import no.lau.prosessmotor.limbo.Limbo;

import java.util.ArrayList;
import java.util.List;

public class Tilstandsmotor {

    List<State> stevList = new ArrayList<State>();
    List<Prosess> processes = new ArrayList<Prosess>();
    private Limbo limbo;

    /**
     *
     * @param prosessId
     * @return the updated state of the process
     * @throws Exception
     */
    public Prosess run(String prosessId) throws Exception {
        for (Prosess process : processes) {
            if(process.prosessId.equals(prosessId)) {
                return run(process, process.state);
            }
        }
        throw new Exception("Process with ID " + prosessId + " not found in repository");
    }

    private Prosess run(Prosess process, State state) throws Exception {
        process.state = state;
        State stateAfterRunning = limbo.run(process.prosessId, state.toString());
        
        int currentStateIndex = stevList.indexOf(state);
        if(stateAfterRunning == State.FAILED )
            return process;
        else if(currentStateIndex + 1 >= stevList.size()) {
            return process;
        } else {
            return run(process, stevList.get(currentStateIndex + 1));
        }
    }

    public void createNewProsess(String prosessId) {
        processes.add(new Prosess(prosessId, stevList.get(0)));
    }

    public void addState(State state) {
        stevList.add(state);
    }

    public void observeLimbo(Limbo limbo) {
        this.limbo = limbo;
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