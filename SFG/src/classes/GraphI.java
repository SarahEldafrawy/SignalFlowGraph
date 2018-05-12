package classes;

import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

public interface GraphI {

    boolean addEdge(String source, String destination, DefaultWeightedEdge e);

    DefaultWeightedEdge addEdge(String source, String destination);

    boolean conatinsEdge(DefaultWeightedEdge e);

    boolean containsEdge(String source, String destination);

    boolean addVertices(String v);

    boolean containsVertex(String v);


    Graph<String, DefaultWeightedEdge> getGraph();

    boolean setInputNode(String n);

    boolean setOutputNode(String n);

    String getInputNode();

    String getOutputNode();

    boolean deleteEdge(DefaultWeightedEdge e);

    boolean deleteVertex(String v);

    boolean deleteAllVertices();

    Set<DefaultWeightedEdge> getEdges();

    Set<DefaultWeightedEdge> getEdges(String v);

    Set<DefaultWeightedEdge> getpath(String sourceVertex, String targetVertex);

    Set<DefaultWeightedEdge> inEdges(String v);

    Set<DefaultWeightedEdge> outEdges(String v);

    void setEdgeWeight(DefaultWeightedEdge e, double weight);

    List<GraphPath<String, DefaultWeightedEdge>> getAllPaths(String sourceVertex,
                                                             String targetVertex);

    // double getPathWeight(String source,
//       String sink);
    Set<String> getVertices();

    List<List<String>> findSimpleCycles();


}
