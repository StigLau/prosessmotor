package no.lau.prosessmotor.stev;

import org.joda.time.DateTime;
import java.util.Map;

public class Validering {

    public Map<String, String> run(Map<String, String> context) throws Exception {
        if(!context.containsKey("processId"))
            throw new Exception("processId not set");
        else if(!context.containsKey("fnr") || context.get("fnr").length() != 11)
            throw new Exception("fnr ikke satt eller gyldig lengde");
        else {
            String className = this.getClass().getSimpleName();
            context.put(className + " endState", "ok");
            context.put(className + " timestamp", new DateTime().toString());
            return context;
        }
    }
}
