package no.lau.prosessmotor;

import no.lau.prosessmotor.integration.met.GenerateStationRequest;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.util.Date;

public class MetIntegrationTest extends CamelTestSupport {

    @Produce
    protected ProducerTemplate wsTemplate;

    @Test
    public void testgetYesterDaysWeather() throws Exception {
        wsTemplate.requestBodyAndHeader("direct:stations", "", "date", new Date());
    }


    @Override
    public RouteBuilder createRouteBuilder() throws java.lang.Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:stations")
                        .process(new GenerateStationRequest())
                        .to("spring-ws:http://eklima.met.no/met/MetService").unmarshal().string()
                        //.to("stream:file?fileName=/tmp/stations.xml")
                        .to("stream:out");
            }
        };
    }
}