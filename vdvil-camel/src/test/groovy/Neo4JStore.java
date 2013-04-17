import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.kernel.Traversal;

public class Neo4JStore implements Processor {

    public GraphDatabaseService graphDb;
    public String command;
    public String key;
    public String value;

    public enum Command {
        setProperty, findByProperty
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Object body = exchange.getIn().getBody();

        if (command.equals(Command.setProperty.name())) {
            Transaction tx = graphDb.beginTx();
            try {

                Node firstNode = graphDb.createNode();
                firstNode.setProperty(key, value);
                System.out.println("Saving " + value);

                /*
                Node secondNode = graphDb.createNode();
            secondNode.setProperty( "message", "World!" );

            relationship = firstNode.createRelationshipTo( secondNode, RelTypes.KNOWS );
            relationship.setProperty( "message", "brave Neo4j " );
                 */
                //
                tx.success();
            } finally { tx.finish(); }
        }
    }
}