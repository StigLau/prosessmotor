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

import static org.mockito.Mockito.mock;

public class ProsessmotorTest {


    Tilstandsmotor tilstandsmotor = new Tilstandsmotor();
    Limbo limbo = new Limbo();
    Stev validering;
    Stev arkivering;
    Stev arkiverKvittering;

    @Before
    public void setup() {
        validering = new Validering();
        arkivering = new Arkivering(mock(SigningService.class), mock(ArchiveService.class));
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
        State result = tilstandsmotor.run("pid1");
        System.out.println(result.name);
    }
}
