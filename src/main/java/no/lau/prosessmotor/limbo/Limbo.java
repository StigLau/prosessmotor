package no.lau.prosessmotor.limbo;

import no.lau.prosessmotor.Tilstand;
import no.lau.prosessmotor.stev.Stev;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Limbo {

    private Map<String, Stev> stevMap = new HashMap<String, Stev>();
    private Map<String, Map<String, String>> prosesserOgDeresTilstand = new HashMap<String, Map<String, String>>();
    private Map<String, List<History>> histories = new HashMap<String, List<History>>();
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public Tilstand run(String processId, String stepName) throws Exception {
        if(!stevMap.containsKey(stepName))
            throw new StevNotFoundException(stepName);
        if(!prosesserOgDeresTilstand.containsKey(processId))
            throw new NoSuchFieldException("ProcessId " + processId);

        Stev stev = stevMap.get(stepName);
        Map<String, String> context = prosesserOgDeresTilstand.get(processId);

        try {
            Map<String, String> resultingStevContext = stev.run(context);
            context.putAll(resultingStevContext);
            histories.get(processId).add(new History(stepName, "ok", new DateTime()));
            logger.info(timestamp() + " - Limbo: " + stepName + " finished successfully");
            return Tilstand.OK;
        } catch (Exception e) {
            logger.error("Experienced a problem", e);
            histories.get(processId).add(new History(stepName, "failed; " + e.getMessage(), new DateTime()));
            return Tilstand.FAILED;
        }
    }

    public void addStev(Stev stev) {
        stevMap.put(stev.toString(), stev);
    }

    public void updateSteg(String processId, String key, String value) {
        if(!histories.containsKey(processId))
            histories.put(processId, new ArrayList<History>());
        if(!prosesserOgDeresTilstand.containsKey(processId))
            prosesserOgDeresTilstand.put(processId, new HashMap<String, String>());
        prosesserOgDeresTilstand.get(processId).put(key, value);
    }

    public List<History> getHistory(String processId) {
        return histories.get(processId);
    }

    public String retrieveState(String processId, String key) {
        return prosesserOgDeresTilstand.get(processId).get(key);
    }

    private String timestamp() {
        return new DateTime().toString("dd.MM.yyyy HH:mm:ss");
    }
}

