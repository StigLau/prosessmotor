package spatial;

import org.neo4j.gis.spatial.indexprovider.SpatialIndexProvider;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpatialExample {
    public static void main(String[] args) throws IOException {
        List<String> lines = readFile("stadiums.csv");

        EmbeddedGraphDatabase db = new EmbeddedGraphDatabase("/Users/stiglau/verktoy/neo4j-community-1.9.M04/data/graph.db");
        Index<Node> index = createSpatialIndex(db, "stadiumsLocation");
        Transaction tx = db.beginTx();

        for (String stadium : lines) {
            String[] columns = stadium.split(",");
            Node stadiumNode = db.createNode();
            stadiumNode.setProperty("wkt", String.format("POINT(%s %s)", columns[4], columns[3]));
            stadiumNode.setProperty("name", columns[0]);
            index.add(stadiumNode, "dummy", "value");
        }

        tx.success();
        tx.finish();
    }

    private static Index<Node> createSpatialIndex(EmbeddedGraphDatabase db, String indexName) {
        return db.index().forNodes(indexName, SpatialIndexProvider.SIMPLE_WKT_CONFIG);
    }

    private static List<String> readFile(String stadiumsFile) throws IOException {
        List<String> lines = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new FileReader(stadiumsFile));
        br.readLine();
        String line = br.readLine();
        while (line != null) {
            lines.add(line);
            line = br.readLine();
        }
        return lines;
    }
}