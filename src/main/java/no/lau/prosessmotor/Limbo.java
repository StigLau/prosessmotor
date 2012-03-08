package no.lau.prosessmotor;

import no.lau.prosessmotor.stev.Stev;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

public class Limbo {

    Map<String, Stev> stevMap = new HashMap<String, Stev>();
    Map<String, Map<String, String>> limboPersistens = new HashMap<String, Map<String, String>>(); 

    public Map<String, String> run(String processId, String stepName) {
        Stev stev = stevMap.get(stepName);
        Map<String, String> context = limboPersistens.get(processId);
        try {
            Map<String, String> resultingContext = stev.run(context);
            String className = stev.getClass().getSimpleName();
            context.put(className + " endState", "ok");
            context.put(className + " timestamp", new DateTime().toString("DD.MM.YYYY"));
            limboPersistens.put(processId, resultingContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return limboPersistens.get(processId);
    }
    
    void addStev(String navn, Stev stev) {
        stevMap.put(navn, stev);
    }

    public void updateState(String processId, String key, String value) {
        if(!limboPersistens.containsKey(processId))
            limboPersistens.put(processId, new HashMap<String, String>());
        limboPersistens.get(processId).put(key, value);
    }
}
