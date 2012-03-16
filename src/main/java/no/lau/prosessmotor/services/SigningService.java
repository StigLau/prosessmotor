package no.lau.prosessmotor.services;

import java.net.URL;

public interface SigningService {

    String fetchSignedDocument(String processId) throws Exception;

    void insertSignOrder(String signOrderReference, String contract);
    
    URL getSignURL(String signOrderReference);
}
