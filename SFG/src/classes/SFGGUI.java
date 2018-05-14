package classes;

import javax.swing.*;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class SFGGUI extends JFrame {

    final String ADDEDGE = "add edge";
    final String DELETE = "delete";
    private String btnClicked;
    private int countCicks = 0;
    private int vertix = 1;
    private mxGraph graph;
    Object parent;
    Object v1;
    Object v2;
    private mxGraphComponent graphComponent;
    private GraphC graphT = GraphC.getInstance();

    private static JFrame frame;
    private JButton addEdgeBtn;
    private JButton addVertexBtn;
    private JButton okayBtn;
    private JButton newGraphBtn;
    private JPanel graphPlane;
    private JPanel bigPlane;
    private JButton removeBtn;
    private JTextField weightEdit;
    private mxCell lastSelectedEdge;

    public SFGGUI() {
        addVertexBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                untoggleEdgeBtn();
                graph.getModel().beginUpdate();
                try {
                    graph.insertVertex(parent, null, "V" + vertix, 50, 50, 50, 50);
                    graphT.addVertices("V" + vertix);
                    vertix++;
                } finally {
                    graph.getModel().endUpdate();
                }
            }
        });
        newGraphBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                untoggleEdgeBtn();
//                graph.clearSelection();
//                graph.selectAll();
//                Object[] cells = graph.getSelectionCells();
//                Point pts = ((mxCell)cells[0]).getGeometry().getPoint();
                vertix = 0;
                graph.removeCells(graph.getChildVertices(graph.getDefaultParent()));
                graphT.deleteAllVertices();
                addRandC();
            }
        });
        addEdgeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnClicked = ADDEDGE;
                addEdgeBtn.setEnabled(false);
            }
        });
        okayBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                untoggleEdgeBtn();
                StringBuilder stringToDisplay = new StringBuilder();

                MasonsF mason = new MasonsF();

                Double TransFun = TransFun = mason.solveG(graphT.getInputNode(), graphT.getOutputNode());


                stringToDisplay.append("Forward Paths:" + "\n");
                List<GraphPath<String, DefaultWeightedEdge>> forwardPaths = mason.getForwardP();
                List<Double> gainFP = mason.getGainFP();
                for (int i = 0; i < forwardPaths.size(); i++) {
                    stringToDisplay.append(displayStr("P" + i + ": ", forwardPaths.get(i).getVertexList()));
                    stringToDisplay.append("    " + "M: " + gainFP.get(i));
                    stringToDisplay.append("\n");
                }

                stringToDisplay.append("\n" + "Individual Loops:" + "\n");
                List<List<String>> individualLoops = mason.getIndividualLoops();
                List<Double> loopsGain = mason.getLoopsGain();
                if (individualLoops.size() == 0) {
                    stringToDisplay.append("there isn't any" + "\n");
                } else {
                    for (int i = 0; i < individualLoops.size(); i++) {
                        stringToDisplay.append(displayStr("L" + i + ": ", individualLoops.get(i)));
                        stringToDisplay.append("    " + "M: " + loopsGain.get(i));
                        stringToDisplay.append("\n");
                    }
                }
                List<List<List<Integer>>> loopsList = mason.getNonTouching();
                stringToDisplay.append("\n" + "Non-TouchingLoops:" + "\n");
                if (loopsList.size() == 0) {
                    stringToDisplay.append("there isn't any" + "\n");
                } else {
                    for (int k = 0; k < loopsList.size(); k++) {
                        for (int j = 0; j < loopsList.get(k).size(); j++) {
                            for (int i = 0; i < loopsList.get(k).get(j).size(); i++) {
                                if (i == 0) {
                                    stringToDisplay.append("L" + loopsList.get(k).get(j).get(i));
                                    continue;
                                }
                                stringToDisplay.append(", L" + loopsList.get(k).get(j).get(i));
                            }
                            stringToDisplay.append("\n");
                        }
                    }
                }
                List<Double> deltaFP = mason.getDeltaFP();
                stringToDisplay.append("\n" + "Deltas:" + "\n");
                for (int i = 0; i < deltaFP.size(); i++) {
                    if (i == 0) {
                        stringToDisplay.append("D" + i + ": " + deltaFP.get(i));
                        continue;
                    }
                    stringToDisplay.append(",  D" + i + ": " + deltaFP.get(i));
                }
                stringToDisplay.append("\n" + "Main Delta:" + "\t" + mason.getDelta());
                stringToDisplay.append("\n" + "Overall Transfer Function:" + "\t" + TransFun);
                /**
                 * get forward paths + gain
                 * get loops + gain
                 * get non touching loops + gain
                 * delta 1,..n
                 * Delta
                 * Overall T.F.
                 */
                JTextArea textArea = new JTextArea(stringToDisplay.toString());
                JScrollPane scrollPane = new JScrollPane(textArea);
                textArea.setLineWrap(true);
                Font font = new Font("Segoe Script", Font.BOLD, 12);
                textArea.setFont(font);
                textArea.setMargin(new Insets(15,15,15,15));
                textArea.setWrapStyleWord(true);
                scrollPane.setPreferredSize( new Dimension( 500, 500 ) );
                JOptionPane.showMessageDialog(frame,
                        scrollPane,
                        "The Overall Transfer Function",
                        JOptionPane.PLAIN_MESSAGE);
            }
        });
        removeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                untoggleEdgeBtn();
                btnClicked = DELETE;
            }
        });
    }

    private void addRandC() {
        graph.insertVertex(parent, null, "R", 100, 100, 50, 50);
        graph.insertVertex(parent, null, "C", 600, 100, 50, 50);

        graphT = GraphC.getInstance();
        graphT.addVertices("R");
        graphT.addVertices("C");
        graphT.setInputNode("R");
        graphT.setOutputNode("C");
    }

    private String displayStr(String start, List<String> list) {
        StringBuilder str = new StringBuilder(start);

        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                str.append(list.get(i));
            } else {
                str.append(", " + list.get(i));
            }
        }
        return str.toString();
    }

    public static void main(String[] args) {
        frame = new JFrame("SFG");
        frame.setContentPane(new SFGGUI().bigPlane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(1000, 650);
        frame.setVisible(true);
    }

    private void createUIComponents() {
        graphPlane = new SFGGUI.NewPanel();
        graphPlane.setBackground(Color.white);
        getContentPane().add(graphPlane);
    }

    class NewPanel extends JPanel {

        NewPanel() {
            graph = new mxGraph();
            parent = graph.getDefaultParent();
            graph.setAllowDanglingEdges(false);

            addRandC();

            graphComponent = new mxGraphComponent(graph);
            graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (lastSelectedEdge != null) {
                        updateEdgeValue();
                    }
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        mxCell cell = (mxCell) graphComponent.getCellAt(e.getX(), e.getY());

                        if (cell != null) {
                            if (cell.isEdge()) {
                                lastSelectedEdge = cell;
                            }
                            updateSelectedVertices(cell);
                        }
                    }
                }
            });
            this.setLayout(new BorderLayout());
            this.add(graphComponent);
        }
    }

    private void updateEdgeValue() {
        double value = Double.parseDouble(weightEdit.getText());
        if (lastSelectedEdge.getValue() != null) {
            String str = (String) lastSelectedEdge.getValue();
            value = Double.parseDouble(str);
        }
        DefaultWeightedEdge edge = graphT.getEdge((String) lastSelectedEdge.getSource().getValue(), (String) lastSelectedEdge.getTarget().getValue());
        graphT.setEdgeWeight(edge, value);
    }

    private void updateSelectedVertices(mxCell cell) {

        if (btnClicked == null) {
            return;
        }
        if (btnClicked.equals(ADDEDGE) && cell.isVertex()) {
            countCicks++;
            if (countCicks == 2 && cell.getValue().equals("R")) {
                countCicks = 0;
                return;
            }
            if (countCicks == 2) {
                v2 = cell;
                graph.getModel().beginUpdate();
                try {
                    graph.insertEdge(parent, null, weightEdit.getText(), v1, v2); // check edge weight
                    graphT.addEdge((String) ((mxCell) v1).getValue(), (String) ((mxCell) v2).getValue()); // check if working
                    graphT.setEdgeWeight(graphT.getGraph().getEdge((String) (((mxCell) v1).getValue()), (String) ((mxCell) v2).getValue()), Double.parseDouble(weightEdit.getText()));
                } finally {
                    graph.getModel().endUpdate();
                }
                graphComponent.refresh();
                countCicks = 0;
            } else {
                v1 = cell;
            }

        } else if (btnClicked.equals(DELETE)) {
            if (cell.getValue().equals("R") || cell.getValue().equals("C")) {
                return;
            }
            graph.getModel().beginUpdate();
            try {
                graph.getModel().remove(cell);
                if (cell.isEdge()) {
                    if (lastSelectedEdge == cell) {
                        lastSelectedEdge = null;
                    }
                    graphT.deleteEdge(graphT.getGraph().getEdge((String) cell.getSource().getValue(), (String) cell.getTarget().getValue()));
                } else if (cell.isVertex()) {
                    graphT.deleteVertex((String) cell.getValue());
                } else {
                    new RuntimeException("Cell isn't vertex nor edge!!");
                }
            } finally {
                graph.getModel().endUpdate();
            }
        }
    }

    private void untoggleEdgeBtn() {
        btnClicked = "";
        countCicks = 0;
        addEdgeBtn.setEnabled(true);
    }
}
