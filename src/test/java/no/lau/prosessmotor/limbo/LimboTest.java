package no.lau.prosessmotor.limbo;

import no.lau.prosessmotor.State;
import no.lau.prosessmotor.stev.Stev;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
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
        assertEquals(State.OK, limbo.run(processId, stev1.toString()));
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
        assertEquals(State.OK , limbo.run(processId, stev2.toString()));
    }
}
