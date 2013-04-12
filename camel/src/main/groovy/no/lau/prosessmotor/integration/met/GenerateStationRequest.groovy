package no.lau.prosessmotor.integration.met

import org.apache.camel.Exchange
import org.apache.camel.Processor

class GenerateStationRequest implements Processor {
    void process(Exchange exchange) throws Exception {
        exchange.getIn().setBody(new WSRequestGenerator(
                method: "getStationsFromTimeserieTypeElemCodes",
                timeserietypeID: 2,
                elements: ["FF", "DD", "RR", "RA", "TA"]
        ).toString())
    }
}