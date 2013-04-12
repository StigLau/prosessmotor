package no.lau.prosessmotor.services;

public interface ArchiveService{
    /**
     * Note that the ArchiveService can be very unstable and throw exceptions
     * @param document
     * @return archivedDocumentId
     * @throws Exception
     */
    public String archive(String document) throws Exception;
}