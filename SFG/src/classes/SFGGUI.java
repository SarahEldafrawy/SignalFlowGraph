package classes;

import javax.swing.*;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SFGGUI extends JFrame {

    final String ADDEDGE = "add edge";
    final String DELETE = "delete";
    final String SINK = "sink";
    final String SOURCE = "source";
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
    private JButton setSinkBtn;
    private JButton setSourceBtn;

    //TODO Style your app
    public SFGGUI() {
        addVertexBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                untoggleEdgeBtn();
                graph.getModel().beginUpdate();
                try {
                    Object v1 = graph.insertVertex(parent, null, "V" + vertix, 50, 50, 30, 30);
                    graphT.addVertices("V" + vertix);
                } finally {
                    graph.getModel().endUpdate();
                }
            }
        });
        newGraphBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                untoggleEdgeBtn();
                graph.removeCells(graph.getChildVertices(graph.getDefaultParent()));
                graphT.deleteAllVertices();
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
                //TODO compute Graph and set results in a dialog
                JOptionPane.showMessageDialog(frame,
                        "blah",
                        "A plain message",
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
        setSourceBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                untoggleEdgeBtn();
                btnClicked = SOURCE;
            }
        });
        setSinkBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                untoggleEdgeBtn();
                btnClicked = SINK;
            }
        });
    }

    public static void main(String[] args) {

        frame = new JFrame("SFG");
        frame.setContentPane(new SFGGUI().bigPlane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        graphPlane = new SFGGUI.NewPanel();
        getContentPane().add(graphPlane);
    }

    class NewPanel extends JPanel {

        NewPanel() {
            graph = new mxGraph();
            parent = graph.getDefaultParent();
            graph.setAllowDanglingEdges(false);
//            ////////////////////////////////////////
//            graph.getModel().beginUpdate();
//            try {
//                Object v1 = graph.insertVertex(parent, null, "Hello", 20, 20, 80, 30);
//                Object v2 = graph.insertVertex(parent, null, "World!", 300, 150, 80, 30);
//                graph.insertEdge(parent, null, "Edge", v1, v2);
//            } finally {
//                graph.getModel().endUpdate();
//            }
//            ////////////////////////////////////////////////////
            graphComponent = new mxGraphComponent(graph);
            graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        mxCell cell = (mxCell) graphComponent.getCellAt(e.getX(), e.getY());

                        if (cell != null) {
                            updateSelectedVertices(cell);
                        }
                    }
                }
            });
            this.setLayout(new BorderLayout());
            this.add(graphComponent);
        }
    }

    private void updateSelectedVertices(mxCell cell) {

        if (btnClicked.equals(ADDEDGE)) {
            countCicks++;
            if (countCicks == 2) {
                graph.getModel().beginUpdate();
                try {
                    v2 = cell;
                    graph.insertEdge(parent, null, weightEdit.getText(), v1, v2); // check edge weight
                    graphT.addEdge(((mxCell) v1).getId(), ((mxCell) v2).getId()); // check if working
                    graphT.setEdgeWeight(graphT.getGraph().getEdge((((mxCell) v1).getId()), ((mxCell) v2).getId()), Integer.parseInt(weightEdit.getText()));
                } finally {
                    graph.getModel().endUpdate();
                }
                graphComponent.refresh();
                countCicks = 0;
            } else {
                v1 = cell;
            }

        } else if (btnClicked.equals(DELETE)) {
            graph.getModel().beginUpdate();
            try {
                graph.getModel().remove(cell);
                if (cell.isEdge()) {
                    graphT.deleteEdge(graphT.getGraph().getEdge(cell.getSource().getId(),cell.getTarget().getId()));
                } else if (cell.isVertex()) {
                    graphT.deleteVertex(cell.getId());
                } else {
                    new RuntimeException("Cell isn't vertex nor edge!!");
                }
            } finally {
                graph.getModel().endUpdate();
            }
        } else if (btnClicked.equals(SOURCE) && cell.isVertex()){
            graphT.setInputNode(cell.getId());
        } else if (btnClicked.equals(SINK) && cell.isVertex()) {
            graphT.setOutputNode(cell.getId());
        }
    }

    private void untoggleEdgeBtn() {
        btnClicked = "";
        countCicks = 0;
        addEdgeBtn.setEnabled(true);
    }
}
