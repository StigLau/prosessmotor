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
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static utils.GraphUtil.cleanUp;
import static utils.GraphUtil.registerShutdownHook;

//Example code collected from http://www.hascode.com/2012/01/neo4j-graph-database-tutorial-how-to-build-a-route-planner-and-other-examples/

public class RailroadExample {
    private static String DB_PATH = "/tmp/neo4j";

    public enum RelTypes implements RelationshipType {
        LEADS_TO, STATION

    }
    GraphDatabaseService graphDb = new EmbeddedGraphDatabase(DB_PATH);

    Index<Node> nodeIndex;


    @Test
    public void TestRailroad() {
        System.out.println("searching for the shortest route from London to Bristol..");
        PathFinder<WeightedPath> finder = GraphAlgoFactory.dijkstra(Traversal.expanderForTypes(RelTypes.LEADS_TO, Direction.BOTH), "distance");

        WeightedPath path = finder.findSinglePath(find("London"), find("Bristol"));
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

            Node londonNode = createNode("London");
            Node brightonNode = createNode("Brighton");
            Node portsmouthNode = createNode("Portsmouth");
            Node bristolNode = createNode("Bristol");
            Node oxfordNode = createNode("Oxford");
            Node gloucesterNode = createNode("Gloucester");
            Node northamptonNode = createNode("Northampton");
            Node southamptonNode = createNode("Southampton");


            createRelationship(londonNode, brightonNode, Collections.singletonMap("distance", 52), RelTypes.LEADS_TO);
            createRelationship(brightonNode, portsmouthNode, Collections.singletonMap("distance", 49), RelTypes.LEADS_TO);
            createRelationship(portsmouthNode, southamptonNode, Collections.singletonMap("distance", 20), RelTypes.LEADS_TO);
            createRelationship(londonNode, oxfordNode, Collections.singletonMap("distance", 95), RelTypes.LEADS_TO);
            createRelationship(oxfordNode, southamptonNode, Collections.singletonMap("distance", 66), RelTypes.LEADS_TO);
            createRelationship(oxfordNode, northamptonNode, Collections.singletonMap("distance", 45), RelTypes.LEADS_TO);
            createRelationship(northamptonNode, bristolNode, Collections.singletonMap("distance", 114), RelTypes.LEADS_TO);
            createRelationship(southamptonNode, bristolNode, Collections.singletonMap("distance", 77), RelTypes.LEADS_TO);
            createRelationship(northamptonNode, gloucesterNode, Collections.singletonMap("distance", 106), RelTypes.LEADS_TO);
            createRelationship(gloucesterNode, bristolNode, Collections.singletonMap("distance", 35), RelTypes.LEADS_TO);

            Node trainStation = createNode("Train Station");
            Node foodPoint = createNode("Food Point");



            tx.success();
        } finally {
            tx.finish();
        }
    }

    Relationship createRelationship(Node from, Node to, Map properties, RelTypes relationType) {
        Relationship relationship = from.createRelationshipTo(to, relationType);
        for (String key : ((Map<String, Object>)properties).keySet()) {
            relationship.setProperty(key, properties.get(key));
        }
        return relationship;
    }

    private Node createNode(String ts) {
        Node node = graphDb.createNode();
        node.setProperty("name", ts);
        nodeIndex.add(node, "name", ts);
        return node;
    }

    @After
    public void shutdown() {
        graphDb.shutdown();
    }
}