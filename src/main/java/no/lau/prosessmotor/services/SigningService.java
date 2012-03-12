package no.lau.prosessmotor.services;

public interface SigningService {

    public String fetchSignedDocument(String processId) throws Exception;

    public static final String SIGNING_ID = "SignedDocumentId";
}
