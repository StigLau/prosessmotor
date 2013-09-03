package no.lau;


import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import utils.GraphUtil;

import java.io.File;
import java.io.IOException;

import static utils.GraphUtil.registerShutdownHook;

public class NewStigsTest {

    private static String DB_PATH = "target/neo4j";

    GraphDatabaseService graphDb = new EmbeddedGraphDatabase(DB_PATH);
    Index<Node> nodeIndex;
    ExecutionEngine engine = new ExecutionEngine(graphDb);

    @Test
    public void testCreateWithCypher() throws IOException {
        registerShutdownHook(graphDb);
        nodeIndex = graphDb.index().forNodes("nodes");

        Transaction tx1 = graphDb.beginTx();
        try {
            GraphUtil.cleanUp(graphDb, nodeIndex);

            File file = new File("/Users/stiglau/utvikling/prosessmotor/vdvil-camel/src/test/resources/gardermoen.neo4j.txt");
            String cypherFile = FileUtils.readFileToString(file);
            ExecutionResult result = engine.execute(cypherFile);
            System.out.println(result.dumpToString());






            tx1.success();
        } finally {
            tx1.finish();
        }
        System.out.println("Wee");

        Transaction tx = graphDb.beginTx();
        try {
            new DataBase(graphDb, nodeIndex).createDataset();
            tx.success();
        } finally {
            tx.finish();
        }

        Transaction tx2 = graphDb.beginTx();
        try {
            System.out.println(engine.execute(
                    //"START n=node:nodeIndexName(name= {sk273})\n" +
                    //"start n=node:nodes(name='SK273'), m=node:nodes(name='Gardermoen Security') MATCH n-[*4]-m RETURN n, m"
                    //"start n=node:nodes(name='SK273'), m=node:nodes(name='Gardermoen Railroad Terminal') MATCH n-[*5]-m RETURN n, m"
                    //"start m=node:nodes(name='Gate 24') RETURN m"
                    //"START x  = node:nodes(name='Gardermoen Security') RETURN x"
                    "match n-->m RETURN *"//Index does not work!

            ).dumpToString());
            tx2.success();
        } finally {
            tx2.finish();
        }

    }
}
