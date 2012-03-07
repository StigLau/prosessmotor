package no.lau.prosessmotor;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProsessmotorTest {

    Tilstandsmotor tilstandsmotor = new Tilstandsmotor();
    
    @Before
    public void setUp() {
        tilstandsmotor.addState(new State("InputValidation"));
        tilstandsmotor.addState(new State("ReserveAccount"));
        tilstandsmotor.addState(new State("ContractCreation"));
        tilstandsmotor.addState(new State("InsertSignOrder"));
        tilstandsmotor.addState(new State("Signing"));
        tilstandsmotor.addState(new State("ArchiveSignedContract"));
        tilstandsmotor.addState(new State("CreateAccount"));
        tilstandsmotor.addState(new State("WithdrawFee"));
    }

    @Test
    public void testStateMachine() throws Exception {
        tilstandsmotor.createNewProsess("100");
        State tilstandsmotorResult = tilstandsmotor.run("100");
        assertEquals("InputValidation", tilstandsmotorResult.name);
    }

    @Test(expected = Exception.class)
    public void processIdDoesNotExistThrowsException() throws Exception {
        tilstandsmotor.run("DoesNotExist");
    }
}
