package no.lau.prosessmotor.stev;

import no.lau.prosessmotor.BestillLaanConstants;
import no.lau.prosessmotor.services.ContractService;
import no.lau.prosessmotor.services.SigningService;

import java.util.Collections;
import java.util.Map;

/**
 * Generates the contract to be signed over and inserts the contract into the signing engine
 */
public class BestillSignering implements Stev {

    ContractService contractService;
    SigningService signingService;

    public BestillSignering(ContractService contractService, SigningService signingService) {
        this.contractService = contractService;
        this.signingService = signingService;
    }

    @Override
    public Map<String, String> run(Map<String, String> context) throws Exception {
        String processId = context.get(BestillLaanConstants.PROCESS_ID);
        String fnr = context.get(BestillLaanConstants.FNR);
        String fullName = context.get(BestillLaanConstants.FULLT_NAVN);
        String loanAmount = context.get(BestillLaanConstants.LAANE_BELOP);
        String accountNr = context.get(BestillLaanConstants.KONTO_NR);
        String contractType = context.get(BestillLaanConstants.LOAN_CONTRACT_TYPE);
        
        String contract = contractService.generateContract(contractType, fnr, fullName, loanAmount, accountNr);
        
        String ourContractReference = processId;
        
        signingService.insertSignOrder(ourContractReference, contract);
        
        return Collections.singletonMap(BestillLaanConstants.SIGN_ID, ourContractReference);
    }
}
