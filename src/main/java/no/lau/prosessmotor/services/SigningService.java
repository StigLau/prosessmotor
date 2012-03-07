package no.lau.prosessmotor.services;

public interface SigningService {

    public String fetchSignedDocument(String processId) throws Exception;
}
