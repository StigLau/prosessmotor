package no.lau.prosessmotor.ui;

import no.lau.prosessmotor.BestillLaanConstants;
import no.lau.prosessmotor.limbo.Limbo;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InputLaanBestillingTest {
    @Test
    public void testSimpleCustomer() {
        InsertPersonalia personalia = new InsertPersonalia();
        personalia.limbo = new Limbo();
        personalia.name = "Johnny Cash";
        personalia.fnr = "12345678901";
        personalia.email = "johnny@cash.com";
        String generatedProcessId = personalia.submit();
        assertEquals("Johnny Cash", personalia.limbo.retrieveState(generatedProcessId, BestillLaanConstants.FULLT_NAVN));
        assertEquals("12345678901", personalia.limbo.retrieveState(generatedProcessId, BestillLaanConstants.FNR));
        assertEquals("johnny@cash.com", personalia.limbo.retrieveState(generatedProcessId, BestillLaanConstants.EMAIL));
    }
}
