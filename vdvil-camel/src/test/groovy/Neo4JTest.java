

import org.junit.Test;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import static org.junit.Assert.assertEquals;

public class Neo4JTest {

    String dbLocation = "/tmp/vdvil/db";

    @Test
    public void testNe04J() {
        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(dbLocation);
        //registerShutdownHook( graphDb );


        Node firstNode;
        Node secondNode;
        Relationship relationship;

        Transaction tx = graphDb.beginTx();
        try
        {
            firstNode = graphDb.createNode();
            firstNode.setProperty( "message", "Hello, " );
            secondNode = graphDb.createNode();
            secondNode.setProperty( "message", "World!" );

            relationship = firstNode.createRelationshipTo( secondNode, RelTypes.KNOWS );
            relationship.setProperty( "message", "brave Neo4j " );

            tx.success();
        }
        finally
        {
            tx.finish();
        }

        assertEquals("Hello, ", firstNode.getProperty( "message" ));
        assertEquals("brave Neo4j ", relationship.getProperty( "message" ));
        assertEquals("World!", secondNode.getProperty( "message" ));

    }


    private static enum RelTypes implements RelationshipType {
        KNOWS
    }



}
