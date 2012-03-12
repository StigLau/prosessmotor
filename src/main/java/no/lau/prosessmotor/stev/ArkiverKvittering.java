package no.lau.prosessmotor.stev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;

/**
 * Pretty prints a receipt of the accumulated context and displays it on logger stream
 */
public class ArkiverKvittering implements Stev{

    Logger logger = LoggerFactory.getLogger(ArkiverKvittering.class);

    @Override
    public Map<String, String> run(Map<String, String> context) throws Exception {
        logger.info("-------------Printing receipt of finished process {}----------", context.get("processId"));
        for (String key : context.keySet()) {
            logger.info("-- {} - {} ", key, context.get(key));
        }
        logger.info("-------------End receipt ----------");
        return Collections.emptyMap();
    }
}
