package no.lau.prosessmotor;

import no.lau.prosessmotor.limbo.Limbo;
import no.lau.prosessmotor.services.ArchiveService;
import no.lau.prosessmotor.services.SigningService;
import no.lau.prosessmotor.stev.ArkiverKvittering;
import no.lau.prosessmotor.stev.Arkivering;
import no.lau.prosessmotor.stev.Stev;
import no.lau.prosessmotor.stev.Validering;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProsessmotorTest {


    Tilstandsmotor tilstandsmotor = new Tilstandsmotor();
    Limbo limbo = new Limbo();
    Stev validering;
    Stev arkivering;
    Stev arkiverKvittering;
    ArchiveService archiveService = mock(ArchiveService.class);

    @Before
    public void setup() {
        validering = new Validering();
        arkivering = new Arkivering(mock(SigningService.class), archiveService);
        arkiverKvittering = new ArkiverKvittering();
        limbo.addStev(validering);
        limbo.addStev(arkivering);
        limbo.addStev(arkiverKvittering);
        tilstandsmotor.observeLimbo(limbo);
    
        tilstandsmotor.addState(new State(validering));
        //tilstandsmotor.addState(new State("ReserveAccount"));
        //tilstandsmotor.addState(new State("ContractCreation"));
        //tilstandsmotor.addState(new State("InsertSignOrder"));
        //tilstandsmotor.addState(new State("Signing"));
        tilstandsmotor.addState(new State(arkivering));
        //tilstandsmotor.addState(new State("CreateAccount"));
        //tilstandsmotor.addState(new State("WithdrawFee"));
        tilstandsmotor.addState(new State(arkiverKvittering));
    }
    
    @Test
    public void runHappyDay() throws Exception {
        tilstandsmotor.createNewProsess("pid1");
        limbo.updateState("pid1", "fnr", "12345678901");
        Prosess result = tilstandsmotor.run("pid1");
        System.out.println(result.state.name);
    }
    
    @Test
    public void testRetryAfterArchivingFails() throws Exception {
        String processId = "pid2";
        tilstandsmotor.createNewProsess(processId);
        limbo.updateState(processId, "fnr", "12345678901");
        
        when(archiveService.archive(anyString())).thenThrow(new RuntimeException("ArchiveService is having a bad day, try again later")).thenReturn("archiveId123");
        assertEquals(arkivering.toString(), tilstandsmotor.run(processId).state.name);
        //Run a second time
        assertEquals(arkiverKvittering.toString(), tilstandsmotor.run(processId).state.name);
        assertEquals(4, limbo.getHistory(processId).size());
        assertEquals(validering + " ok", limbo.getHistory(processId).get(0).stepName + " " + limbo.getHistory(processId).get(0).state);
        assertEquals(arkivering + " failed; ArchiveService is having a bad day, try again later", limbo.getHistory(processId).get(1).stepName + " " + limbo.getHistory(processId).get(1).state);
        assertEquals(arkivering + " ok", limbo.getHistory(processId).get(2).stepName + " " + limbo.getHistory(processId).get(2).state);
        assertEquals(arkiverKvittering + " ok", limbo.getHistory(processId).get(3).stepName + " " + limbo.getHistory(processId).get(3).state);
    }
}
