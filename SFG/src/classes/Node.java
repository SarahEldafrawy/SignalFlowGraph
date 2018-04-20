package classes;

import java.util.ArrayList;

public class Node {
	private int num;
	private ArrayList<Edge> edges;
	public Node(int num) {
		this.num = num;
		edges = new ArrayList<Edge>();
	}
	
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	
	
	public void addEdge(double gain, Node to) {
		edges.add(new Edge(gain, this, to));
	}
	
	public void removeEdge(Edge e) {
		edges.remove(e);
	}

	public ArrayList<Edge> getEdges() {
		return edges;
	}

	public void setEdges(ArrayList<Edge> edges) {
		this.edges = edges;
	}

	public void addEdge(Edge e) {
		edges.add(e);
		
	}
	
	


}
