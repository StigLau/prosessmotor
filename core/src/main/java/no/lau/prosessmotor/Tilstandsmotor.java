package no.lau.prosessmotor;

import no.lau.prosessmotor.limbo.Limbo;

import java.util.ArrayList;
import java.util.List;

public class Tilstandsmotor {

    List<Steg> stevList = new ArrayList<Steg>();
    List<Prosess> processes = new ArrayList<Prosess>();
    private Limbo limbo;

    /**
     *
     * @param prosessId
     * @return the updated steg of the process
     * @throws Exception
     */
    public Prosess run(String prosessId) throws Exception {
        for (Prosess process : processes) {
            if(process.prosessId.equals(prosessId)) {
                return run(process, process.steg);
            }
        }
        throw new Exception("Process with ID " + prosessId + " not found in repository");
    }

    private Prosess run(Prosess process, Steg steg) throws Exception {
        process.steg = steg;
        Tilstand tilstandAfterRunning = limbo.run(process.prosessId, steg.toString());
        
        int currentStegIndex = stevList.indexOf(steg);
        if(tilstandAfterRunning == Tilstand.FAILED )
            return process;
        else if(currentStegIndex + 1 >= stevList.size()) {
            return process;
        } else {
            return run(process, stevList.get(currentStegIndex + 1));
        }
    }

    public void createNewProsess(String prosessId) {
        processes.add(new Prosess(prosessId, stevList.get(0)));
    }

    public void addSteg(Steg steg) {
        stevList.add(steg);
    }

    public void observeLimbo(Limbo limbo) {
        this.limbo = limbo;
    }
}



class Prosess {

    final public String prosessId;
    public Steg steg;

    public Prosess(String prosessId, Steg steg) {
        this.prosessId = prosessId;
        this.steg = steg;
    }
}