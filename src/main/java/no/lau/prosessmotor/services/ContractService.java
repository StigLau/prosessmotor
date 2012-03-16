package no.lau.prosessmotor.services;

public interface ContractService {
    String generateContract(String contractType, String fnr, String fullName, String loanAmount, String accountNr);
}
