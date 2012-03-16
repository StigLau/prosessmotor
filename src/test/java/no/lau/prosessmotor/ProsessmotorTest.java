package no.lau.prosessmotor;

import no.lau.prosessmotor.limbo.Limbo;
import no.lau.prosessmotor.services.ArchiveService;
import no.lau.prosessmotor.services.ContractService;
import no.lau.prosessmotor.services.SigningService;
import no.lau.prosessmotor.stev.*;
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
    Stev bestillSignering;
    ContractService contractService = mock(ContractService.class);
    ArchiveService archiveService = mock(ArchiveService.class);
    SigningService signingService = mock(SigningService.class);

    @Before
    public void setup() {
        validering = new Validering();
        bestillSignering = new BestillSignering(contractService, signingService);
        arkivering = new Arkivering(signingService, archiveService);
        arkiverKvittering = new ArkiverKvittering();
        //This sets up the flow in the state machine
        limbo.addStev(validering);
        limbo.addStev(bestillSignering);
        limbo.addStev(arkivering);
        limbo.addStev(arkiverKvittering);

        tilstandsmotor.observeLimbo(limbo);
    
        tilstandsmotor.addSteg(new Steg(validering));
        //tilstandsmotor.addSteg(new Steg("ReserveAccount"));
        tilstandsmotor.addSteg(new Steg(bestillSignering));
        //tilstandsmotor.addSteg(new Steg("Signing"));
        tilstandsmotor.addSteg(new Steg(arkivering));
        //tilstandsmotor.addSteg(new Steg("CreateAccount"));
        //tilstandsmotor.addSteg(new Steg("WithdrawFee"));
        tilstandsmotor.addSteg(new Steg(arkiverKvittering));
    }
    
    @Test
    public void runHappyDay() throws Exception {
        tilstandsmotor.createNewProsess("pid1");
        limbo.updateSteg("pid1", "fnr", "12345678901");
        Prosess result = tilstandsmotor.run("pid1");
        System.out.println(result.steg.name);
    }
    
    @Test
    public void testRetryAfterArchivingFails() throws Exception {
        String processId = "pid2";
        tilstandsmotor.createNewProsess(processId);
        limbo.updateSteg(processId, "fnr", "12345678901");
        
        when(archiveService.archive(anyString())).thenThrow(new RuntimeException("ArchiveService is having a bad day, try again later")).thenReturn("archiveId123");
        assertEquals(arkivering.toString(), tilstandsmotor.run(processId).steg.name);
        //Run a second time
        assertEquals(arkiverKvittering.toString(), tilstandsmotor.run(processId).steg.name);
        assertEquals(5, limbo.getHistory(processId).size());
        int i = 0;
        assertEquals(validering + " ok", resultString(processId, i++));
        assertEquals(bestillSignering + " ok", resultString(processId, i++));
        assertEquals(arkivering + " failed; ArchiveService is having a bad day, try again later", resultString(processId, i++));
        assertEquals(arkivering + " ok", resultString(processId, i++));
        assertEquals(arkiverKvittering + " ok", resultString(processId, i++));
    }

    private String resultString(String processId, int i) {
        return limbo.getHistory(processId).get(i).stepName + " " + limbo.getHistory(processId).get(i).state;
    }
}
