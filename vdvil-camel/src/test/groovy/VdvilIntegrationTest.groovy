import org.apache.camel.Exchange
import org.apache.camel.Produce
import org.apache.camel.ProducerTemplate
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.test.junit4.CamelTestSupport
import org.junit.Test

class VdvilIntegrationTest extends CamelTestSupport {

    @Produce
    ProducerTemplate wsTemplate;

    def mp3Something = "http://kpro09.googlecode.com/svn/trunk/graph-gui-scala/src/main/resources/composition/javazone.dvl.composition.xml"
    enum Header_Fields {
        FETCH_URL, SAVE_AS_FILE_NAME
    }
    enum Routes {
        FETCH_META_DATA
    }

    @Test
    void testgetYesterDaysWeather() throws Exception {

        Map myMap = [:]
        myMap[Header_Fields.FETCH_URL.toString()] = mp3Something
        myMap[Header_Fields.SAVE_AS_FILE_NAME.toString()] = "hello.xml"
        wsTemplate.requestBodyAndHeaders("direct:${Routes.FETCH_META_DATA.toString()}", "hello", myMap)

    }


    @Override
    public RouteBuilder createRouteBuilder() throws java.lang.Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:${Routes.FETCH_META_DATA.toString()}")
                .setHeader(Exchange.HTTP_URI, simple("\${header.${Header_Fields.FETCH_URL.toString()}}"))

                        .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                        .to("http://to_be_replaced_by_header_values")

                .setHeader(Exchange.FILE_NAME, simple("\${header.${Header_Fields.SAVE_AS_FILE_NAME.toString()}}"))
                .to("file:/tmp/vdvil");
                //        .to("stream:out");
            }
        };
    }
}