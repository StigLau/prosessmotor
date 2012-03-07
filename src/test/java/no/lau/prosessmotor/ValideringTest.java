package no.lau.prosessmotor;

import no.lau.prosessmotor.stev.Validering;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;

public class ValideringTest {
    Validering validering = new Validering();

    @Test
    public void testValidering() throws Exception {
        Map<String, String> context = new HashMap<String, String>();
        context.put("processId", "123");
        context.put("fnr", "12345678901");
        Map result = validering.run(context);
        assertEquals("ok", result.get("Validering endState"));
        System.out.println("Validering timestamp " + result.get("Validering timestamp"));
    }
}
