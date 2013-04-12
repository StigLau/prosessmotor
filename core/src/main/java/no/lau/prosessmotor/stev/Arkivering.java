package no.lau.prosessmotor.stev;

import no.lau.prosessmotor.BestillLaanConstants;
import no.lau.prosessmotor.services.ArchiveService;
import no.lau.prosessmotor.services.SigningService;

import java.util.Map;

public class Arkivering implements Stev {

    SigningService signingService;
    ArchiveService archiveService;

    public Arkivering(SigningService signingService, ArchiveService archiveService) {
        this.signingService = signingService;
        this.archiveService = archiveService;
    }

    public Map<String, String> run(Map<String, String> context) throws Exception {
        String signingId = context.get(BestillLaanConstants.SIGN_ID);

        String xmlDocument = signingService.fetchSignedDocument(signingId);
        String documentReference = archiveService.archive(xmlDocument);
        context.put("documentReferenceInArchive", documentReference);
        return context;
    }
}

