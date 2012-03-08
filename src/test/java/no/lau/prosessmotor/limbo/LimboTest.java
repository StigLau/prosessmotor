package no.lau.prosessmotor.limbo;

import no.lau.prosessmotor.stev.Stev;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class LimboTest {
    Limbo limbo = new Limbo();
    Stev stev1 = mock(Stev.class);
    Stev stev2 = mock(Stev.class);

    @Before
    public void setup() {
        limbo.addStev(stev1);
        limbo.addStev(stev2);
    }
    
    @Test
    public void runningLimboWithOnlyValidation() throws Exception {
        String processId = "123";
        limbo.updateState(processId, "mock state", "hello");
        Map<String, String> result = limbo.run(processId, stev1.toString());
        assertEquals("ok", result.get(stev1 + " endState"));
        assertNotNull(result.get(stev1 + " timestamp"));
    }

    @Test(expected = StevNotFoundException.class)
    public void stevNotFoundThrowsExcption() throws Exception{
        limbo.run("123", "Non-existant Stev");
    }

    @Test(expected = NoSuchFieldException.class)
    public void processIdNotFoundThrowsException() throws Exception{
        limbo.run("Does not exist", stev1.toString());
    }

    @Test
    public void runningBothValideringAndArkivering() throws Exception {
        String processId = "123";
        limbo.updateState(processId, "some state", "hello");
        limbo.run(processId, stev1.toString());
        Map<String, String> result = limbo.run(processId, stev2.toString());
        assertEquals("ok", result.get(stev1 + " endState"));
        assertEquals("ok", result.get(stev2 + " endState"));
        System.out.println("Timestamp " + stev1 + result.get(stev1 + " timestamp"));
        System.out.println("Timestamp " + stev2 + result.get(stev2 + " timestamp"));
    }
}
