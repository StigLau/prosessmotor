package no.lau;

import org.apache.commons.io.FileUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphalgo.WeightedPath;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.Traversal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static utils.GraphUtil.cleanUp;
import static utils.GraphUtil.registerShutdownHook;
import static no.lau.DataBase.RelTypes.*;

//Example code collected from http://www.hascode.com/2012/01/neo4j-graph-database-tutorial-how-to-build-a-route-planner-and-other-examples/

public class RailroadExample {
    private static String DB_PATH = "target/neo4j";


    GraphDatabaseService graphDb = new EmbeddedGraphDatabase(DB_PATH);
    Index<Node> nodeIndex;
    ExecutionEngine engine = new ExecutionEngine(graphDb);
    DataBase db;

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
        //Trip trip = new Trip(graphDb, nodeIndex);

        Transaction tx = graphDb.beginTx();
        try {
            final DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");

            Node gunnar = db.createNode("User Gunnar");
            Node gunnarsBergenTrip = db.createNode("Gunnars trip to Bergen");
            db.createRelationship(gunnar, PLANNED_TRIP, gunnarsBergenTrip);
            gunnarsBergenTrip.createRelationshipTo(oslo, FROM);
            gunnarsBergenTrip.createRelationshipTo(sk273, TO);

            Relationship osloSk273 = oslo.createRelationshipTo(sk273, TRIP_LEG);
            //Lets refine this trip
            Node g24 = find("Gate 24");
            Node security = find("Gardermoen Security");
            Node gardermoenRailwayStation = find("OSL Railway Station");
            Node osloRailwayStation = find("Oslo Railway Station");

            sk273.createRelationshipTo(g24, TRIP_LEG);
            db.createRelationship(g24, TRIP_LEG, security, new HashMap() {{
                put("finished", "16:12");
            }});
            g24.createRelationshipTo(security, TRIP_LEG);
            security.createRelationshipTo(gardermoenRailwayStation, TRIP_LEG);
            osloRailwayStation.createRelationshipTo(security, TRIP_LEG);

            osloSk273.delete();


            calculateRouteTimes(oslo, sk273);

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




    private void calculateRouteTimes(Node from, Node to) {

        //ExecutionResult result = engine.execute("start n=node(*) where n.name! = 'Oslo' return n, n.name");
        ExecutionResult result = engine.execute(
                "START me=node(*)\n" +
                        "MATCH me-->friend-[:TRIP_LEG]->friend_of_friend\n" +
                        "WHERE me.name='Oslo'" +
                        "RETURN me, friend, friend_of_friend");
        System.out.println(result.dumpToString());
    }

    private Node find(String nodeName) {
        IndexHits<Node> aNodes = nodeIndex.get("name", nodeName);
        return aNodes.getSingle();
    }



    @Before
    public void setUp() {
        registerShutdownHook(graphDb);
        nodeIndex = graphDb.index().forNodes("nodes");
        db = new DataBase(graphDb, nodeIndex);


        Transaction tx = graphDb.beginTx();
        try {
            cleanUp(graphDb, nodeIndex);
            db.createDataset();
            tx.success();
        } finally {
            tx.finish();
        }
    }


    @After
    public void shutdown() {
        graphDb.shutdown();
    }
}
