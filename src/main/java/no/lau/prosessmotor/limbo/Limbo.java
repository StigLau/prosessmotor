package no.lau.prosessmotor.limbo;

import no.lau.prosessmotor.stev.Stev;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

public class Limbo {

    private Map<String, Stev> stevMap = new HashMap<String, Stev>();
    private Map<String, Map<String, String>> prosesserOgDeresTilstand = new HashMap<String, Map<String, String>>();

    public Map<String, String> run(String processId, String stepName) throws Exception {
        if(!stevMap.containsKey(stepName))
            throw new StevNotFoundException(stepName);
        if(!prosesserOgDeresTilstand.containsKey(processId))
            throw new NoSuchFieldException("ProcessId " + processId);

        Stev stev = stevMap.get(stepName);
        Map<String, String> context = prosesserOgDeresTilstand.get(processId);

        try {
            Map<String, String> resultingStevContext = stev.run(context);
            context.putAll(resultingStevContext);
            context.put(stepName + " endState", "ok");
            String endTimeStamp = new DateTime().toString("dd.MM.yyyy HH:mm:ss");
            context.put(stepName + " timestamp", endTimeStamp);
            System.out.println(endTimeStamp + " - Limbo: " + stepName + " finished successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return context;
    }
    
    public void addStev(Stev stev) {
        stevMap.put(stev.toString(), stev);
    }

    public void updateState(String processId, String key, String value) {
        if(!prosesserOgDeresTilstand.containsKey(processId))
            prosesserOgDeresTilstand.put(processId, new HashMap<String, String>());
        prosesserOgDeresTilstand.get(processId).put(key, value);
    }
}
