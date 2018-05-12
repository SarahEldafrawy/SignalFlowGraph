package classes;

import java.util.Set;

import classes.GraphC;
import classes.GraphI;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Main {

	public static void main(String[] args) {

		GraphI g =  GraphC.getInstance();
		g.addVertices("R");
		g.addVertices("x1");
		g.addVertices("x2");
		g.addVertices("x3");
		g.addVertices("x4");
		g.addVertices("C");
		Set<String> sV = g.getVertices();
		g.addEdge("R", "x1");
		g.setEdgeWeight(g.getGraph().getEdge("R", "x1"), 5);
		g.addEdge("x1", "x2");
		g.addEdge("x2", "x3");
		g.addEdge("x3", "x4");
		g.addEdge("x4", "C");

		g.setEdgeWeight(g.getGraph().getEdge("x1", "x2"), 5);
		g.setEdgeWeight(g.getGraph().getEdge("x2", "x3"), 5);
		g.setEdgeWeight(g.getGraph().getEdge("x3","x4"), 5);
		g.setEdgeWeight(g.getGraph().getEdge("x4", "C"), 5);
g.addEdge("x1", "x3");
g.addEdge("x2", "C");
g.addEdge("C", "x1");
g.addEdge("x4", "x3");
g.setEdgeWeight(g.getGraph().getEdge("C", "x1"), 5);
g.setEdgeWeight(g.getGraph().getEdge("x4", "x3"), 5);
g.setEdgeWeight(g.getGraph().getEdge("x2", "C"), 5);
g.setEdgeWeight(g.getGraph().getEdge("x1", "x3"), 5);
Set<DefaultWeightedEdge> sE = g.getEdges();
for(DefaultWeightedEdge e : sE ){
	System.out.println(g.getGraph().getEdgeWeight(e));
}

MasonsF m = new MasonsF();
double s = m.solveG("R", "C");



	}

}
