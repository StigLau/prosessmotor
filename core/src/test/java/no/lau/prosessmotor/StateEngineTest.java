package no.lau.prosessmotor;

import no.lau.prosessmotor.limbo.Limbo;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class StateEngineTest {

    Tilstandsmotor tilstandsmotor = new Tilstandsmotor();
    Steg signedContract = new Steg("ArchiveSignedContract");

    @Before
    public void setUp() {
        tilstandsmotor.addSteg(new Steg("Validering"));
        //tilstandsmotor.addSteg(new Steg("ReserveAccount"));
        //tilstandsmotor.addSteg(new Steg("ContractCreation"));
        //tilstandsmotor.addSteg(new Steg("InsertSignOrder"));
        //tilstandsmotor.addSteg(new Steg("Signing"));
        tilstandsmotor.addSteg(signedContract);
        //tilstandsmotor.addSteg(new Steg("CreateAccount"));
        //tilstandsmotor.addSteg(new Steg("WithdrawFee"));
    }

    @Test
    public void testStateMachine() throws Exception {
        tilstandsmotor.createNewProsess("100");
        tilstandsmotor.observeLimbo(mock(Limbo.class));
        Prosess tilstandsmotorResult = tilstandsmotor.run("100");
        assertEquals(signedContract, tilstandsmotorResult.steg);
    }

    @Test(expected = Exception.class)
    public void processIdDoesNotExistThrowsException() throws Exception {
        tilstandsmotor.run("DoesNotExist");
    }
}
