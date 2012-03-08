package no.lau.prosessmotor;

import no.lau.prosessmotor.services.ArchiveService;
import no.lau.prosessmotor.services.SigningService;
import no.lau.prosessmotor.stev.Arkivering;
import no.lau.prosessmotor.stev.Validering;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class LimboTest {
    Limbo limbo = new Limbo();

    @Before
    public void setup() {
        limbo.addStev("Validering", new Validering());
        limbo.addStev("Arkivering", new Arkivering(mock(SigningService.class), mock(ArchiveService.class)));
    }
    
    @Test
    public void runningLimboWithOnlyValidation() {
        String processId = "123";

        limbo.updateState(processId, "processId", processId);
        limbo.updateState(processId, "fnr", "12345678901");

        Map<String, String> result = limbo.run(processId, "Validering");
        assertEquals("ok", result.get("Validering endState"));
    }

    @Test
    public void runningBothValideringAndArkivering() {
        String processId = "123";

        limbo.updateState(processId, "processId", processId);
        limbo.updateState(processId, "fnr", "12345678901");

    }
}
