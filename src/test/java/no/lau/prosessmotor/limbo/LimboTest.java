package no.lau.prosessmotor.limbo;

import no.lau.prosessmotor.stev.Stev;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class LimboTest {
    Limbo limbo = new Limbo();

    @Before
    public void setup() {
        limbo.addStev("Stev 1", mock(Stev.class));
        limbo.addStev("Stev 2", mock(Stev.class));
    }
    
    @Test
    public void runningLimboWithOnlyValidation() throws Exception {
        String processId = "123";
        limbo.updateState(processId, "mock state", "hello");
        Map<String, String> result = limbo.run(processId, "Stev 1");
        assertEquals("ok", result.get("Stev 1 endState"));
    }

    @Test(expected = StevNotFoundException.class)
    public void stevNotFoundThrowsExcption() throws Exception{
        limbo.run("123", "Non-existant Stev");
    }

    @Test(expected = NoSuchFieldException.class)
    public void processIdNotFoundThrowsException() throws Exception{
        limbo.run("Does not exist", "Stev 1");
    }

    @Test
    public void runningBothValideringAndArkivering() throws Exception {
        String processId = "123";
        limbo.updateState(processId, "some state", "hello");
        limbo.run(processId, "Stev 1");
        Map<String, String> result = limbo.run(processId, "Stev 2");
        assertEquals("ok", result.get("Stev 1 endState"));
        assertEquals("ok", result.get("Stev 2 endState"));
    }
}
