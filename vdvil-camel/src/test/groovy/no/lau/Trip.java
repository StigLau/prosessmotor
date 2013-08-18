package no.lau;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;

import java.util.ArrayList;
import java.util.List;

public class Trip {
    GraphDatabaseService graphDb;
    Index<Node> nodeIndex;

    List<Node> legs = new ArrayList<Node>();
    
    Trip(GraphDatabaseService graphDb, Index<Node> nodeIndex) {
        this.graphDb = graphDb;
        this.nodeIndex = nodeIndex;
    }

    public void addLeg(Node from, Node to) {
        this.legs.add(from);
        this.legs.add(to);
    }
}
