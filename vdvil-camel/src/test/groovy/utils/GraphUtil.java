package utils;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;

public class GraphUtil {
    public static void cleanUp(final GraphDatabaseService graphDb,
                               final Index<Node> nodeIndex) {
        for (Node node : graphDb.getAllNodes()) {
            for (Relationship rel : node.getRelationships()) {
                rel.delete();
            }
            nodeIndex.remove(node);
            node.delete();
        }
    }

    public static void registerShutdownHook(final GraphDatabaseService graphDb) {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running example before it's completed)
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
    }
}