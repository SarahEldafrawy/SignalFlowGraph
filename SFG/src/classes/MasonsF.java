package classes;

import java.util.ArrayList;
import java.util.HashSet;

import java.util.List;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.cycle.SzwarcfiterLauerSimpleCycles;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedSubgraph;

public class MasonsF {

    ////////////////////////
    private List<GraphPath<String, DefaultWeightedEdge>> forwardP;
    private List<Double> gainFP;
    private List<Double> deltaFP;
    private List<List<List<Integer>>> nonTouching = new ArrayList<>();
    private double delta;
    private List<List<String>> individualLoops;
    private List<Double> loopsGain;
    ////////////////////////////////

    private GraphC g = GraphC.getInstance();

    /**
     * Solve mason's formula
     *
     * @param source
     * @param destination
     * @return
     */
    public double solveG(String source, String destination) {
        if (source.equals(destination)) { //TODO added
            throw new RuntimeException("Same vertix");
        }
        individualLoops = g.findSimpleCycles();
        nonTouching = new ArrayList<>();
        loopsGain = new ArrayList<>();
        delta = computeDelta(individualLoops);
        forwardP = g.getAllPaths(source, destination);
        int numOfFP = forwardP.size();
        gainFP = computeGain(numOfFP);
        deltaFP = deltaEachFP(numOfFP);
        double numerator = 0;
        for (int i = 0; i < numOfFP; i++) {
            numerator += (gainFP.get(i) * deltaFP.get(i));
        }
        return numerator / delta;
    }

    /**
     * find delta for each fprward path
     *
     * @param numOfFP
     * @return
     */

    private List<Double> deltaEachFP(int numOfFP) {
        List<Double> delta = new ArrayList<>();
        for (int i = 0; i < numOfFP; i++) {
            Set<String> vFP = new HashSet<>(forwardP.get(i).getVertexList());
            Set<String> remain = getUntouchedGraph(vFP);
            //remain.removeAll(vFP);
            DirectedSubgraph<String, DefaultWeightedEdge> subG = new DirectedSubgraph<String, DefaultWeightedEdge>(
                    (DirectedGraph<String, DefaultWeightedEdge>) g.getGraph(), remain);
            SzwarcfiterLauerSimpleCycles<String, DefaultWeightedEdge> cycleFind = new SzwarcfiterLauerSimpleCycles<>();
            cycleFind.setGraph(subG);
            List<List<String>> cycles = cycleFind.findSimpleCycles();
            delta.add(computeDelta(cycles));
        }
        return delta;
    }

    private Set<String> getUntouchedGraph(Set<String> vFP) {
        List<String> gV = new ArrayList<>(g.getVertices());
        for (String v : vFP) {
            if (gV.contains(v)) {
                gV.remove(v);
            }
        }
        return new HashSet<String>(gV);
    }

    /**
     * compute Mn: gain of FP number n
     *
     * @param numOfFP
     * @return
     */
    public List<Double> computeGain(int numOfFP) {
        List<Double> gains = new ArrayList<>();
        for (int i = 0; i < numOfFP; i++) {
            List<DefaultWeightedEdge> es = forwardP.get(i).getEdgeList();
            double sum = 1;
            for (DefaultWeightedEdge e : es) {
                sum *= g.getGraph().getEdgeWeight(e);
            }
            gains.add(sum);
        }
        return gains;
    }

