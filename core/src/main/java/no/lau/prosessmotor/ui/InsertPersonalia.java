package no.lau.prosessmotor.ui;

import static no.lau.prosessmotor.BestillLaanConstants.*;
import no.lau.prosessmotor.limbo.Limbo;

import java.util.Random;

public class InsertPersonalia {

    public Limbo limbo;
    String name;
    String fnr;
    String email;

    public String submit() {
        String processId = generateRandom();
        limbo.updateSteg(processId, FULLT_NAVN, name);
        limbo.updateSteg(processId, FNR, fnr);
        limbo.updateSteg(processId, EMAIL, email);
        return processId;
    }

    private String generateRandom() {
        return String.valueOf(new Random().nextInt());
    }
}
