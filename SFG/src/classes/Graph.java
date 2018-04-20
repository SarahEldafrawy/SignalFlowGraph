package classes;

import java.util.ArrayList;

public class Graph {
	
	protected ArrayList<Node> nodes;
	protected ArrayList<Edge> edges;
	
	public Graph() {
		nodes = new ArrayList<Node>();
		edges = new ArrayList<Edge>();
	}
	
	public void addNode(Node n) {
		nodes.add(n);
	}
	
	public void addEdge(int from , int to , double gain) {
		
		Node f  = nodes.get(nodes.indexOf(new Node(from)));
		Node t  = nodes.get(nodes.indexOf(new Node(to)));
		if(f == null || t == null)
			return;
		Edge e = new Edge(gain, f, t);
		for(int i = 0  ; i < f.getEdges().size(); i++)
			if(f.getEdges().contains(e))
				return;
		f.addEdge(e);
		edges.add(e);
	}
	
	public void removeEdge(int from , int to , double  gain) {
		Node f  = nodes.get(nodes.indexOf(new Node(from)));
		Node t  = nodes.get(nodes.indexOf(new Node(to)));
		
		if(f == null || t == null)
			return;
		
		Edge e  = new Edge(gain, f, t);
		for(int i = 0; i < edges.size(); i++) {
			if(edges.get(i).equals(e)){
				edges.remove(i);
				break;
			}
		}
		
		ArrayList<Edge> edg = f.getEdges();
		for(int i = 0; i < edg.size(); i++) {
			if(edg.get(i).equals(e)){
				edg.remove(i);
				break;
			}
		}
		
		edg = t.getEdges();
		for(int i = 0; i < edg.size(); i++) {
			if(edg.get(i).equals(e)){
				edg.remove(i);
				break;
			}
		}
		
	}
	
	public void addEdge(Edge e) {
		edges.add(e);
	}
	
	
	public void addNode(int n) {
		if(nodes.contains(new Node(n)))
			return;
		nodes.add(new Node(n));
	}
	



	
	public Node getNode(int n) {
		int x = nodes.indexOf(new Node(n));
		if(x == -1)
			return null;
		return nodes.get(x);
	}
	
	public ArrayList<Node> getNodes() {
		return nodes;
	}


	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}


	public ArrayList<Edge> getEdges() {
		return edges;
	}


	public void setEdges(ArrayList<Edge> edges) {
		this.edges = edges;
	}

}
