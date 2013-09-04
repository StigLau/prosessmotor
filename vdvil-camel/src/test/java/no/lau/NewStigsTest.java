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

            //engine.execute("create index on :TRANSPORTATION(name)");
            //engine.execute("create index on :POI(name)");
            File file = new File("/Users/stiglau/utvikling/prosessmotor/vdvil-camel/src/test/resources/gardermoen.neo4j.txt");
            String cypherFile = FileUtils.readFileToString(file);
            ExecutionResult result = engine.profile(cypherFile);
            System.out.println(result.dumpToString());
            tx1.success();
        } finally {
            tx1.finish();
        }

        Transaction tx2 = graphDb.beginTx();
        try {
            //engine.execute("create index on :POI(name); ");
            System.out.println(engine.execute(

                    //"match n-->m RETURN *"//Index does not work!
                    "MATCH gt:POI WHERE gt.name='Gate 24' with gt as gate MATCH term:TERMINAL WHERE term.name='Oslo Railroad Terminal' with gate, term MATCH gate <-[r:TRANSPORTATION*]- term return *"
                            //"start n=node:POI('name:*') set n:POI RETURN count(*)"

            ).dumpToString());
            tx2.success();
        } finally {
            tx2.finish();
        }

    }
}
