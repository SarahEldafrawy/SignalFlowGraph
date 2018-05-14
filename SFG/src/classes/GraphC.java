package classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgrapht.*;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.alg.cycle.*;
import org.jgrapht.alg.shortestpath.*;

public class GraphC implements GraphI {
    private DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> graph;

    private String inputNode, outputNode;
    private static GraphC instance = null;

    private GraphC() {
        graph = new DefaultDirectedWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        inputNode = "";
        outputNode = "";
    }

    public static GraphC getInstance() {
        if (instance == null) {
            instance = new GraphC();
        }
        return instance;
    }

    @Override
    public boolean addEdge(String source, String destination, DefaultWeightedEdge e) {

        return graph.addEdge(source, destination, e);
    }

    @Override
    public DefaultWeightedEdge getEdge(String source, String destination) {

        return graph.getEdge(source,destination);
    }

    @Override
    public DefaultWeightedEdge addEdge(String source, String destination) {
        return graph.addEdge(source, destination);
    }

    @Override
    public boolean conatinsEdge(DefaultWeightedEdge e) {
        return graph.containsEdge(e);
    }

    @Override
    public boolean containsEdge(String source, String destination) {
        return graph.containsEdge(source, destination);
    }

    @Override
    public boolean addVertices(String v) {
        return graph.addVertex(v);
    }

    @Override
    public boolean containsVertex(String v) {
        return graph.containsVertex(v);
    }

    @Override
    public Graph<String, DefaultWeightedEdge> getGraph() {
        return graph;
    }

    @Override
    public boolean setInputNode(String n) {
        if (containsVertex(n)) {
            inputNode = n;
            return true;
        }

        return false;
    }

    @Override
    public boolean setOutputNode(String n) {
        if (containsVertex(n)) {
            outputNode = n;
            return true;
        }

        return false;
    }

    @Override
    public String getInputNode() {
        return inputNode;
    }

    @Override
    public String getOutputNode() {
        return outputNode;
    }

    @Override
    public boolean deleteEdge(DefaultWeightedEdge e) {

        return graph.removeEdge(e);

    }

    @Override
    public boolean deleteVertex(String v) {

        return graph.removeVertex(v);
    }
    @Override
    public boolean deleteAllVertices() {
        List<String> list = new ArrayList<>(graph.vertexSet());
        while (!list.isEmpty()) {
            graph.removeVertex(list.get(0));
            list.remove(0);
        }
        System.out.println(graph.vertexSet().isEmpty());
        System.out.println(graph.edgeSet().isEmpty());
        return graph.vertexSet().isEmpty();
    }

    @Override
    public Set<DefaultWeightedEdge> getEdges() {
        return graph.edgeSet();
    }

    @Override
    public Set<DefaultWeightedEdge> getEdges(String v) {
        return graph.edgesOf(v);
    }

    @Override
    public Set<DefaultWeightedEdge> getpath(String sourceVertex, String targetVertex) {
        return graph.getAllEdges(sourceVertex, targetVertex);
    }

    @Override
    public Set<DefaultWeightedEdge> inEdges(String v) {
        return graph.outgoingEdgesOf(v);
    }

    @Override
    public Set<DefaultWeightedEdge> outEdges(String v) {
        return graph.incomingEdgesOf(v);
    }

    @Override
    public void setEdgeWeight(DefaultWeightedEdge e, double weight) {
        graph.setEdgeWeight(e, weight);
    }

    @Override
    public List<GraphPath<String, DefaultWeightedEdge>> getAllPaths(String sourceVertex, String targetVertex) {
        System.out.println("Shortest path from i to c:");
        AllDirectedPaths<String, DefaultWeightedEdge> directedPaths = new AllDirectedPaths<>(graph);
        return directedPaths.getAllPaths(sourceVertex, targetVertex, true, this.getVertices().size());
    }

    /*
     * @Override public double getPathWeight(String source, String sink) {
     * DefaultWeightedEdge = graph.ge return graph.we; }
     */
    @Override
    public Set<String> getVertices() {

        return graph.vertexSet();
    }

    @Override
    public List<List<String>> findSimpleCycles() {

        SzwarcfiterLauerSimpleCycles<String, DefaultWeightedEdge> cycleFind = new SzwarcfiterLauerSimpleCycles<>();
        cycleFind.setGraph(graph);

        return cycleFind.findSimpleCycles();
    }

}
