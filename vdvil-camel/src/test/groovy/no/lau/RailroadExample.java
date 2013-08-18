package no.lau;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphalgo.WeightedPath;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.Traversal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static utils.GraphUtil.cleanUp;
import static utils.GraphUtil.registerShutdownHook;
import static no.lau.RailroadExample.RelTypes.*;

//Example code collected from http://www.hascode.com/2012/01/neo4j-graph-database-tutorial-how-to-build-a-route-planner-and-other-examples/

public class RailroadExample {
    private static String DB_PATH = "/tmp/neo4j";

    public enum RelTypes implements RelationshipType {
        LEADS_TO, //Deprecated
        IS_A, //Classification in the meta-model
        CLASSIFIED_AS, //Klassifisering av hva noe er; point of interest, Train Station
        ENGULFS, //For å si at dette er et videre områdebegrep enn dets mindre områder (Land, fylke, kommune osv)
        TRAVELS,
        TRIP_LEG, //A trip-leg a user will encounter
        PLANNED_TRIP, //Trips a user owns
        FROM, //Start of a planned trip
        TO, //End of a planned trip
        EMBARKS_FROM, //Where a Vehicle embarks from

    }

    GraphDatabaseService graphDb = new EmbeddedGraphDatabase(DB_PATH);

    Index<Node> nodeIndex;


    @Test
    public void TestRailroad() {
        System.out.println("searching for the shortest route from London to Bristol..");
        PathFinder<WeightedPath> finder = GraphAlgoFactory.dijkstra(Traversal.expanderForTypes(LEADS_TO, Direction.BOTH), "distance");

        WeightedPath path = finder.findSinglePath(find("London Railway Station"), find("Bristol"));
        assertEquals(198, Double.valueOf(path.weight()).intValue());
        System.out.println("London - Bristol with a distance of: " + path.weight() + " and via: ");
        for (Node n : path.nodes()) {
            System.out.print(" " + n.getProperty("name"));
        }

        System.out.println("\nsearching for the shortest route from Northampton to Brighton..");
        path = finder.findSinglePath(find("Northampton"), find("Brighton"));
        System.out.println("Northampton - Brighton with a distance of: " + path.weight() + " and via: ");
        for (Node n : path.nodes()) {
            System.out.print(" " + n.getProperty("name"));
        }
    }


    @Test
    public void testUserCreatesTravelPlanFromOsloToGx553() {
        Node oslo = find("Oslo");
        Node sk273 = find("SK273");
        System.out.println(oslo);
        //Trip trip = new Trip(graphDb, nodeIndex);

        Transaction tx = graphDb.beginTx();
        try {

            Node gunnar = createNode("User Gunnar");
            Node gunnarsBergenTrip = createNode("Gunnars trip to Bergen");
            createRelationship(gunnar, PLANNED_TRIP, gunnarsBergenTrip);
            gunnarsBergenTrip.createRelationshipTo(oslo, FROM);
            gunnarsBergenTrip.createRelationshipTo(sk273, TO);

            Relationship osloSk273 = oslo.createRelationshipTo(sk273, TRIP_LEG);


            //Point of interest to Point of interest
            //Region
            //Port / Train Station / Security / Baggage collection
            //Food Vendor, Tax Free, Kiosk, Toilet


            //Plain
            tx.success();
        } finally {
            tx.finish();
        }
    }

    private Node find(String nodeName) {
        IndexHits<Node> aNodes = nodeIndex.get("name", nodeName);
        return aNodes.getSingle();
    }