    /**
     * find delta for all graphs
     *
     * @param cycles
     * @return
     */
    public double computeDelta(List<List<String>> cycles) {
        boolean[][] checkTouch = this.untouchedLoops(cycles);
        int f = 2;
        double sum = 0;
        double total = 1 - computeTotalCyclesGain(cycles);
        List<Integer> loops = new ArrayList<>();

        for (int n = 2; n < checkTouch.length; n++) {
            int count = 1;
            for (int i = 0; i < checkTouch.length - 1; i++) {
                count = 1;
                for (int k = i + 1; k < checkTouch[i].length; k++) {
                    if (!checkTouch[i][k]) {
                        count++;
                        if (!loops.contains(i))
                            loops.add(i);
                        loops.add(k);

                    }
                    if (count == n) {
                        if (n == 2) {
                            int c = nonTouching.size();
                            while (n - 2 >= c) {
                                nonTouching.add(c, new ArrayList<List<Integer>>());
                                c++;
                            }
                            nonTouching.get(n - 2).add(new ArrayList<>(loops));
                            double tmpSum = computeWeightOfLoop(loops, cycles);
                            sum += tmpSum;

                        } else {
                            if (checkIfAllNonTouching(checkTouch, loops)) {
                                int c = nonTouching.size();
                                while (n - 2 >= c) {
                                    nonTouching.add(c, new ArrayList<List<Integer>>());
                                    c++;
                                }
                                nonTouching.get(n - 2).add(new ArrayList<>(loops));
                                double tmpSum = computeWeightOfLoop(loops, cycles);
                                sum += tmpSum;
                            }
                        }
                        count = 1;
                        loops.clear();

                    }
                }
            }
            total += (sum * Math.pow(-1, f));
            sum = 0;
            f++;
        }
        return total;
    }

    private boolean checkIfAllNonTouching(boolean[][] checkTouch, List<Integer> loops) {
        for (int i = 1; i < loops.size() - 1; i++) {
            for (int j = i + 1; j < loops.size(); j++) {
                if (checkTouch[loops.get(i)][loops.get(j)]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Total gain of cycles
     *
     * @param cycles
     * @return
     */

    private double computeTotalCyclesGain(List<List<String>> cycles) {
        int total = 0;
        for (List<String> vL : cycles) {
            Double g = gainOfLoop(vL);
            loopsGain.add(g);
            total += g;
        }
        return total;
    }

    private double gainOfLoop(List<String> vL) {
        List<DefaultWeightedEdge> edges = new ArrayList<>();
        for (int i = 0; i < vL.size(); i++) {
            if (i < vL.size() - 1) {
                edges.add(g.getGraph().getEdge(vL.get(i), vL.get(i + 1)));
            } else {
                edges.add(g.getGraph().getEdge(vL.get(i), vL.get(0)));
            }
        }
        int sum = 1;
        for (DefaultWeightedEdge e : edges) {
            sum *= g.getGraph().getEdgeWeight(e);
        }
        return sum;

    }

    /**
     * gets the total gain for n non touching loops
     *
     * @param loops
     * @return
     */
    public double computeWeightOfLoop(List<Integer> loops, List<List<String>> cycles) {
        int total = 1;
        for (Integer i : loops) {
            total *= loopsGain.get(i);
        }
        return total;
    }

    /**
     * check the untouching and touching loops
     *
     * @param cycles
     * @return
     */
    public boolean[][] untouchedLoops(List<List<String>> cycles) {
        boolean[][] checkTouch = new boolean[cycles.size()][cycles.size()];
        int numberOfCycles = cycles.size();
        for (int i = 0; i < numberOfCycles - 1; i++) {
            List<String> vLoop1 = cycles.get(i);
            int sizeL1 = vLoop1.size();
            for (int j = 0; j < sizeL1; j++) {
                for (int k = i + 1; k < numberOfCycles; k++) {
                    if (checkTouch[i][k]) {
                        continue;
                    }
                    if (cycles.get(k).contains(vLoop1.get(j))) {
                        checkTouch[i][k] = true;
                    }
                }
            }
        }
        return checkTouch;
    }

    //////////////////////////////////////////////////
    public List<Double> getGainFP() {
        return gainFP;
    }

    public List<GraphPath<String, DefaultWeightedEdge>> getForwardP() {
        return forwardP;
    }
    public List<Double> getDeltaFP() {
        return deltaFP;
    }

    public List<List<List<Integer>>> getNonTouching() {
        return nonTouching;
    }

    public double getDelta() {
        return delta;
    }

    public List<List<String>> getIndividualLoops() {
        return individualLoops;
    }

    public List<Double> getLoopsGain() {
        return loopsGain;
    }
}