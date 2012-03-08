package no.lau.prosessmotor.stev;

import java.util.Map;

public interface Stev {
    public Map<String, String> run(Map<String, String> context) throws Exception;
}