    @Before
    public void setUp() {

        registerShutdownHook(graphDb);
        nodeIndex = graphDb.index().forNodes("nodes");

        Transaction tx = graphDb.beginTx();
        try {
            cleanUp(graphDb, nodeIndex);

            Node heathrowAirport = createNode("Heathrow Airport");
            Node londonRailwayStation = createNode("London Railway Station");
            Node brighton = createNode("Brighton");
            Node portsmouth = createNode("Portsmouth");
            Node bristol = createNode("Bristol");
            Node oxford = createNode("Oxford");
            Node gloucester = createNode("Gloucester");
            Node northampton = createNode("Northampton");
            Node southampton = createNode("Southampton");

            Node oslo = createNode("Oslo");
            Node bergen = createNode("Bergen");
            Node london = createNode("London");
            final Node gardermoen = createNode("Gardermoen");
            Node oslGardermoenRailwayTerminal = createNode("OSL Railway Station");
            Node oslGardermoenBusTerminal = createNode("OSL Bus Station");
            Node oslGardermoenTaxiTerminal = createNode("OSL Taxi Station");


            createRelationship(londonRailwayStation, LEADS_TO, brighton, Collections.singletonMap("distance", 52));
            createRelationship(londonRailwayStation, LEADS_TO, heathrowAirport, Collections.singletonMap("distance", 16));
            createRelationship(brighton, LEADS_TO, portsmouth, Collections.singletonMap("distance", 49));
            createRelationship(portsmouth, LEADS_TO, southampton, Collections.singletonMap("distance", 20));
            createRelationship(londonRailwayStation, LEADS_TO, oxford, Collections.singletonMap("distance", 95));
            createRelationship(oxford, LEADS_TO, southampton, Collections.singletonMap("distance", 66));
            createRelationship(oxford, LEADS_TO, northampton, Collections.singletonMap("distance", 45));
            createRelationship(northampton, LEADS_TO, bristol, Collections.singletonMap("distance", 114));
            createRelationship(southampton, LEADS_TO, bristol, Collections.singletonMap("distance", 77));
            createRelationship(northampton, LEADS_TO, gloucester, Collections.singletonMap("distance", 106));
            createRelationship(gloucester, LEADS_TO, bristol, Collections.singletonMap("distance", 35));

            //LeadsTo used by distance evaluator
            createRelationship(oslo, LEADS_TO, london, Collections.singletonMap("distance", 716));
            Map props = new HashMap();
            props.put("distance", 716);
            props.put("travelTime", "2:20");
            createRelationship(heathrowAirport, LEADS_TO, gardermoen, props);


            //Meta data model
            Node pointOfInterest = createNode("Point of interest");
            Node trainStation = createNode("Train Station");
            Node airPort = createNode("Air Port");
            Node airPortGate = createNode("Air Port Gate");
            Node foodPoint = createNode("Food Point");
            Node city = createNode("City");
            createRelationship(city, IS_A, pointOfInterest);
            createRelationship(trainStation, IS_A, pointOfInterest);
            createRelationship(airPort, IS_A, pointOfInterest);
            createRelationship(airPortGate, IS_A, pointOfInterest);
            createRelationship(foodPoint, IS_A, pointOfInterest);


            Node methodOfTravel = createNode("Method Of Travel");
            Node airPlane = createNode("Air Plane");
            Node train = createNode("Train");

            createRelationship(airPlane, CLASSIFIED_AS, methodOfTravel);
            createRelationship(train, CLASSIFIED_AS, methodOfTravel);

            createRelationship(airPlane, EMBARKS_FROM, airPortGate);
            createRelationship(train, EMBARKS_FROM, trainStation);

            //Instances of Meta data model
            createRelationship(oslo, CLASSIFIED_AS, city);
            createRelationship(bergen, CLASSIFIED_AS, city);
            createRelationship(london, CLASSIFIED_AS, city);
            createRelationship(gardermoen, CLASSIFIED_AS, pointOfInterest);

            createRelationship(oslGardermoenRailwayTerminal, CLASSIFIED_AS, trainStation);

            createRelationship(londonRailwayStation, CLASSIFIED_AS, trainStation);
            createRelationship(brighton, CLASSIFIED_AS, trainStation);
            createRelationship(portsmouth, CLASSIFIED_AS, trainStation);
            createRelationship(oxford, CLASSIFIED_AS, trainStation);
            createRelationship(northampton, CLASSIFIED_AS, trainStation);
            createRelationship(southampton, CLASSIFIED_AS, trainStation);
            createRelationship(gloucester, CLASSIFIED_AS, trainStation);

            //Gardermoen has a airport, bus terminal train terminal aso

            createRelationship(oslo, ENGULFS, gardermoen);
            createRelationship(gardermoen, ENGULFS, oslGardermoenRailwayTerminal);
            createRelationship(gardermoen, ENGULFS, oslGardermoenBusTerminal);
            createRelationship(gardermoen, ENGULFS, oslGardermoenTaxiTerminal);


            createRelationship(london, ENGULFS, londonRailwayStation);


            //Template creation
            Node sk273 = createNode(new HashMap<String, Object>() {{
                put("name", "SK273");
                put("from", "gardermoen");
                put("to", "bergen");
                put("departure", "friday 16:15");
                put("flightNumber", "SK273");
                put("gate", 24);
            }});

            createRelationship(sk273, CLASSIFIED_AS, airPlane);
            createRelationship(sk273, TRAVELS, gardermoen);
            createRelationship(sk273, TRAVELS, bergen);

            Node g24 = createNode("Gate 24");
            g24.createRelationshipTo(airPortGate, CLASSIFIED_AS);
            gardermoen.createRelationshipTo(g24, ENGULFS);
            sk273.createRelationshipTo(g24, EMBARKS_FROM);


            tx.success();
        } finally {
            tx.finish();
        }
    }

    Relationship createRelationship(Node origin, RelTypes relationType, Node other) {
        return createRelationship(origin, relationType, other, new HashMap());
    }

    Relationship createRelationship(Node from, RelTypes relationType, Node to, Map properties) {
        Relationship relationship = from.createRelationshipTo(to, relationType);
        for (String key : ((Map<String, Object>) properties).keySet()) {
            relationship.setProperty(key, properties.get(key));
        }
        return relationship;
    }

    private Node createNode(final String ts) {
        return createNode(new HashMap<String, Object>() {{
            put("name", ts);
        }});
    }

    private Node createNode(HashMap<String, Object> properties) {
        assert (properties.containsKey("name"));
        Node node = graphDb.createNode();
        nodeIndex.add(node, "name", properties.get("name"));
        for (String key : properties.keySet()) {
            node.setProperty(key, properties.get(key));
        }
        return node;
    }

    @After
    public void shutdown() {
        graphDb.shutdown();
    }
}
