package classes;

public class Edge {
	private double gain;
	private Node from;
	private Node to;
	
	public Edge(double gain,Node from , Node to) {
		this.gain = gain;
		this.from = from;
		this.to = to;
	}


	public boolean equals(Object obj) {
		Edge t = (Edge) obj;
		if(this.from.equals(t.from) && this.to.equals(t.to) && this.gain == t.gain)
			return true;
		return false;
	}
	
	public double getGain () {
		return gain;
	}

	public void setGain (double gain) {
		this.gain = gain;
	}

	public Node getFrom() {
		return from;
	}

	public void setFrom(Node from) {
		this.from = from;
	}

	public Node getTo() {
		return to;
	}

	public void setTo(Node to) {
		this.to = to;
	}
}
