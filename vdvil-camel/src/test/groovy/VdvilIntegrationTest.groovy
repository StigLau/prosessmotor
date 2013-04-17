import org.apache.camel.Exchange
import org.apache.camel.LoggingLevel
import org.apache.camel.Processor
import org.apache.camel.Produce
import org.apache.camel.ProducerTemplate
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.builder.xml.XPathBuilder
import org.apache.camel.test.junit4.CamelTestSupport
import org.junit.Before
import org.junit.Test
import org.neo4j.graphdb.GraphDatabaseService
import org.neo4j.graphdb.factory.GraphDatabaseFactory

class VdvilIntegrationTest extends CamelTestSupport {

    @Produce
    ProducerTemplate wsTemplate;

    def composition = "http://kpro09.googlecode.com/svn/trunk/graph-gui-scala/src/main/resources/composition/javazone.dvl.composition.xml"
    def dbLocation = "/tmp/vdvil/db"
    GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(dbLocation);

    enum Header_Fields { FETCH_URL, SAVE_AS_FILE_NAME }
    enum Routes { FETCH_META_DATA }

    @Before
    void setup() {
        registerShutdownHook( graphDb );
    }

    @Test
    void testDoingStuff() throws Exception {

        Map myMap = [:]
        myMap[Header_Fields.FETCH_URL.toString()] = composition
        myMap[Header_Fields.SAVE_AS_FILE_NAME.toString()] = "composition.xml"
        wsTemplate.requestBodyAndHeaders("direct:${Routes.FETCH_META_DATA.toString()}", "hello", myMap)
    }



    @Override
    public RouteBuilder createRouteBuilder() throws java.lang.Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:${Routes.FETCH_META_DATA.toString()}").tracing()
                    .to("direct:downloadAndSave")
                    .to("direct:saveFile", "direct:fetchParts")
                        //.log(LoggingLevel.WARN, "Saving \$body to file")

                from("direct:saveFile")
                .setBody(header("msgPayload"))
                        .setHeader(Exchange.FILE_NAME, simple("\${header.${Header_Fields.SAVE_AS_FILE_NAME.toString()}}"))

                    .to("file:/tmp/vdvil")

                /*
                .process(new Neo4JStore(graphDb: graphDb,
                        command: Neo4JStore.Command.setProperty,
                        key: Header_Fields.FETCH_URL.name(),
                        value: composition))
                        */
                //        .to("stream:out");
                from("direct:fetchParts")
                .setBody(header("msgPayload"))
                .split().tokenizeXML("part").streaming()

                    //.simple('$header[msgPayload]').split(new XPathBuilder("//composition/parts/part"))
                        .to("stream:out")

                from("direct:downloadAndSave")
                        .setHeader(Exchange.HTTP_URI, simple("\${header.${Header_Fields.FETCH_URL.toString()}}"))
                        .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                        .to("http://to_be_replaced_by_header_values")
                        .setHeader("msgPayload", body())

                /*
                .choice()
    .when(header("CamelFileName")
    .endsWith(".xml"))
        .to("jms:xmlOrders")
    .when(header("CamelFileName")
    .endsWith(".csv"))
        .to("jms:csvOrders");
                 */
            }
        };
    }
    static void registerShutdownHook( final GraphDatabaseService graphDb ) {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running example before it's completed)
        Runtime.getRuntime().addShutdownHook( new Thread() {
            @Override
            public void run() { graphDb.shutdown(); }
        } );
    }
}

