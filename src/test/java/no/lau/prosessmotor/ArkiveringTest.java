package no.lau.prosessmotor;

import no.lau.prosessmotor.services.ArchiveService;
import no.lau.prosessmotor.services.SigningService;
import no.lau.prosessmotor.stev.Arkivering;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ArkiveringTest {

    SigningService signingService = mock(SigningService.class);
    ArchiveService archiveService = mock(ArchiveService.class);
    Arkivering arkivering = new Arkivering(signingService, archiveService);

    @Test
    public void runningArkivering() throws Exception {
        String processId = "testId 5";
        String mockDocument = "mock XML Document";
        String mockArchiveId = "123";

        Map<String, String> context = new HashMap<String, String>();
        context.put("processId", processId);

        when(signingService.fetchSignedDocument(processId)).thenReturn(mockDocument);
        when(archiveService.archive(mockDocument)).thenReturn(mockArchiveId);
        


        Map result = arkivering.run(context);
        assertEquals("ok", result.get("endState"));
        assertEquals(mockArchiveId, result.get("documentReferenceInArchive"));
    }
}
