package no.lau.prosessmotor.stev;

import no.lau.prosessmotor.services.ArchiveService;
import no.lau.prosessmotor.services.SigningService;

import java.util.Map;

public class Arkivering {

    SigningService signingService;
    ArchiveService archiveService;

    public Arkivering(SigningService signingService, ArchiveService archiveService) {
        this.signingService = signingService;
        this.archiveService = archiveService;
    }

    public Map run(Map<String, String> context) throws Exception {
        String processId = context.get("processId");

        String xmlDocument = signingService.fetchSignedDocument(processId);
        String documentReference = archiveService.archive(xmlDocument);
        context.put("documentReferenceInArchive", documentReference);
        context.put("endState", "ok");
        return context;
    }
}

