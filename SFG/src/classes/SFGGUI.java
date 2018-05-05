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
    private String btnClicked;
    private int countCicks = 0;
    private int vertix = 0;
    private mxGraph graph;
    Object parent;
    Object v1;
    Object v2;
    private mxGraphComponent graphComponent;

    private JButton addEdgeBtn;
    private JButton addVertexBtn;
    private JButton okayBtn;
    private JButton newGraphBtn;
    private JPanel graphPlane;
    private JPanel bigPlane;
    private JButton removeBtn;
    private JTextField weightEdit;

    //TODO convert GraphX to GraphT
    //TODO Style your app
    public SFGGUI() {
        addVertexBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                untoggleEdgeBtn();
                graph.getModel().beginUpdate();
                try {
                    Object v1 = graph.insertVertex(parent, null, "kl", 50, 50, 30, 30);
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
                //TODO
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

    public static void main(String[] args) {

        JFrame frame = new JFrame("SFG");
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
            ////////////////////////////////////////
            graph.getModel().beginUpdate();
            try {
                Object v1 = graph.insertVertex(parent, null, "Hello", 20, 20, 80, 30);
                Object v2 = graph.insertVertex(parent, null, "World!", 300, 150, 80, 30);
                graph.insertEdge(parent, null, "Edge", v1, v2);
            } finally {
                graph.getModel().endUpdate();
            }
            ////////////////////////////////////////////////////
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
